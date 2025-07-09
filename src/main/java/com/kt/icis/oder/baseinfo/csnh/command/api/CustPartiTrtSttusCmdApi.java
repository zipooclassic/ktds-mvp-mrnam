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

import com.kt.icis.oder.baseinfo.csnh.command.controller.CustPartiTrtSttusCmdCntr;
import com.kt.icis.oder.baseinfo.csnh.command.payload.in.CustPartiTrtSttusInCmdPyld;
import com.kt.icis.oder.baseinfo.csnh.command.payload.out.CustPartiTrtSttusOutCmdPyld;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @Project: ICIS TR 오더공통 icis-oder-wrlincomn-daemon
 * @FileName: CustSpatnLnkSttusCmdApi
 * @Class 설명 : 고객분리 처리상태 갱신
 * @작성일 : 2025. 01. 07.
 * @작성자 : 알앤비소프트웨어 / 91369160 / 손경화
 */
@RestController
@RequestMapping("/csnh")
@RequiredArgsConstructor
@Tag(name = "고객분리연동처리상태갱신", description = "고객분리연동 처리시 상태갱신")
public class CustPartiTrtSttusCmdApi {
	
	private final CustPartiTrtSttusCmdCntr  cmdCntr;
	
	@Operation(summary = "고객분리처리상태갱신", description = "고객분리처리상태갱신")
    @PostMapping(path = "/custpartitrt/sttus")
    public CustPartiTrtSttusOutCmdPyld custpartitrtSttus(@RequestBody CustPartiTrtSttusInCmdPyld inPyld) {
		return CustPartiTrtSttusOutCmdPyld.builder()
				.custPartiTrtSttusOutCmdDto( cmdCntr.saveCustDivdSttus(inPyld.getLobCustPartiTrtOutCmdDto()) )
				.build();
	}

}
