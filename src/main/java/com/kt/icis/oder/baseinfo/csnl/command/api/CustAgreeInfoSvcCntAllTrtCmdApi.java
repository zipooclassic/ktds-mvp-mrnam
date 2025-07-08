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
import com.kt.icis.oder.baseinfo.csnl.command.controller.CustAgreeInfoSvcCntAllTrtCmdCntr;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @Project: ICIS TR 오더공통 icis-oder-baseinfo-daemon
 * @FileName: CustAgreeInfoSvcCntAllTrtCmdApi
 * @Class 설명 : 고객동의 정보 서비스계약 일괄 처리 데몬 API(배치->데몬변경)
 * @작성일 : 2025. 01. 20.
 * @작성자 : 알앤비소프트웨어 / 91369160 / 손경화
 */
@RestController
@RequestMapping("/csnl921d")
@RequiredArgsConstructor
@Tag(name = "고객동의정보 서비스계약 일괄처리", description = "고객동의정보 서비스계약 일괄처리데몬-CSNL921D")
public class CustAgreeInfoSvcCntAllTrtCmdApi {
	
	private final CustAgreeInfoSvcCntAllTrtCmdCntr controller;
	
	@Operation(summary = "고객동의정보 서비스계약 일괄처리", description = "고객동의정보 서비스계약 일괄처리데몬-CSNL921D")
    @PostMapping(path = "/custagreeinfosvccntalltrt")
	public BiDaemonTmpUseOutCmdPyld custAgreeInfoSvcCntAllTrt(@RequestBody BiDaemonTmpUseInCmdPyld inPyld) {
		BiDaemonTmpUseOutCmdDto rtnDto = new BiDaemonTmpUseOutCmdDto();
		controller.custAgreeInfoSvcCntAllTrt();
		rtnDto.setResltFlag("S");
        rtnDto.setResltMsg("CSNL921D Excecution");
        return  BiDaemonTmpUseOutCmdPyld.builder().outDs(rtnDto).build();   // Json 변환 후 response 전송
		
	}

}
