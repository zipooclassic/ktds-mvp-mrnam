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
package com.kt.icis.oder.baseinfo.csnl.command.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.icis.oder.baseinfo.common.command.payload.in.BiDaemonTmpUseInCmdPyld;
import com.kt.icis.oder.baseinfo.common.command.payload.out.BiDaemonTmpUseOutCmdPyld;
import com.kt.icis.oder.baseinfo.common.command.payload.out.dto.BiDaemonTmpUseOutCmdDto;
import com.kt.icis.oder.baseinfo.csnl.command.controller.CustInfoAgreeTrtCmdCntr;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @Project: ICIS TR 오더공통 icis-oder-baseinfo-daemon
 * @FileName: CustInfoAgreeTrtCmdApi
 * @Class 설명 : 고객정보동의 처리 데몬 API
 * @작성일 : 2025. 02. 05.
 * @작성자 : 리그시스템 / 91361628 / 최현
 */
@RestController
@RequestMapping("/csnl")
@RequiredArgsConstructor
@Tag(name = "고객정보동의 처리", description = "고객정보동의 처리 데몬 - CSNL920D")
public class CustInfoAgreeTrtCmdApi {
	
	private final CustInfoAgreeTrtCmdCntr controller;
	
	@Operation(summary = "고객정보동의 처리", description = "고객정보동의 처리 데몬 - CSNL920D")
    @PostMapping(path = "/custinfoagreetrt")
	public BiDaemonTmpUseOutCmdPyld custInfoAgreeTrt(@RequestBody BiDaemonTmpUseInCmdPyld inPyld) {
		BiDaemonTmpUseOutCmdDto rtnDto = new BiDaemonTmpUseOutCmdDto();
		rtnDto = controller.custInfoAgreeTrt();
        return  BiDaemonTmpUseOutCmdPyld.builder().outDs(rtnDto).build();   // Json 변환 후 response 전송	
	}
}
