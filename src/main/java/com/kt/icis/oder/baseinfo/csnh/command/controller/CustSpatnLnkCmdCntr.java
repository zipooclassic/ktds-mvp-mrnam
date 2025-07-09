/**************************************************************************************
 * ICIS version 1.0
 *
 *  Copyright ⓒ 2022 kt/ktds corp. All rights reserved.
 *
 *  This is a proprietary software of kt corp, and you may not use this file except in
 *  compliance with license agreement with kt corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of kt corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 *************************************************************************************/

package com.kt.icis.oder.baseinfo.csnh.command.controller;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.icis.cmmnfrwk.utils.JsonUtil;
import com.kt.icis.cmmnfrwk.utils.LogUtil;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciacustdivdCsnh622dDto;
import com.kt.icis.oder.baseinfo.common.service.BtCciaCustDivdSvc;
import com.kt.icis.oder.baseinfo.common.service.BtEiCordordinfoSvc;
import com.kt.icis.oder.baseinfo.common.service.BtEtCordordinfoSvc;
import com.kt.icis.oder.baseinfo.common.service.BtIaCordordinfoSvc;
import com.kt.icis.oder.baseinfo.common.service.BtIcCordordinfoSvc;
import com.kt.icis.oder.baseinfo.common.service.BtInCordordinfoSvc;
import com.kt.icis.oder.baseinfo.common.service.BtPpCordordinfoSvc;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.CustPartiTrtInCmdPyld;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.dto.CustPartiTrtInCmdDto;
import com.kt.icis.oder.common.constants.LobAllConstants;
import com.kt.icis.oder.common.exception.OrderException;
import com.kt.icis.oder.common.kafka.constants.KafkaServiceConstants;
import com.kt.icis.oder.common.kafka.controller.KafkaProducerCntr;
import com.kt.icis.oder.common.kafka.dto.KafkaSendInDto;
import com.kt.icis.oder.lob.bizcomn.controller.SaIdCntr;
import com.kt.icis.oder.lob.bizcomn.payload.out.dto.LobCcpiprodhierChkSaIdOutDto;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustSpatnLnkCmdCntr {

    private final BtCciaCustDivdSvc btCciacustdivdSvc;
    private final BtPpCordordinfoSvc btPpCordordinfoSvc;
    private final BtIcCordordinfoSvc btIcCordordinfoSvc;
    private final BtEiCordordinfoSvc btEiCordordinfoSvc;
    private final BtIaCordordinfoSvc btIaCordordinfoSvc;
    private final BtInCordordinfoSvc btInCordordinfoSvc;
    private final BtEtCordordinfoSvc btEtCordordinfoSvc;
    
//    private final EiCustPartiTrtCmdClnt eiCustPartiTrtCmdClnt;
//    private final EtCustPartiTrtCmdClnt etCustPartiTrtCmdClnt;
//    private final IaCustPartiTrtCmdClnt iaCustPartiTrtCmdClnt;
//    private final IcCustPartiTrtCmdClnt icCustPartiTrtCmdClnt;
//    private final InCustPartiTrtCmdClnt inCustPartiTrtCmdClnt;
//    private final PpCustPartiTrtCmdClnt ppCustPartiTrtCmdClnt;
    
    private final SaIdCntr saIdCntr;
    
    // Kafka
 	private final KafkaProducerCntr        kafkaProducerCntr;

    /**
     * 고객분리연동
     * @tuxedo CSNH622D
     */
    @Transactional
    //@Scheduled(fixedDelayString = "${oder.batch.cust-spatn-lnk.delay-time}")
    public void custSpatnLnk() {
        LogUtil.info("[starttime/custSpatnLnk]: {} ", new Timestamp(System.currentTimeMillis()));

        List<BtCciacustdivdCsnh622dDto> btCciaCustDivdCsnh622dDtoList = btCciacustdivdSvc.findByCciacustdivdAndccpiprodhierAndccstbasicinfo();

        for(BtCciacustdivdCsnh622dDto dto : btCciaCustDivdCsnh622dDtoList) {
            dto.setProcResult("S");
            dto.setErrMsg(null);

            String checkOnGoingOrdNo = CheckOnGoingOrdNo(dto);

            if ("Fail".equals(checkOnGoingOrdNo)) {
            	dto.setProcResult("F");
                dto.setErrMsg("CORDORDINFO 테이블 조회 오류");
                infUpdateIcisWork(dto);
                
                continue;
            }
            else if (checkOnGoingOrdNo!=null) { //ordNo 있을 경우 continue
                dto.setProcResult("P");
                dto.setErrMsg("진행중 오더가 존재합니다 ORD_NO ["+checkOnGoingOrdNo+"]");
                infUpdateIcisWork(dto);
                
                continue;
            }

            //ordNo 없을 경우
            if (StringUtils.isEmpty(dto.getCheckOldCustId())) {
            	dto.setProcResult("F");
                dto.setErrMsg("ICIS 원부를 찾을 수 없습니다.");
                infUpdateIcisWork(dto);
                
                continue;
            }
            else if ( StringUtils.isNotEmpty(dto.getCustId()) 
		        		&& StringUtils.isNotEmpty(dto.getCheckOldCustId())
		        		&& !dto.getCustId().equals(dto.getCheckOldCustId()) ) {
                dto.setProcResult("F");
                dto.setErrMsg("고객분리 요청한 정보와 ICIS 원부상태(고객ID)가 다릅니다.");
                infUpdateIcisWork(dto);
                
                continue;
            }

            LogUtil.debug("infCallCcmc241s");
            callCcmc241s(dto);
        }
        LogUtil.info("[endtime/custSpatnLnk]: {} ", new Timestamp(System.currentTimeMillis()));
    }

    private void infUpdateIcisWork(BtCciacustdivdCsnh622dDto dto) {
        LogUtil.info("procResult[{}]", dto.getProcResult());
        LogUtil.info("errMsg[{}]", dto.getErrMsg());
        btCciacustdivdSvc.updateBySaidCustidOccurdateNewcustid(dto);
    }
    
    private CustPartiTrtInCmdDto getMappedCustPartiTrtDto(BtCciacustdivdCsnh622dDto dto) {
    	return CustPartiTrtInCmdDto.builder()
    			.saId(dto.getSaId())
    			.befCustId(dto.getCustId())
    			.aftCustId(dto.getNewCustId())
    			.regOfcCd(dto.getRegOfcCd())
    			.regerEmpNo(dto.getRegrId())
    			.regerEmpName(dto.getRegerEmpName())
    			.reqerName(dto.getReqerName())
    			.reqerRelation(dto.getCustRelCd())
    			.reqerNo(dto.getReqerCustNo())
    			.reqerTelNo(dto.getReqerPhnNo())
    			.reqerTelNo1(dto.getReqerCellphnNo())
    			.occurDate(dto.getOccurDate())
    			.build();
    }

    private void callCcmc241s(BtCciacustdivdCsnh622dDto dto) {    	
    	LobCcpiprodhierChkSaIdOutDto lobCcpiprodhierChkSaIdOutDto = saIdCntr.chkSaIdLob(dto.getSaId());
//    	CustPartiTrtOutCmdPyld custPartiTrtOutCmdPyld = new CustPartiTrtOutCmdPyld();
    	CustPartiTrtInCmdPyld custPartiTrtInCmdPyld = CustPartiTrtInCmdPyld.builder()
    			.lobCustPartiTrtInCmdDto(this.getMappedCustPartiTrtDto(dto))
    			.build();
    	//custPartiTrtOutCmdPyld = ppCustPartiTrtCmdClnt.regLobCustPartiTrt(CommonUtil.getFeignHeader(), custPartiTrtInCmdPyld);
    	/* ----------------- Kafka 전환시 미사용     	
    	switch (lobCcpiprodhierChkSaIdOutDto.getLobCd()) {
	        case "PP":
	        	custPartiTrtOutCmdPyld = ppCustPartiTrtCmdClnt.regLobCustPartiTrt(CommonUtil.getFeignHeader(), custPartiTrtInCmdPyld);
	            break;
	        case "IC":
	        	custPartiTrtOutCmdPyld = icCustPartiTrtCmdClnt.regLobCustPartiTrt(CommonUtil.getFeignHeader(), custPartiTrtInCmdPyld);
	            break;
	        case "EI":
	        	custPartiTrtOutCmdPyld = eiCustPartiTrtCmdClnt.regLobCustPartiTrt(CommonUtil.getFeignHeader(), custPartiTrtInCmdPyld);
	            break;
	        case "IA":
	        	custPartiTrtOutCmdPyld = iaCustPartiTrtCmdClnt.regLobCustPartiTrt(CommonUtil.getFeignHeader(), custPartiTrtInCmdPyld);
	            break;
	        case "IN":
	        	custPartiTrtOutCmdPyld = inCustPartiTrtCmdClnt.regLobCustPartiTrt(CommonUtil.getFeignHeader(), custPartiTrtInCmdPyld);
	            break;
	        case "ET":
	        	custPartiTrtOutCmdPyld = etCustPartiTrtCmdClnt.regLobCustPartiTrt(CommonUtil.getFeignHeader(), custPartiTrtInCmdPyld);
	            break;
	    }
    	
        if (custPartiTrtOutCmdPyld.getLobCustPartiTrtOutCmdDto() == null 
        		|| "F".equals(custPartiTrtOutCmdPyld.getLobCustPartiTrtOutCmdDto().getVcResultCd())) { // CCT_FAIL
            dto.setProcResult("F");
            dto.setErrMsg("CCT_Msg_SetInputRow(ccmc241s) 호출 오류 입니다");
            infUpdateIcisWork(dto);
        } 
    	-------------------------------- Kafka 전환시 미사용 */
    	
    	// Kafka전환 호출
    	switch (lobCcpiprodhierChkSaIdOutDto.getLobCd()) {
        	case "PP":
	            try{
	                KafkaSendInDto kafkaSendInDto = kafkaProducerCntr.getKafkaSendInDto(
	                        KafkaServiceConstants.KAFKA_PPD_CUST_PARTITRT
	                        , this.getClass().getSimpleName()
	                        , JsonUtil.toJson(custPartiTrtInCmdPyld));
	                
	                //상태변경처리 
	                kafkaSendInDto.setCallbackLob("BID");
	                kafkaSendInDto.setCallbackApi("/csnh/custpartitrt/sttus"); 
	                kafkaProducerCntr.send(kafkaSendInDto);
	            }catch (Exception e){
	            	dto.setProcResult("F");
	                dto.setErrMsg("고객분할처리(ccmc241s) kafka 호출 오류입니다");
	                infUpdateIcisWork(dto);
	                throw new OrderException("MCSNI1051", List.of("고객분할처리(ccmc241s) kafka 호출 오류!!"));
	            }
	            break;
        	case "IC":
	            try{
	                KafkaSendInDto kafkaSendInDto = kafkaProducerCntr.getKafkaSendInDto(
	                        KafkaServiceConstants.KAFKA_ICD_CUST_PARTITRT
	                        , this.getClass().getSimpleName()
	                        , JsonUtil.toJson(custPartiTrtInCmdPyld));
	                
	                //상태변경처리 
	                kafkaSendInDto.setCallbackLob("BID");
	                kafkaSendInDto.setCallbackApi("/csnh/custpartitrt/sttus"); 
	                kafkaProducerCntr.send(kafkaSendInDto);
	            }catch (Exception e){
	            	dto.setProcResult("F");
	                dto.setErrMsg("고객분할처리(ccmc241s) kafka 호출 오류입니다");
	                infUpdateIcisWork(dto);
	            	throw new OrderException("MCSNI1051", List.of("고객분할처리(ccmc241s) kafka 호출 오류!!"));
	            }
	            break;
	        case "EI":
	            try{
	                KafkaSendInDto kafkaSendInDto = kafkaProducerCntr.getKafkaSendInDto(
	                        KafkaServiceConstants.KAFKA_EID_CUST_PARTITRT
	                        , this.getClass().getSimpleName()
	                        , JsonUtil.toJson(custPartiTrtInCmdPyld));
	
	                //상태변경처리 
	                kafkaSendInDto.setCallbackLob("BID");
	                kafkaSendInDto.setCallbackApi("/csnh/custpartitrt/sttus"); 
	                kafkaProducerCntr.send(kafkaSendInDto);
	            }catch (Exception e){
	            	dto.setProcResult("F");
	                dto.setErrMsg("고객분할처리(ccmc241s) kafka 호출 오류입니다");
	                infUpdateIcisWork(dto);
	            	throw new OrderException("MCSNI1051", List.of("고객분할처리(ccmc241s) kafka 호출 오류!!"));
	            }
	            break;
	        case "IA":
	            try{
	                KafkaSendInDto kafkaSendInDto = kafkaProducerCntr.getKafkaSendInDto(
	                        KafkaServiceConstants.KAFKA_IAD_CUST_PARTITRT
	                        , this.getClass().getSimpleName()
	                        , JsonUtil.toJson(custPartiTrtInCmdPyld));
	                
	                //상태변경처리 
	                kafkaSendInDto.setCallbackLob("BID");
	                kafkaSendInDto.setCallbackApi("/csnh/custpartitrt/sttus"); 
	                kafkaProducerCntr.send(kafkaSendInDto);
	            }catch (Exception e){
	            	dto.setProcResult("F");
	                dto.setErrMsg("고객분할처리(ccmc241s) kafka 호출 오류입니다");
	                infUpdateIcisWork(dto);
	            	throw new OrderException("MCSNI1051", List.of("고객분할처리(ccmc241s) kafka 호출 오류!!"));
	            }
	            break;
	        case "IN":
	            try{
	                KafkaSendInDto kafkaSendInDto = kafkaProducerCntr.getKafkaSendInDto(
	                        KafkaServiceConstants.KAFKA_IND_CUST_PARTITRT
	                        , this.getClass().getSimpleName()
	                        , JsonUtil.toJson(custPartiTrtInCmdPyld));
	                
	                //상태변경처리 
	                kafkaSendInDto.setCallbackLob("BID");
	                kafkaSendInDto.setCallbackApi("/csnh/custpartitrt/sttus"); 
	                kafkaProducerCntr.send(kafkaSendInDto);
	            }catch (Exception e){
	            	dto.setProcResult("F");
	                dto.setErrMsg("고객분할처리(ccmc241s) kafka 호출 오류입니다");
	                infUpdateIcisWork(dto);
	            	throw new OrderException("MCSNI1051", List.of("고객분할처리(ccmc241s) kafka 호출 오류!!"));
	            }
	            break;
	        case "ET":
	            try{
	                KafkaSendInDto kafkaSendInDto = kafkaProducerCntr.getKafkaSendInDto(
	                        KafkaServiceConstants.KAFKA_ETD_CUST_PARTITRT
	                        , LobAllConstants.LOB_BI_DAEMON
	                        , JsonUtil.toJson(custPartiTrtInCmdPyld));
	                
	                //상태변경처리 
	                kafkaSendInDto.setCallbackLob("BID");
	                kafkaSendInDto.setCallbackApi("/csnh/custpartitrt/sttus"); 
	                kafkaProducerCntr.send(kafkaSendInDto);
	            }catch (Exception e){
	            	dto.setProcResult("F");
	                dto.setErrMsg("고객분할처리(ccmc241s) kafka 호출 오류입니다");
	                infUpdateIcisWork(dto);
	            	throw new OrderException("MCSNI1051", List.of("고객분할처리(ccmc241s) kafka 호출 오류!!"));
	            }
	            break;
	        default :
	        	break;
    	}
    }

    private String CheckOnGoingOrdNo(BtCciacustdivdCsnh622dDto dto) {
        String ordNo=null;
        try {
            ordNo = btPpCordordinfoSvc.checkOnGoingOrdNo(dto.getSaId());
            if (ordNo==null) {
                ordNo = btIcCordordinfoSvc.checkOnGoingOrdNo(dto.getSaId());
            }
            if (ordNo==null) {
                ordNo = btEiCordordinfoSvc.checkOnGoingOrdNo(dto.getSaId());
            }
            if (ordNo==null) {
                ordNo = btIaCordordinfoSvc.checkOnGoingOrdNo(dto.getSaId());
            }
            if (ordNo==null) {
                ordNo = btInCordordinfoSvc.checkOnGoingOrdNo(dto.getSaId());
            }
            if (ordNo==null) {
                ordNo = btEtCordordinfoSvc.checkOnGoingOrdNo(dto.getSaId());
            }
        }catch(Exception e) {
            return "Fail";
        }

        return ordNo;
    }

}
