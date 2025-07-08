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

import com.kt.icis.cmmnfrwk.utils.LogUtil;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciaCustDivdCsnh627dDto;
import com.kt.icis.oder.baseinfo.common.service.BtCciaCustDivdSvc;
import com.kt.icis.oder.baseinfo.common.service.BtCordordinfoSvc;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

/**
 * @Class Name : Csnh627dCntr
 * @Class 설명 : 고객분리연동시 진행중 오더 체크하여 재처리가능하게 변경
 * @작성일 : 2023. 10. 12
 * @작성자 : 미소시스템 / 91343659 / 심은섭
 */
@Component
@RequiredArgsConstructor
public class CustSpatnLnkAgntrCmdCntr {

    private final BtCciaCustDivdSvc btCciaCustDivdSvc;

    private final BtCordordinfoSvc btCordordinfoSvc;

    //private final BtCciaCustDivdRepo btCciaCustDivdRepo;

    String gszPgmName = "csnh627d";
    String szLogMsg = "";

    /**
     * @Method Name : csnh627d
     * @Method 설명 : 고객분리연동시 진행중 오더 체크하여 재처리가능하게 변경
     * @작성일 : 2023. 10. 12
     * @작성자 : 미소시스템 / 91343659 / 심은섭
     * @param
     * @return
     */
    public void custSpatnLnkAgntr() {
        LogUtil.info("[starttime/custSpatnLnkAgntr]: {} ", new Timestamp(System.currentTimeMillis()));
        try {
            /**
             *  Reader
             *  desc : 재처리 대상 조회
             */
            List<BtCciaCustDivdCsnh627dDto> readerDtoList = btCciaCustDivdSvc.infDeclareCursorIcisWork();
            String ordNo = "";

            if(readerDtoList != null && !readerDtoList.isEmpty()) {
                for(BtCciaCustDivdCsnh627dDto btCciaCustDivdCsnh627dDto : readerDtoList) {
                    //진행중인 오더 존재 여부 확인
                    try {
                        LogUtil.info("IpsProcResult : =================={}============", btCciaCustDivdCsnh627dDto.getIpsProcResult());
                        ordNo = btCordordinfoSvc.checkOnGoingOrdNo(btCciaCustDivdCsnh627dDto.getSaId());
                        LogUtil.info("ordNo : =================={}============", ordNo);
                    }catch(Exception e) {
                        LogUtil.info("IpsProcResult : =================={}============", btCciaCustDivdCsnh627dDto.getIpsProcResult());
                        //실패 정보 업데이트 이후 프로세스 종료
                        BtCciaCustDivdCsnh627dDto csnh627dWriterDto = new BtCciaCustDivdCsnh627dDto();

                        csnh627dWriterDto.setProcResult(btCciaCustDivdCsnh627dDto.getProcResult());
                        csnh627dWriterDto.setNewCustId(btCciaCustDivdCsnh627dDto.getNewCustId());
                        csnh627dWriterDto.setCustId(btCciaCustDivdCsnh627dDto.getCustId());
                        csnh627dWriterDto.setSaId(btCciaCustDivdCsnh627dDto.getSaId());
                        csnh627dWriterDto.setOccurDate(btCciaCustDivdCsnh627dDto.getOccurDate());

                        this.infUpdateIcisWork(csnh627dWriterDto);
                        LogUtil.info("Exit reson : infUpdateIcisWork fail");
                        break;
                    }

                    if(StringUtils.isNotEmpty(ordNo)) {
                        /* 진행중오더 존재 */
                        LogUtil.info("+++++++++++++++exist ordNo+++++++++++++++++");
                        BtCciaCustDivdCsnh627dDto csnh627dWriterDto = new BtCciaCustDivdCsnh627dDto();

                        csnh627dWriterDto.setProcResult(btCciaCustDivdCsnh627dDto.getProcResult());
                        csnh627dWriterDto.setNewCustId(btCciaCustDivdCsnh627dDto.getNewCustId());
                        csnh627dWriterDto.setCustId(btCciaCustDivdCsnh627dDto.getCustId());
                        csnh627dWriterDto.setSaId(btCciaCustDivdCsnh627dDto.getSaId());
                        csnh627dWriterDto.setOccurDate(btCciaCustDivdCsnh627dDto.getOccurDate());

                        this.infUpdateIcisWork(csnh627dWriterDto);
                    }else {
                        LogUtil.info("+++++++++++++++No have a ordNo+++++++++++++++++");
                        /* 진행중오더 해소 */
                        BtCciaCustDivdCsnh627dDto csnh627dWriterDto = new BtCciaCustDivdCsnh627dDto();

                        csnh627dWriterDto.setProcResult("0");
                        csnh627dWriterDto.setNewCustId(btCciaCustDivdCsnh627dDto.getNewCustId());
                        csnh627dWriterDto.setCustId(btCciaCustDivdCsnh627dDto.getCustId());
                        csnh627dWriterDto.setSaId(btCciaCustDivdCsnh627dDto.getSaId());
                        csnh627dWriterDto.setOccurDate(btCciaCustDivdCsnh627dDto.getOccurDate());

                        this.infUpdateIcisWork(csnh627dWriterDto);
                    }

                }
            }
        }catch (Exception e) {
            LogUtil.info("Exit reson : custSpatnLnkAgntr fail");
            return ;
        }
        LogUtil.info("[endtime/custSpatnLnkAgntr]: {} ", new Timestamp(System.currentTimeMillis()));
        return ;
    }

    /**
     * @param csnh627dWriterDto
     */
    private void infUpdateIcisWork(BtCciaCustDivdCsnh627dDto csnh627dWriterDto) {
        //ICIS(CCIA_CUST_DIVD)에 처리 결과 값을 갱신
    	//2024.04.18 ICISTR-96574 로그점검 조치를 위한 주석처리
    	//LogUtil.info("+++++++++++++++start infUpdateIcisWork+++++++++++++++++{}", csnh627dWriterDto.toString());
        btCciaCustDivdSvc.updateIcisWork(csnh627dWriterDto);
    }


}
