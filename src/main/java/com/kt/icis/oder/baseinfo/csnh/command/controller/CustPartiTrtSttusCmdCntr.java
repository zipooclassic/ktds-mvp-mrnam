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

import org.springframework.stereotype.Component;

import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciacustdivdCsnh622dDto;
import com.kt.icis.oder.baseinfo.common.service.BtCciaCustDivdSvc;
import com.kt.icis.oder.baseinfo.csnh.command.payload.in.dto.CustPartiTrtSttusInCmdDto;
import com.kt.icis.oder.baseinfo.csnh.command.payload.out.dto.CustPartiTrtSttusOutCmdDto;

import lombok.RequiredArgsConstructor;

/**
 * @Class Name : CustSpatnLnkSttusCmdCntr
 * @Class 설명 : 고객분리정보 처리상태갱신
 * @작성일 : 2025. 01. 07.
 * @작성자 : 알앤비소프트웨어 / 91369160 / 손경화
 */
@Component
@RequiredArgsConstructor
public class CustPartiTrtSttusCmdCntr {
	
	private final BtCciaCustDivdSvc btCciaCustDivdSvc;
	
	public CustPartiTrtSttusOutCmdDto saveCustDivdSttus(CustPartiTrtSttusInCmdDto inCmdDto) {
		
		/*
		UPDATE  BI_CCIA_CUST_DIVD
        SET      PROC_RESULT    =   :#{#dto.procResult?:''}
                ,PROC_DATE      =   SYSDATE
                ,ERR_MSG        =   :#{#dto.errMsg?:''}
                ,IPS_PROC_RESULT=   DECODE(:#{#dto.procResult?:''}, 'F', 'F', 'N', 'N', 'S', '0')
        WHERE   SA_ID           =   :#{#dto.saId?:''}
        AND     CUST_ID         =   :#{#dto.custId?:''}
        AND     OCCUR_DATE      =   :#{#dto.occurDate?:''}
        AND     NEW_CUST_ID     =   :#{#dto.newCustId?:''}		
		*/
		BtCciacustdivdCsnh622dDto csnh622dDto = new BtCciacustdivdCsnh622dDto();
		csnh622dDto.setSaId(inCmdDto.getSaId());
		csnh622dDto.setProcResult(inCmdDto.getVcResultCd());
		csnh622dDto.setCustId(inCmdDto.getCustId());
		csnh622dDto.setNewCustId(inCmdDto.getNewCustId());
		csnh622dDto.setOccurDate(inCmdDto.getOccurDate());
		csnh622dDto.setErrMsg(inCmdDto.getVcResultMsg());
		btCciaCustDivdSvc.updateBySaidCustidOccurdateNewcustid(csnh622dDto);
        return CustPartiTrtSttusOutCmdDto.builder()
        		.resultCd("S")
        		.resultMsg("성공")
        		.build();
    }

}
