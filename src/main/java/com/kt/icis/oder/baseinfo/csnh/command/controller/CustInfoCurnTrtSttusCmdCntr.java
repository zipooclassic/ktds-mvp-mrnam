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

import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciaCustChangInfoCsnh624dDto;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciacustintegCsnh625dDto;
import com.kt.icis.oder.baseinfo.common.service.BtCciaCustChangInfoSvc;
import com.kt.icis.oder.baseinfo.common.service.BtCciacustintegSvc;
import com.kt.icis.oder.baseinfo.csnh.command.payload.in.dto.CustInfoCurnTrtSttusInCmdDto;
import com.kt.icis.oder.baseinfo.csnh.command.payload.out.dto.CustInfoCurnTrtSttusOutCmdDto;

import lombok.RequiredArgsConstructor;

/**
 * @Class Name : CustInfoCurnTrtSttusCmdCntr
 * @Class 설명 : 고객정보 현행화 처리상태 저장(SF_CCMC_10010)
 * @작성일 : 2024. 12. 02.
 * @작성자 : 알앤비소프트웨어 / 91369161 / 김현진
 */
@Component
@RequiredArgsConstructor
public class CustInfoCurnTrtSttusCmdCntr {
    
    private final BtCciacustintegSvc btCciaCustIntegSvc;
    private final BtCciaCustChangInfoSvc btCciaCustChangInfoSvc;
    
    /**
     * @Method Name : saveCustInfoCurnTrtSttus
     * @Method 설명 : 고객정보 현행화 처리상태 저장(CIA연동 테이블) - SF_CCMC_10010
     * @작성일 : 2024. 12. 02.
     * @작성자 : 알앤비소프트웨어 / 91369161 / 김현진
     * @param
     * @return
     */
    public CustInfoCurnTrtSttusOutCmdDto saveCustInfoCurnTrtSttus(CustInfoCurnTrtSttusInCmdDto custInfoCurnTrtSttusInCmdDto) {
    	if ( "I".equals( custInfoCurnTrtSttusInCmdDto.getCustProcType() ) ) {
    		BtCciacustintegCsnh625dDto btCciacustintegCsnh625dDto = new BtCciacustintegCsnh625dDto();
    		btCciacustintegCsnh625dDto.setCustId( custInfoCurnTrtSttusInCmdDto.getCustId() );
    		btCciacustintegCsnh625dDto.setOccurDate( custInfoCurnTrtSttusInCmdDto.getOccurDate() );
    		btCciacustintegCsnh625dDto.setProcResult( custInfoCurnTrtSttusInCmdDto.getProcResult() );
    		btCciacustintegCsnh625dDto.setErrMsg( custInfoCurnTrtSttusInCmdDto.getErrMsg() );
    		
    		btCciaCustIntegSvc.infUpdateCiaWork(btCciacustintegCsnh625dDto);
    	} else if ( "C".equals( custInfoCurnTrtSttusInCmdDto.getCustProcType() ) ) {
    		BtCciaCustChangInfoCsnh624dDto btCciaCustChangInfoCsnh624dDto = new BtCciaCustChangInfoCsnh624dDto();
    		btCciaCustChangInfoCsnh624dDto.setCustId( custInfoCurnTrtSttusInCmdDto.getCustId() );
    		btCciaCustChangInfoCsnh624dDto.setOccurDate( custInfoCurnTrtSttusInCmdDto.getOccurDate() );
    		btCciaCustChangInfoCsnh624dDto.setProcResult( custInfoCurnTrtSttusInCmdDto.getProcResult() );
    		btCciaCustChangInfoCsnh624dDto.setErrMsg( custInfoCurnTrtSttusInCmdDto.getErrMsg() );
    		
    		btCciaCustChangInfoSvc.infCUpdateCiaWork(btCciaCustChangInfoCsnh624dDto);
    	} else {
    		return CustInfoCurnTrtSttusOutCmdDto.builder()
            		.resultCd("F")
            		.resultMsg("실패(CustProcType 값 오류)")
            		.build();
    	}
        
        return CustInfoCurnTrtSttusOutCmdDto.builder()
        		.resultCd("S")
        		.resultMsg("성공")
        		.build();
    }
    
}
