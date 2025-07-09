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

package com.kt.icis.oder.baseinfo.csnh.command.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.icis.oder.baseinfo.csnh.command.controller.CustInfoCurnTrtSttusCmdCntr;
import com.kt.icis.oder.baseinfo.csnh.command.payload.in.CustInfoCurnTrtSttusInCmdPyld;
import com.kt.icis.oder.baseinfo.csnh.command.payload.out.CustInfoCurnTrtSttusOutCmdPyld;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @Project: ICIS TR 오더공통 icis-oder-baseinfo-daemon
 * @FileName: CustInfoCurnTrtSttusCmdApi
 * @Class 설명 : 고객정보 현행화 처리상태 저장(SF_CCMC_10010)
 * @작성일 : 2024. 12. 02.
 * @작성자 : 알앤비소프트웨어 / 91369161 / 김현진
 */
@RestController
@RequestMapping("/cust")
@RequiredArgsConstructor
@Tag(name = "고객정보 현행화 처리상태 저장(CIA연동 테이블)", description = "고객정보 현행화 처리상태 저장(CIA연동 테이블) - SF_CCMC_10010")
public class CustInfoCurnTrtSttusCmdApi {
	
	private final CustInfoCurnTrtSttusCmdCntr  custInfoCurnTrtSttusCmdCntr;
	
	/**
	 * @Method Name : saveCustInfoCurnTrtSttus
	 * @Method 설명 : 고객정보 현행화 처리상태 저장(SF_CCMC_10010)
	 * @작성일 : 2024. 12. 02.
	 * @작성자 : 알앤비소프트웨어 / 91369161 / 김현진
	 * @param : BiDaemonTmpUseInCmdPyld
	 * @return : BiDaemonTmpUseOutCmdPyld
	 */
	@Operation(summary = "고객정보 현행화 처리상태 저장(CIA연동 테이블)", description = "고객정보 현행화 처리상태 저장(CIA연동 테이블) - SF_CCMC_10010")
	@PostMapping(path = "/savecustinfocurntrtsttus")
	CustInfoCurnTrtSttusOutCmdPyld saveCustInfoCurnTrtSttus(@RequestBody CustInfoCurnTrtSttusInCmdPyld custInfoCurnTrtSttusInCmdPyld) {
		return CustInfoCurnTrtSttusOutCmdPyld.builder()
				.custInfoCurnTrtSttusOutCmdDto( custInfoCurnTrtSttusCmdCntr.saveCustInfoCurnTrtSttus( custInfoCurnTrtSttusInCmdPyld.getCustInfoCurnTrtSttusInCmdDto() ))
				.build();
	}
}
