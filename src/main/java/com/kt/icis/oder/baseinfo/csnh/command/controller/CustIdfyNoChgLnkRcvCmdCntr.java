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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.kt.icis.cmmnfrwk.utils.JsonUtil;
import com.kt.icis.cmmnfrwk.utils.LogUtil;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciaCustChangInfoCsnh624dDto;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciacustintegCsnh624dDto;
import com.kt.icis.oder.baseinfo.common.service.BtCciaCustChangInfoSvc;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.Csnh624dToCcmc123sInCmdPyld;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.WcCwkociasatarginfo2InCmdPyld;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.dto.Csnh624dToCcmc123sInCmdDto;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.dto.WcCwkociasatarginfo2InCmdDto;
import com.kt.icis.oder.common.exception.OrderException;
import com.kt.icis.oder.common.kafka.constants.KafkaServiceConstants;
import com.kt.icis.oder.common.kafka.controller.KafkaProducerCntr;
import com.kt.icis.oder.common.kafka.dto.KafkaSendInDto;

import lombok.RequiredArgsConstructor;

/**
 * @Class Name : Csnh624dCntr
 * @Class 설명 : 고객개명/식별번호변경 연동
 * @작성일 : 2023. 9. 11
 * @작성자 : 미소시스템 / 91343659 / 심은섭
 */
@Component
@RequiredArgsConstructor
public class CustIdfyNoChgLnkRcvCmdCntr {

    //private final BtCwkociasatarginfoSvc btCwkociasatarginfoSvc;

    //private final Csnh624dToCcmc123sCmdClnt csnh624dToCcmc123sClnt;

    private final BtCciaCustChangInfoSvc btCciaCustChangInfoSvc;

    private final KafkaProducerCntr kafkaProducerCntr;

    //private final WcCwkociasatarginfoClnt wcCwkociasatarginfoClnt;

    private static final String GSZ_PGM_NAME = "csnh624d";
    String szLogMsg = "";

    /**
     * @Method Name : csnh624d
     * @Method 설명 : 고객개명/식별번호변경 연동
     * @작성일 : 2023. 9. 11
     * @작성자 : 미소시스템 / 91343659 / 심은섭
     * @param
     * @return
     */
//    @Scheduled(fixedDelayString = "${oder.batch.cust-idfy-no-chg-lnk-rcv.delay-time}")
    public void custIdfyNoChgLnkRcv() {
        LogUtil.info("Batch(custIdfyNoChgLnkRcv) start : " + ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ISO_DATE_TIME));

        String errMsg = "";
        String procResult = "";

        try {
            /**
             *  desc : CIA 변동 데이터 조회
             */
            //  BI_CCIA_CUST_CHANG_INFO 테이블 조회
            List<BtCciacustintegCsnh624dDto> readerDtoList = btCciaCustChangInfoSvc.getInfCDeclareCursorCiaWork();

            if(readerDtoList != null && !readerDtoList.isEmpty()) {
                LogUtil.info("readerDtoList : =================={}============", readerDtoList.toString());
                for(BtCciacustintegCsnh624dDto btCciacustintegCsnh624dDto : readerDtoList) {
                    Map<String,String> resultMap = new HashMap<String,String>();
                    try {

                        if("0".equals(btCciacustintegCsnh624dDto.getExist())) {
                            procResult = "F";
                            errMsg = "CCSTBASICINFO에 존재하지 않는 고객ID 입니다.";

                        }else {

                            try {
                                /* 33.21.2  CCT_Msg_SetInputRow (ccmc123s)    */
                                //  ccmc123sf 서비스 호출
                                resultMap = this.infCallSvcProc(btCciacustintegCsnh624dDto);
                                if(resultMap != null) {
                                    LogUtil.info("Exit reson {}: ccmc123sClnt 변동 데이터 조회 완료", resultMap.toString());

                                    procResult = resultMap.get("resultCd").toString();
                                    errMsg = resultMap.get("resultMsg").toString();

                                    if("P".equals(procResult)) {
                                        this.insertInfCInsertSaWorkForInfo(btCciacustintegCsnh624dDto);
                                    }
                                }else {
                                    procResult = "T";
                                    errMsg = "호출 비정상 [ccmc123s]";
                                }

                            }catch(Exception e) {
                                LogUtil.error("Exit reson {}: ICIS(CCIA_CUST_DIVD)에 처리 결과 값을 갱신 FAIL", e.getMessage());
                                procResult = "T";
                                errMsg = "호출 비정상 [ccmc123s]";
                            }
                        }

                        BtCciaCustChangInfoCsnh624dDto btCciaCustChangInfoCsnh624dDto = new BtCciaCustChangInfoCsnh624dDto();
                        //ICIS(CCIA_CUST_INTEG)에 처리 결과 값을 갱신
                        btCciaCustChangInfoCsnh624dDto.setProcResult(procResult);
                        btCciaCustChangInfoCsnh624dDto.setErrMsg(errMsg);
                        btCciaCustChangInfoCsnh624dDto.setCustId(btCciacustintegCsnh624dDto.getCustId());
                        btCciaCustChangInfoCsnh624dDto.setOccurDate(btCciacustintegCsnh624dDto.getOccurDate());

                        btCciaCustChangInfoSvc.infCUpdateCiaWork(btCciaCustChangInfoCsnh624dDto);


                    }catch(Exception e) {
                        LogUtil.error("Exit reson : custIdfyNoChgLnkRcv fail");
                        break;
                    }
                }
            }

            LogUtil.info("Batch(custIdfyNoChgLnkRcv) process count : " + readerDtoList.size());
        }catch (Exception e) {

            LogUtil.error("Exit reson : CIA 변동 데이터 조회");
            LogUtil.error("Exit reson : custIdfyNoChgLnkRcv fail");

        }

        LogUtil.info("Batch(custIdfyNoChgLnkRcv) end : " + ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ISO_DATE_TIME));
    }

    /**
     * @param btCciacustintegCsnh624dDto
     */
    private void insertInfCInsertSaWorkForInfo(BtCciacustintegCsnh624dDto btCciacustintegCsnh624dDto) {
        //ICIS(CCIA_CUST_DIVD)에 처리 결과 값을 갱신
        WcCwkociasatarginfo2InCmdDto btCwkociasatarginfoCsnh624dDto = new WcCwkociasatarginfo2InCmdDto();
        btCwkociasatarginfoCsnh624dDto.setCustId(btCciacustintegCsnh624dDto.getCustId());
        btCwkociasatarginfoCsnh624dDto.setOccurDate(btCciacustintegCsnh624dDto.getOccurDate());
        btCwkociasatarginfoCsnh624dDto.setSvcType(GSZ_PGM_NAME);

        WcCwkociasatarginfo2InCmdPyld wcCwkociasatarginfo2InCmdPyld = WcCwkociasatarginfo2InCmdPyld.builder().cwkociasatarginfo2InCmdDto(btCwkociasatarginfoCsnh624dDto).build();

        try{
            //INSERT WC_CWKO_CIA_SA_TARG_INFO
            KafkaSendInDto kafkaSendInDto = kafkaProducerCntr.getKafkaSendInDto(
                    KafkaServiceConstants.KAFKA_WC_CUST_CWKO_REGCWKOCIASATARGINFO
                    , this.getClass().getSimpleName()
                    , JsonUtil.toJson(wcCwkociasatarginfo2InCmdPyld));

            kafkaProducerCntr.send(kafkaSendInDto);
        }catch (Exception e){
            throw new OrderException("MCSNI1051", List.of("고객정보현행화처리(INSERT)-CSNH624D kafka 호출 처리 중 오류!!"));
        }
    }


    private Map<String,String> infCallSvcProc(BtCciacustintegCsnh624dDto dto) {
        Map<String,String> resultMap = new HashMap<String,String>();

        Csnh624dToCcmc123sInCmdDto ccmc123sInDto = new Csnh624dToCcmc123sInCmdDto();
        ccmc123sInDto.setCustProcType("C");
        ccmc123sInDto.setCustId(dto.getCustId());
        ccmc123sInDto.setOccurDate(dto.getOccurDate());
        ccmc123sInDto.setRegerEmpNo(GSZ_PGM_NAME);
        ccmc123sInDto.setRegerEmpName(GSZ_PGM_NAME);
        ccmc123sInDto.setRegOfcCd("");
        Csnh624dToCcmc123sInCmdPyld ccmc123sInPyld = Csnh624dToCcmc123sInCmdPyld.builder().custInfoCurnPrecdTrtInCmdDto(ccmc123sInDto).build();

        try{
            //분산으로 호출할 필요 없는 상태 - ccmc123sf호출
            KafkaSendInDto kafkaSendInDto = kafkaProducerCntr.getKafkaSendInDto(
                    KafkaServiceConstants.KAFKA_PPD_CUST_CURN_PRECDTRT
                    , this.getClass().getSimpleName()
                    , JsonUtil.toJson(ccmc123sInPyld));

            kafkaProducerCntr.send(kafkaSendInDto);
        }catch (Exception e){
            throw new OrderException("MCSNI1051", List.of("고객정보 현행화 선행처리 kafka 호출 처리 중 오류!!"));
        }

        resultMap.put("resultCd", "P");
        resultMap.put("resultMsg", "고객정보 현행화 선행처리 Kafka 호출 성공");

        return resultMap;

    }

}
