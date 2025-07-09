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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.kt.icis.cmmnfrwk.utils.JsonUtil;
import com.kt.icis.cmmnfrwk.utils.LogUtil;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciacustintegCsnh625dDto;
import com.kt.icis.oder.baseinfo.common.service.BtCciacustintegSvc;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.Ccmc123sInCmdPyld;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.WcCwkociasatarginfo2InCmdPyld;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.dto.Ccmc123sInCmdDto;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.dto.WcCwkociasatarginfo2InCmdDto;
import com.kt.icis.oder.common.exception.OrderException;
import com.kt.icis.oder.common.kafka.constants.KafkaServiceConstants;
import com.kt.icis.oder.common.kafka.controller.KafkaProducerCntr;
import com.kt.icis.oder.common.kafka.dto.KafkaSendInDto;

import lombok.RequiredArgsConstructor;

/**
 * @Class Name : Csnh625dCntr
 * @Class 설명 : CIA에서 요청한 고객통합을 수행하는 Demon으로
 *                고객의 대상 서비스계약을 발췌하여 작업테이블에 편성한다.
 * @작성일 : 2023. 10. 12
 * @작성자 : 미소시스템 / 91343659 / 심은섭
 */
@Component
@RequiredArgsConstructor
public class CustItgLnkRcvCmdCntr {

    //private final BtCwkociasatarginfoSvc btCwkociasatarginfoSvc;

    private final BtCciacustintegSvc btCciacustintegSvc;

    //private final Ccmc123sCmdClnt ccmc123sClnt;

    private final KafkaProducerCntr kafkaProducerCntr;

    private static final String GSZ_PGM_NAME = "csnh625d";
    String szLogMsg = "";

    /**
     * @Method Name : csnh625d
     * @Method 설명 : 고객통합 연동
     * @작성일 : 2023. 10. 12
     * @작성자 : 미소시스템 / 91343659 / 심은섭
     * @param
     * @return
     */
//    @Scheduled(fixedDelayString = "${oder.batch.cust-itg-lnk-rcv.delay-time}")
    public void custItgLnkRcv() {
        LogUtil.info("[starttime/custItgLnkRcv]: {} ", new Timestamp(System.currentTimeMillis()));
        String errMsg = "";
        String procResult = "";
        int exitCnt = 0;
        boolean result = false;

        try {
            /**
             *  desc : CIA 변동 데이터 조회
             */
            List<BtCciacustintegCsnh625dDto> readerDtoList = btCciacustintegSvc.getInfDeclareCursorCiaWork();

            if(readerDtoList != null && !readerDtoList.isEmpty()) {
            	// 2024.04.18 ICISTR-96574 로그점검 조치를 위한 주석처리
            	// LogUtil.info("readerDtoList : =================={}============", readerDtoList.toString());
                for(BtCciacustintegCsnh625dDto btCciacustintegCsnh625dDto : readerDtoList) {
                    Map<String,String> resultMap = new HashMap<String,String>();

                    try {
                        //진행중인 오더 존재 여부 확인
                        exitCnt = btCciacustintegSvc.getInfIsPendingCust(btCciacustintegCsnh625dDto);

                        if(exitCnt != 0) {
                            //고객ID가 다른 변동에서 통합될 고객ID로 사용되는 경우 편성하지 않도록 한다
                            LogUtil.info("infIsPendingCust.......Exist {}", exitCnt);
                            continue;
                        }

                        if("0".equals(btCciacustintegCsnh625dDto.getExist())) {
                            procResult = "F";
                            errMsg = "CCSTBASICINFO에 존재하지 않는 고객ID 입니다.";

                        }else {

                            try {
                                /* 33.21.2  CCT_Msg_SetInputRow (ccmc123s)    */
                                //  ccmc123sf 서비스 호출
                                resultMap = this.infCallSvcProc(btCciacustintegCsnh625dDto);
                                LogUtil.info("Exit reson : ccmc123s sucess{}", result);

                                procResult = resultMap.get("resultCd").toString();
                                errMsg = resultMap.get("resultMsg").toString();

                                if("P".equals(procResult)) {
                                    this.insertInfInsertSaWorkForInfo(btCciacustintegCsnh625dDto);
                                }

                            }catch(Exception e) {
                                procResult = "T";
                                errMsg = "호출 비정상 [ccmc123s]";
                            }
                        }

                        //ICIS(CCIA_CUST_INTEG)에 처리 결과 값을 갱신
                        btCciacustintegCsnh625dDto.setProcResult(procResult);
                        btCciacustintegCsnh625dDto.setErrMsg(errMsg);
                        //2024.04.18 ICISTR-96574 로그점검 조치를 위한 주석처리
                        //LogUtil.info("infUpdateCiaWork : call{}", btCciacustintegCsnh625dDto.toString());

                        btCciacustintegSvc.infUpdateCiaWork(btCciacustintegCsnh625dDto);
                        LogUtil.info("infUpdateCiaWork : call SUCESS");

                    }catch(Exception e) {
                        LogUtil.info("Exit reson : custItgLnkRcv fail");
                        break;
                    }
                }
            }
        }catch (Exception e) {

            LogUtil.info("Exit reson : CIA 변동 데이터 조회");
            LogUtil.info("Exit reson : custItgLnkRcv fail");

        }
        LogUtil.info("[endtime/custItgLnkRcv]: {} ", new Timestamp(System.currentTimeMillis()));

    }

    /**
     * @param btCciacustintegCsnh625dDto
     */
    private void insertInfInsertSaWorkForInfo(BtCciacustintegCsnh625dDto btCciacustintegCsnh625dDto) {
    	/*
        //ICIS(CCIA_CUST_DIVD)에 처리 결과 값을 갱신
        BtCwkociasatarginfoCsnh625dDto btCwkociasatarginfoCsnh625dDto = new BtCwkociasatarginfoCsnh625dDto();
        btCwkociasatarginfoCsnh625dDto.setCustId(btCciacustintegCsnh625dDto.getCustId());
        btCwkociasatarginfoCsnh625dDto.setOccurDate(btCciacustintegCsnh625dDto.getOccurDate());

        List<BtCwkoCiaSaTargInfoEntt> btCwkoCiaSaTargInfoEnttList = btCwkociasatarginfoSvc.selectInfInsertSaWorkForInfo(btCwkociasatarginfoCsnh625dDto);
        for (BtCwkoCiaSaTargInfoEntt entt : btCwkoCiaSaTargInfoEnttList) {
            entt.setNew(true);
            btCwkociasatarginfoSvc.save(entt);
        }
        //        btCwkociasatarginfoSvc.insertInfInsertSaWorkForInfo(btCwkoCiaSaTargInfoEnttList);
    	 */
                
        WcCwkociasatarginfo2InCmdDto wcCwkociasatarginfo2InCmdDto = new WcCwkociasatarginfo2InCmdDto();
        wcCwkociasatarginfo2InCmdDto.setCustId(btCciacustintegCsnh625dDto.getCustId());
        wcCwkociasatarginfo2InCmdDto.setOccurDate(btCciacustintegCsnh625dDto.getOccurDate());
        wcCwkociasatarginfo2InCmdDto.setSvcType("CSNH625D");
        WcCwkociasatarginfo2InCmdPyld wcCwkociasatarginfo2InCmdPyld = WcCwkociasatarginfo2InCmdPyld.builder().cwkociasatarginfo2InCmdDto(wcCwkociasatarginfo2InCmdDto).build();
        
        try{
            //분산으로 호출할 필요 없는 상태
            KafkaSendInDto kafkaSendInDto = kafkaProducerCntr.getKafkaSendInDto(
                    KafkaServiceConstants.KAFKA_WC_CUST_CWKO_REGCWKOCIASATARGINFO
                    , this.getClass().getSimpleName()
                    , JsonUtil.toJson(wcCwkociasatarginfo2InCmdPyld));
            
            kafkaProducerCntr.send(kafkaSendInDto);
        }catch (Exception e){
            throw new OrderException("MCSNI1051", List.of("고객정보현행화처리(INSERT)-CSNH624D, CSNH625D kafka 호출 처리 중 오류!!"));
        }
    }

    private Map<String,String> infCallSvcProc(BtCciacustintegCsnh625dDto dto) {
        Map<String,String> resultMap = new HashMap<String,String>();

        Ccmc123sInCmdDto ccmc123sInDto = new Ccmc123sInCmdDto();
        ccmc123sInDto.setCustProcType("I");
        ccmc123sInDto.setCustId(dto.getCustId());
        ccmc123sInDto.setOccurDate(dto.getOccurDate());
        ccmc123sInDto.setRegerEmpNo(GSZ_PGM_NAME);
        ccmc123sInDto.setRegerEmpName(GSZ_PGM_NAME);
        ccmc123sInDto.setRegOfcCd("");
        Ccmc123sInCmdPyld ccmc123sInPyld = Ccmc123sInCmdPyld.builder().custInfoCurnPrecdTrtInCmdDto(ccmc123sInDto).build();

        try{
            //분산으로 호출할 필요 없는 상태
            KafkaSendInDto kafkaSendInDto = kafkaProducerCntr.getKafkaSendInDto(
                    KafkaServiceConstants.KAFKA_PPD_CUST_CURN_PRECDTRT
                    , this.getClass().getSimpleName()
                    , JsonUtil.toJson(ccmc123sInPyld));

            kafkaProducerCntr.send(kafkaSendInDto);
        }catch (Exception e){
            throw new OrderException("MCSNI1051", List.of("고객정보 현행화 선행처리(ccmc123s) kafka 호출 처리 중 오류!!"));
        }

        resultMap.put("resultCd", "P");
        resultMap.put("resultMsg", "고객정보 현행화 선행처리 Kafka 호출 성공");

        return resultMap;

    }

}
