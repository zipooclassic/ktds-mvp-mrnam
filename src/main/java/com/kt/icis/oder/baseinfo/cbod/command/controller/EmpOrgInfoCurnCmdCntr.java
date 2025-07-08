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

package com.kt.icis.oder.baseinfo.cbod.command.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import com.kt.icis.cmmnfrwk.cris.domain.CrisRqstInfo;
import com.kt.icis.cmmnfrwk.cris.payload.CrisRespOutDs;
import com.kt.icis.cmmnfrwk.cris.payload.CrisRqstInDs;
import com.kt.icis.cmmnfrwk.utils.CrisUtil;
import com.kt.icis.cmmnfrwk.utils.JsonUtil;
import com.kt.icis.cmmnfrwk.utils.LogUtil;
import com.kt.icis.oder.baseinfo.common.command.payload.in.BiCwkoerpempinfoInCmdPyld;
import com.kt.icis.oder.baseinfo.common.command.payload.in.BiCwkoorgmasterInCmdPyld;
import com.kt.icis.oder.baseinfo.common.command.payload.in.dto.BiCwkoerpempinfoInCmdDto;
import com.kt.icis.oder.baseinfo.common.command.payload.in.dto.BiCwkoorgmasterInCmdDto;
import com.kt.icis.oder.baseinfo.common.repository.BtCwkoerpempinfoRepo;
import com.kt.icis.oder.baseinfo.common.repository.BtCwkoorgmasterRepo;
import com.kt.icis.oder.baseinfo.common.repository.BtMsyserpempinfoRepo;
import com.kt.icis.oder.baseinfo.common.repository.BtMsysorgmasterRepo;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCwkoerpempinfoDto;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCwkoorgmasterDto;
import com.kt.icis.oder.baseinfo.common.repository.entity.BtMsyserpempinfoEntt;
import com.kt.icis.oder.baseinfo.common.repository.entity.BtMsysorgmasterEntt;
import com.kt.icis.oder.common.exception.OrderException;
import com.kt.icis.oder.common.kafka.constants.KafkaServiceConstants;
import com.kt.icis.oder.common.kafka.controller.KafkaProducerCntr;
import com.kt.icis.oder.common.kafka.dto.KafkaSendInDto;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmpOrgInfoCurnCmdCntr {

    private final BtCwkoerpempinfoRepo btCwkoerpempinfoRepo;
    private final BtMsyserpempinfoRepo btMsyserpempinfoRepo;
    private final BtCwkoorgmasterRepo  btCwkoorgmasterRepo;
    private final BtMsysorgmasterRepo  btMsysorgmasterRepo;
    
    private final KafkaProducerCntr    kafkaProducerCntr;

    private String pgmName = "cbod904d";

    /**
     * 사원 | 조직정보 현행화 데몬
     * @tuxedo CBOD904D
     */
    public void empOrgInfoCurn() {
    	LogUtil.info("Batch(empOrgInfoCurn) start : " + ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ISO_DATE_TIME));

        /* 1. 사원정보 변동분 반영 */
        UMsysErpEmpInfo(pgmName);

        /* 2. 조직정보 변동분 반영 */
        UMsysOrgMaster(pgmName);

        LogUtil.info("Batch(empOrgInfoCurn) end : " + ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ISO_DATE_TIME));
    }

    private void UMsysErpEmpInfo(String pgmName) {
        List<BtCwkoerpempinfoDto> dtoList = btCwkoerpempinfoRepo.findCwkoerpempinfoByWoflagAndEmpno(pgmName);
        LogUtil.info("사원정보 변동분 반영 시작");
        if (dtoList.size()==0){
            return;
        }
        
        List<BiCwkoerpempinfoInCmdDto> inDtoList = new ArrayList<BiCwkoerpempinfoInCmdDto>();
        
        for (BtCwkoerpempinfoDto dto : dtoList) {
            dto.setWoFlag("Y");
            if (dto == null || dto.getEmpNo() == null || "".equals(dto.getEmpNo())) {
                continue;
            }

            Date date = new Date();

            String socialNo = "";

            try {
                socialNo = dto.getSocialNo().substring(8,13).trim();
                if ("999999".equals(socialNo)) {
                    socialNo = dto.getSocialNo().substring(0,7).trim();
                } else {
                    CrisRqstInfo crisRqstInfo = new CrisRqstInfo();
                    crisRqstInfo.setReqData(dto.getSocialNo());
                    crisRqstInfo.setReqAdd1("AUTH");
                    //        crisRqstInfo.setEndUsrId("91305459");

                    CrisRqstInDs crisRqstInDs = new CrisRqstInDs();
                    crisRqstInDs.setCrisRqstInfo(crisRqstInfo);
                    //주민번호 가상번호 가져오기
                    CrisRespOutDs crisRespOutDs = CrisUtil.getIssueSocialNo(crisRqstInDs);
                    socialNo = crisRespOutDs.getCrisResp().getResInfo().get(0).getResData1();
                }
            }catch(NullPointerException e) {
                LogUtil.info("Cris 호출 오류 socialId is null [{}]",dto.getSocialNo());
            }catch (IndexOutOfBoundsException e){
                //                socialNo = spIcisGetVcRef(dto.getSocialNo(),"1","","","");
                LogUtil.info("Cris 호출 오류 socialId 자리수 확인[{}]",dto.getSocialNo());
            }

            BtMsyserpempinfoEntt btMsyserpempinfoEntt = BtMsyserpempinfoEntt.builder()
                    .empNo(dto.getEmpNo())
                    .empNoOld(dto.getEmpNoOld())
                    .empNm(dto.getEmpNm())
                    .statusCd(dto.getStatusCd())
                    .deptCd(dto.getDeptCd())

                    .noFormCd(dto.getNoFormCd())
                    .norTelNo(dto.getNorTelNo())
                    .mailid(dto.getMailid())
                    .handphoneNo(dto.getHandphoneNo())
                    .jobCd(dto.getJobCd())

                    .content(dto.getContent())
                    .replDt(dto.getReplDt())
                    .persg(dto.getPersg())
                    .zbizunitCd(dto.getZbizunitCd())
                    .zmanagerNo(dto.getZmanagerNo())

                    .icisEmpNo(dto.getEmpNoOld()==null?dto.getEmpNo():dto.getEmpNoOld())
                    .regDate(new Timestamp(date.getTime()))
                    .chngDate(new Timestamp(date.getTime()))
                    .eaiSeq(dto.getEaiSeq()==null?new BigDecimal(0):new BigDecimal(dto.getEaiSeq()))
                    .socialNo(socialNo)

                    .build();
            if ("I".equals(dto.getEaiOp())) {
                btMsyserpempinfoEntt.setNew(true);
                try {
                    btMsyserpempinfoRepo.save(btMsyserpempinfoEntt);
                }catch(Exception e) {
                    LogUtil.info("INSERT MSYSERPEMPINFO 오류발생");
                    dto.setWoFlag("N");
                    dto.setWoErrMsg("INSERT Error");
                    LogUtil.info("INSERT Error");
                } finally {
                    try {
                        inDtoList.add( updCwkoEmpCbod904d(dto) );
                    }catch(Exception e) {
                        LogUtil.info("INSERT Error {}");
                    }
                }
            }else if ("D".equals(dto.getEaiOp())) {
                try {
                    btMsyserpempinfoRepo.deleteById(dto.getEmpNo());
                }catch(Exception e) {
                    e.printStackTrace();
                    dto.setWoFlag("N");
                    dto.setWoErrMsg("Delete Error");
                    LogUtil.info("Delete Error {}", e);
                } finally {
                    try {
                        inDtoList.add( updCwkoEmpCbod904d(dto) );
                    }catch(Exception e) {
                        LogUtil.info("Delete Error {}");
                    }
                }
            }else if ("U".equals(dto.getEaiOp())) {
                try {
                    btMsyserpempinfoRepo.save(btMsyserpempinfoEntt);
                }catch(Exception e) {
                    e.printStackTrace();
                    dto.setWoFlag("N");
                    dto.setWoErrMsg("Update Error");
                    LogUtil.info("Update Error {}", e);
                } finally {
                    try {
                        inDtoList.add( updCwkoEmpCbod904d(dto) );
                    }catch(Exception e) {
                        LogUtil.info("Update Error {}");
                    }
                }
            }else {
                dto.setWoFlag("N");
                dto.setWoErrMsg("EAI_OP 값 오류["+dto.getEaiOp()+"]");
                try {
                    inDtoList.add( updCwkoEmpCbod904d(dto) );
                }catch(Exception e) {
                    LogUtil.info("empOrgInfoCurn err");
                }
            }
//            try {
//                updCwkoEmpCbod904d(dto);
//            }catch(Exception ee) {
//                throw new OrderException("empOrgInfoCurn err");
//            }
        }
        
        sendUpdCwkoEmpCbod904d(inDtoList);
        
        LogUtil.info("사원정보 변동분 반영 끝");
    }


    private void UMsysOrgMaster(String pgmName) {
        List<BtCwkoorgmasterDto> dtoList = btCwkoorgmasterRepo.findCwkoorgmasterByWoflagAndEmpno(pgmName);

        if (dtoList.size()==0){
            return;
        }
        
        List<BiCwkoorgmasterInCmdDto> inDtoList = new ArrayList<BiCwkoorgmasterInCmdDto>();
        
        for (BtCwkoorgmasterDto dto:dtoList) {
            dto.setWoFlag("Y");
            if (ObjectUtils.isEmpty(dto) || StringUtils.isEmpty(dto.getRowId())) {
                continue;
            }
            Date date = new Date();
            BtMsysorgmasterEntt btMsysorgmasterEntt = BtMsysorgmasterEntt.builder()
                    .orgcode(dto.getOrgcode())
                    .parentorgcode(dto.getParentorgcode())
                    .orgname(dto.getOrgname())
                    .orgengname(dto.getOrgengname())
                    .divisionwork(dto.getDivisionwork())

                    .zipid(dto.getZipid())
                    .postalcd(dto.getPostalcd())
                    .addr1(dto.getAddr1())
                    .addr2(dto.getAddr2())
                    .businessgb(dto.getBusinessgb())

                    .orgtype(dto.getOrgtype())
                    .ispayorg(dto.getIspayorg())
                    .telno(dto.getTelno())
                    .faxno(dto.getFaxno())
                    .isprofitcenter(dto.getIsprofitcenter())

                    .profitcenter(dto.getProfitcenter())
                    .iscostcenter(dto.getIscostcenter())
                    .costcenter(dto.getCostcenter())
                    .isevaluate(dto.getIsevaluate())
                    .headcode(dto.getHeadcode())

                    .orglevel(dto.getOrglevel())
                    .orgorder(dto.getOrgorder())
                    .formorder(dto.getFormorder())
                    .isleaf(dto.getIsleaf())
                    .creationdtime(new Timestamp(dto.getCreationdtime().getTime()))

                    .updatedtime(new Timestamp(dto.getUpdatedtime().getTime()))
                    .active(dto.getActive())
                    .capempno(dto.getCapempno())
                    .orggrade(dto.getOrggrade())
                    .lastupdator(dto.getLastupdator())

                    .orgsymname(dto.getOrgsymname())
                    .regDate(new Timestamp(date.getTime()))
                    .chngDate(new Timestamp(date.getTime()))
                    .eaiSeq(dto.getEaiSeq()==null?null:new BigDecimal(dto.getEaiSeq()))

                    .build();

            if ("I".equals(dto.getEaiOp())) {
                btMsysorgmasterEntt.setNew(true);
                try {
                    btMsysorgmasterRepo.save(btMsysorgmasterEntt);
                }catch(Exception e) {
                    dto.setWoFlag("N");
                    dto.setWoErrMsg("INSERT Error");
                    LogUtil.info("INSERT Error");
                } finally {
                    try {
                        inDtoList.add( updCwkoOrgCbod904d(dto) );
                    }catch(Exception e) {
                        LogUtil.info("Update Error {}");
                    }
                }
            }else if ("D".equals(dto.getEaiOp())) {
                try {
                    btMsysorgmasterRepo.deleteById(dto.getOrgcode());
                }catch(Exception e) {
                    dto.setWoFlag("N");
                    dto.setWoErrMsg("Delete Error");
                    LogUtil.info("Delete Error");
                } finally {
                    try {
                        inDtoList.add( updCwkoOrgCbod904d(dto) );
                    }catch(Exception e) {
                        LogUtil.info("Update Error {}");
                    }
                }
            }else if ("U".equals(dto.getEaiOp())) {
                try {
                    btMsysorgmasterRepo.save(btMsysorgmasterEntt);
                }catch(Exception e) {
                    dto.setWoFlag("N");
                    dto.setWoErrMsg("Update Error");
                    LogUtil.info("Update Error");
                } finally {
                    try {
                        inDtoList.add( updCwkoOrgCbod904d(dto) );
                    }catch(Exception e) {
                        LogUtil.info("Update Error {}");
                    }
                }
            }else {
                dto.setWoFlag("N");
                dto.setWoErrMsg("EAI_OP 값 오류["+dto.getEaiOp()+"]");
                try {
                    inDtoList.add( updCwkoOrgCbod904d(dto) );
                }catch(Exception ee) {
                    throw new OrderException("empOrgInfoCurn err");
                }
            }
//            try {
//                updCwkoOrgCbod904d(dto);
//            }catch(Exception ee) {
//                throw new OrderException("empOrgInfoCurn err");
//            }
        }
        
        sendUpdCwkoOrgCbod904d(inDtoList);
        
    }

    private BiCwkoerpempinfoInCmdDto updCwkoEmpCbod904d(BtCwkoerpempinfoDto dto) {
        BiCwkoerpempinfoInCmdDto inDto = new BiCwkoerpempinfoInCmdDto();
        inDto.setRowId(dto.getRowId());
        inDto.setSocialNo(dto.getSocialNo());
        inDto.setWoErrMsg(dto.getWoErrMsg());
        inDto.setWoFlag(dto.getWoFlag());
        
        return inDto;
    }

    private void sendUpdCwkoEmpCbod904d(List<BiCwkoerpempinfoInCmdDto> inDtoList) {
        LogUtil.info("사원조직정보현행화(cbod904d) Kafka 호출 작업 시작");
        
        BiCwkoerpempinfoInCmdPyld inPyld = new BiCwkoerpempinfoInCmdPyld();
        inPyld.setCwkoerpempinfoInCmdDto(inDtoList);
        
        try {
            KafkaSendInDto kafkaSendInDto = kafkaProducerCntr.getKafkaSendInDto(
                    KafkaServiceConstants.KAFKA_WC_CUST_REGCWKOERPEMPINFOCHNG
                    , this.getClass().getSimpleName()
                    , JsonUtil.toJson(inPyld));
            
            kafkaProducerCntr.send(kafkaSendInDto);
        } catch (Exception e) {
            List<String> errParam = new ArrayList<String>();
            errParam.add("사원조직정보현행화(cbod904d) Kafka 호출");
            throw new OrderException("MCSNI1051", errParam);
        }
        
        LogUtil.info("사원조직정보현행화(cbod904d) Kafka 호출 작업 끝");
    }

    private BiCwkoorgmasterInCmdDto updCwkoOrgCbod904d(BtCwkoorgmasterDto dto) {
    	BiCwkoorgmasterInCmdDto inDto = new BiCwkoorgmasterInCmdDto();
    	inDto.setRowId(dto.getRowId());
        inDto.setWoErrMsg(dto.getWoErrMsg());
        inDto.setWoFlag(dto.getWoFlag());
        
        return inDto;
    }

    private void sendUpdCwkoOrgCbod904d(List<BiCwkoorgmasterInCmdDto> inDtoList) {
        LogUtil.info("조직정보변동정보수신(cbod904d) Kafka 호출 작업 시작");
        
        BiCwkoorgmasterInCmdPyld inPyld = new BiCwkoorgmasterInCmdPyld();
        inPyld.setCwkoorgmasterInCmdDto(inDtoList);
        
        try {
            KafkaSendInDto kafkaSendInDto = kafkaProducerCntr.getKafkaSendInDto(
                    KafkaServiceConstants.KAFKA_WC_CUST_REGCWKOORGMASTERCHNG
                    , this.getClass().getSimpleName()
                    , JsonUtil.toJson(inPyld));
            
            kafkaProducerCntr.send(kafkaSendInDto);
        } catch (Exception e) {
            List<String> errParam = new ArrayList<String>();
            errParam.add("조직정보변동정보수신(cbod904d) Kafka 호출");
            throw new OrderException("MCSNI1051", errParam);
        }
        
        LogUtil.info("조직정보변동정보수신(cbod904d) Kafka 호출 작업 끝");
    }

}
