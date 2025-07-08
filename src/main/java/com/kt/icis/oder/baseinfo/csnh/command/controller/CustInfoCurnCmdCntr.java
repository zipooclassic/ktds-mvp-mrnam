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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.icis.cmmnfrwk.utils.JsonUtil;
import com.kt.icis.cmmnfrwk.utils.LogUtil;
import com.kt.icis.oder.baseinfo.common.command.payload.in.BiCwkociasatarginfoInCmdPyld;
import com.kt.icis.oder.baseinfo.common.command.payload.in.dto.BiCwkociasatarginfoInCmdDto;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciacustintegAndCciacustchanginfoDto;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCwkociasatarginfoCsnj626Dto;
import com.kt.icis.oder.baseinfo.common.service.BtCciacustintegSvc;
import com.kt.icis.oder.baseinfo.common.service.BtCwkociasatarginfoSvc;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.Csnh626dToCcmc122sInCmdPyld;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.dto.Csnh626dToCcmc122sInCmdDto;
import com.kt.icis.oder.baseinfo.csnh.command.service.CustInfoCurnCmdSvc;
import com.kt.icis.oder.common.exception.OrderException;
import com.kt.icis.oder.common.kafka.constants.KafkaServiceConstants;
import com.kt.icis.oder.common.kafka.controller.KafkaProducerCntr;
import com.kt.icis.oder.common.kafka.dto.KafkaSendInDto;
import com.kt.icis.oder.common.kafka.dto.KafkaSendOutDto;

import lombok.RequiredArgsConstructor;

/**
 * controller
 */
@Component
@RequiredArgsConstructor
public class CustInfoCurnCmdCntr {
    private final BtCciacustintegSvc btCciacustintegSvc;
    private final BtCwkociasatarginfoSvc btCwkociasatarginfoSvc;
    //private final Csnh626dToCcmc122sCmdClnt ccmc122sClnt;
    private final CustInfoCurnCmdSvc custInfoCurnCmdSvc;
    
//    private final SaIdCntr saIdCntr;
    
    // Kafka
    private final KafkaProducerCntr        kafkaProducerCntr;
    
    private static final String GSZ_PGM_NAME = "csnh626d";
    private static final String REG_OFC_CD = "700141";
    
    /**
     * 고객정보 현행화 처리
     * @tuxedo CSNH626D
     */
    @Transactional
//    @Scheduled(fixedDelayString = "${oder.batch.cust-info-curn.delay-time}")
    public void custInfoCurn() {
        LogUtil.info("Batch(custInfoCurn) start : " + ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ISO_DATE_TIME));
        
        List<Csnh626dToCcmc122sInCmdDto> ccmc122sInDtoList = new ArrayList<Csnh626dToCcmc122sInCmdDto>();
        
        List<BtCciacustintegAndCciacustchanginfoDto> dtoList = btCciacustintegSvc.getCciacustintegAndCciacustChanginfo();
        for (BtCciacustintegAndCciacustchanginfoDto dto : dtoList) {
            int nRemainCnt = btCwkociasatarginfoSvc.getCount( dto.getCustProcType(),dto.getCustId(),dto.getOccurDate() );
            if ( nRemainCnt < 0 ) {
                continue;
            } else if ( nRemainCnt == 0 ) {
                ccmc122sInDtoList.add( setCcmc122sInDto( dto, null, null ) );
                continue;
            } else {
                List<BtCwkociasatarginfoCsnj626Dto> btCwkociasatarginfoCsnj626DtoList = btCwkociasatarginfoSvc.getSaIdSaCd( dto.getCustProcType(), dto.getCustId(),dto. getOccurDate() );
                for ( BtCwkociasatarginfoCsnj626Dto btCwkociasatarginfoCsnj626Dto : btCwkociasatarginfoCsnj626DtoList ) {
                    ccmc122sInDtoList.add( setCcmc122sInDto( dto, btCwkociasatarginfoCsnj626Dto.getSaId(), btCwkociasatarginfoCsnj626Dto.getSaCd() ) );
                }
            }
        }
        
        // 122 서비스 호출
        infCallSvcProc( ccmc122sInDtoList );
        
        LogUtil.info("Batch(custInfoCurn) end : " + ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ISO_DATE_TIME));
        
        //고객정보 현행화 처리 kafka 호출여부 체크(updateStatusCdAndProcDate 대상이 있는 경우만 호출) : ICISTR-178048 added 2025.03.05
        int updatedCount = btCwkociasatarginfoSvc.getUpdatedCount();
        if ( updatedCount > 0 ) {
	        // 고객정보 현행화 처리 KAFKA 호출
	        try {
	            LogUtil.info(" \n\n === [ updateStatusCdAndProcDate() start ] ===");
	
	            BiCwkociasatarginfoInCmdDto InCmdDto = new BiCwkociasatarginfoInCmdDto();
	            InCmdDto.setCustId(null);
	            InCmdDto.setCustProcType(null);
	            InCmdDto.setOccurDate(null);
	
	            BiCwkociasatarginfoInCmdPyld inPyld = BiCwkociasatarginfoInCmdPyld.builder().cwkociasatarginfoInCmdDto(InCmdDto).build();
	            KafkaSendInDto  kafkaSendInDto = kafkaProducerCntr.getKafkaSendInDto(
	                    KafkaServiceConstants.KAFKA_WC_CWKO_REGCWKOCIASATARGINFO
	                    , this.getClass().getSimpleName()
	                    , JsonUtil.toJson(inPyld));
	            
	            KafkaSendOutDto kafkaSendOutDto = kafkaProducerCntr.send(kafkaSendInDto);
	            
	            LogUtil.info("\n\n ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★"
	                        + "\n " + JsonUtil.toJson(kafkaSendOutDto)
	                        + "\n ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
	        } 
	        catch (RuntimeException e) {
	            LogUtil.error("고객정보 현행화 처리 kafka 호출 처리 중 오류!", e);
	            throw new OrderException("MCSNI1051", List.of("고객정보 현행화 처리 kafka 호출 처리 중 오류!!"));
	        }
    	}
    }

    private Csnh626dToCcmc122sInCmdDto setCcmc122sInDto(BtCciacustintegAndCciacustchanginfoDto dto, String saId, String saCd) {
        Csnh626dToCcmc122sInCmdDto ccmc122sInDto = new Csnh626dToCcmc122sInCmdDto();
        ccmc122sInDto.setCustProcType(dto.getCustProcType());
        ccmc122sInDto.setCustId(dto.getCustId());
        ccmc122sInDto.setOccurDate(dto.getOccurDate());
        ccmc122sInDto.setSaId(saId);
        ccmc122sInDto.setSaCd(saCd);
        ccmc122sInDto.setExptYn("N");
        ccmc122sInDto.setRegerEmpNo(GSZ_PGM_NAME);
        ccmc122sInDto.setRegerEmpName(GSZ_PGM_NAME);
        ccmc122sInDto.setRegOfcCd(REG_OFC_CD);
        
        return ccmc122sInDto;
    }
    
    private void infCallSvcProc ( List<Csnh626dToCcmc122sInCmdDto> ccmc122sInDtoList ) {
        List<Csnh626dToCcmc122sInCmdDto> ccmc122sPPInDto = new ArrayList<Csnh626dToCcmc122sInCmdDto>();
        List<Csnh626dToCcmc122sInCmdDto> ccmc122sICInDto = new ArrayList<Csnh626dToCcmc122sInCmdDto>();
        List<Csnh626dToCcmc122sInCmdDto> ccmc122sEIInDto = new ArrayList<Csnh626dToCcmc122sInCmdDto>();
        List<Csnh626dToCcmc122sInCmdDto> ccmc122sIAInDto = new ArrayList<Csnh626dToCcmc122sInCmdDto>();
        List<Csnh626dToCcmc122sInCmdDto> ccmc122sINInDto = new ArrayList<Csnh626dToCcmc122sInCmdDto>();
        List<Csnh626dToCcmc122sInCmdDto> ccmc122sETInDto = new ArrayList<Csnh626dToCcmc122sInCmdDto>();
        
        for ( Csnh626dToCcmc122sInCmdDto ccmc122sInDto : ccmc122sInDtoList ) {
//            LobCcpiprodhierChkSaIdOutDto lobCcpiprodhierChkSaIdOutDto = saIdCntr.chkSaIdLob( ccmc122sInDto.getSaId() );
//            String lobCode = lobCcpiprodhierChkSaIdOutDto.getLobCd();
            String lobCode = custInfoCurnCmdSvc.selectLobCdBySaId( ccmc122sInDto.getSaId() );
            
            if ( StringUtils.isNotEmpty( lobCode ) ) {
                switch (lobCode) {
                    case "PP":
                        ccmc122sPPInDto.add( ccmc122sInDto );
                        break;
                    case "IC":
                        ccmc122sICInDto.add( ccmc122sInDto );
                        break;
                    case "EI":
                        ccmc122sEIInDto.add( ccmc122sInDto );
                        break;
                    case "IA":
                        ccmc122sIAInDto.add( ccmc122sInDto );
                        break;
                    case "IN":
                        ccmc122sINInDto.add( ccmc122sInDto );
                        break;
                    case "ET":
                        ccmc122sETInDto.add( ccmc122sInDto );
                        break;
                    default:
                        break;
                }
            } else {
                ccmc122sPPInDto.add( ccmc122sInDto );
                ccmc122sICInDto.add( ccmc122sInDto );
                ccmc122sEIInDto.add( ccmc122sInDto );
                ccmc122sIAInDto.add( ccmc122sInDto );
                ccmc122sINInDto.add( ccmc122sInDto );
                ccmc122sETInDto.add( ccmc122sInDto );
            }
        }
        
        Csnh626dToCcmc122sInCmdPyld ccmc122sPPInPyld = Csnh626dToCcmc122sInCmdPyld.builder().custInfoCurnTrtInCmdDto(ccmc122sPPInDto).build();
        Csnh626dToCcmc122sInCmdPyld ccmc122sICInPyld = Csnh626dToCcmc122sInCmdPyld.builder().custInfoCurnTrtInCmdDto(ccmc122sICInDto).build();
        Csnh626dToCcmc122sInCmdPyld ccmc122sEIInPyld = Csnh626dToCcmc122sInCmdPyld.builder().custInfoCurnTrtInCmdDto(ccmc122sEIInDto).build();
        Csnh626dToCcmc122sInCmdPyld ccmc122sIAInPyld = Csnh626dToCcmc122sInCmdPyld.builder().custInfoCurnTrtInCmdDto(ccmc122sIAInDto).build();
        Csnh626dToCcmc122sInCmdPyld ccmc122sINInPyld = Csnh626dToCcmc122sInCmdPyld.builder().custInfoCurnTrtInCmdDto(ccmc122sINInDto).build();
        Csnh626dToCcmc122sInCmdPyld ccmc122sETInPyld = Csnh626dToCcmc122sInCmdPyld.builder().custInfoCurnTrtInCmdDto(ccmc122sETInDto).build();
        
        if ( ccmc122sPPInDto != null && ccmc122sPPInDto.size() > 0 ) {
            try {
                KafkaSendInDto kafkaSendInDto = kafkaProducerCntr.getKafkaSendInDto(
                        KafkaServiceConstants.KAFKA_PPD_CUST_CURN_TRT
                        , this.getClass().getSimpleName()
                        , JsonUtil.toJson(ccmc122sPPInPyld));
                
                kafkaProducerCntr.send(kafkaSendInDto);
            } catch (Exception e) {
                throw new OrderException("MCSNI1051", List.of("고객정보 현행화 처리(ccmc122s) kafka 호출 처리 중 오류!!"));
            }
        }
        if ( ccmc122sICInDto != null && ccmc122sICInDto.size() > 0 ) {
            try {
                KafkaSendInDto kafkaSendInDto = kafkaProducerCntr.getKafkaSendInDto(
                        KafkaServiceConstants.KAFKA_ICD_CUST_CURN_TRT
                        , this.getClass().getSimpleName()
                        , JsonUtil.toJson(ccmc122sICInPyld));
                
                kafkaProducerCntr.send(kafkaSendInDto);
            } catch (Exception e) {
                throw new OrderException("MCSNI1051", List.of("고객정보 현행화 처리(ccmc122s) kafka 호출 처리 중 오류!!"));
            }
        }
        if ( ccmc122sEIInDto != null && ccmc122sEIInDto.size() > 0 ) {
            try {
                KafkaSendInDto kafkaSendInDto = kafkaProducerCntr.getKafkaSendInDto(
                        KafkaServiceConstants.KAFKA_EID_CUST_CURN_TRT
                        , this.getClass().getSimpleName()
                        , JsonUtil.toJson(ccmc122sEIInPyld));
                
                kafkaProducerCntr.send(kafkaSendInDto);
            } catch (Exception e) {
                throw new OrderException("MCSNI1051", List.of("고객정보 현행화 처리(ccmc122s) kafka 호출 처리 중 오류!!"));
            }
        }
        if ( ccmc122sIAInDto != null && ccmc122sIAInDto.size() > 0 ) {
            try {
                KafkaSendInDto kafkaSendInDto = kafkaProducerCntr.getKafkaSendInDto(
                        KafkaServiceConstants.KAFKA_IAD_CUST_CURN_TRT
                        , this.getClass().getSimpleName()
                        , JsonUtil.toJson(ccmc122sIAInPyld));
                
                kafkaProducerCntr.send(kafkaSendInDto);
            } catch (Exception e) {
                throw new OrderException("MCSNI1051", List.of("고객정보 현행화 처리(ccmc122s) kafka 호출 처리 중 오류!!"));
            }
        }
        if ( ccmc122sINInDto != null && ccmc122sINInDto.size() > 0 ) {
            try {
                KafkaSendInDto kafkaSendInDto = kafkaProducerCntr.getKafkaSendInDto(
                        KafkaServiceConstants.KAFKA_IND_CUST_CURN_TRT
                        , this.getClass().getSimpleName()
                        , JsonUtil.toJson(ccmc122sINInPyld));
                
                kafkaProducerCntr.send(kafkaSendInDto);
            } catch (Exception e) {
                throw new OrderException("MCSNI1051", List.of("고객정보 현행화 처리(ccmc122s) kafka 호출 처리 중 오류!!"));
            }
        }
        if ( ccmc122sETInDto != null && ccmc122sETInDto.size() > 0 ) {
            try {
                KafkaSendInDto kafkaSendInDto = kafkaProducerCntr.getKafkaSendInDto(
                        KafkaServiceConstants.KAFKA_ETD_CUST_CURN_TRT
                        , this.getClass().getSimpleName()
                        , JsonUtil.toJson(ccmc122sETInPyld));
                
                kafkaProducerCntr.send(kafkaSendInDto);
            } catch (Exception e) {
                throw new OrderException("MCSNI1051", List.of("고객정보 현행화 처리(ccmc122s) kafka 호출 처리 중 오류!!"));
            }
        }
    }

}
