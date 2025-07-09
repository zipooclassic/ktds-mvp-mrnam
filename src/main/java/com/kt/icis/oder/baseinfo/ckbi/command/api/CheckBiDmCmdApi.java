/**************************************************************************************
 * ICIS TR version 1.0
 *
 *  Copyright ⓒ 2022 kt/ktds corp. All rights reserved.
 *
 *  This is a proprietary software of kt corp, and you may not use this file except in
 *  compliance with license agreement with kt corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of kt corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 *************************************************************************************/

package com.kt.icis.oder.baseinfo.ckbi.command.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.icis.oder.baseinfo.ckbi.command.controller.CheckBiDmCmdCntr;
import com.kt.icis.oder.lob.conncheck.payload.in.ConnCheckInPyld;
import com.kt.icis.oder.lob.conncheck.payload.out.ConnCheckOutPyld;
import com.kt.icis.oder.lob.conncheck.payload.out.dto.ConnCheckResultOutDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @Project: ICIS TR 오더공통 icis-oder-baseinfo-daemon
 * @FileName: CheckBiDmCmdApi
 * @Class 설명 : 데몬 점검 데몬 API 작업
 * @작성일 : 2025. 01. 03.
 * @작성자 : 오더AA / 91367924  / 임동열
 */

@RestController
@RequestMapping("/ckbi")
@RequiredArgsConstructor
@Tag(name = "BI데몬연결점검", description ="BI데몬연결점검")
public class CheckBiDmCmdApi {
	
	private final CheckBiDmCmdCntr checkBiDmCmdCntr;
	
	@Operation(summary = "BI데몬연결점검", description ="BI데몬연결점검")
	@PostMapping(path = "/result")
	public ConnCheckOutPyld getResult(@RequestBody  ConnCheckInPyld inPyld) {
		ConnCheckResultOutDto outDto = checkBiDmCmdCntr.getResult(inPyld);
		return ConnCheckOutPyld.builder().connCheckResultOutDs(outDto).build();
	}

}
