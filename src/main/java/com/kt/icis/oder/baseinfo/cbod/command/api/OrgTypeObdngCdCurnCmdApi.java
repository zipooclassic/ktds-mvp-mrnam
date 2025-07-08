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

package com.kt.icis.oder.baseinfo.cbod.command.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.icis.oder.baseinfo.cbod.command.controller.OrgTypeObdngCdCurnCmdCntr;
import com.kt.icis.oder.baseinfo.common.command.payload.in.BiDaemonTmpUseInCmdPyld;
import com.kt.icis.oder.baseinfo.common.command.payload.out.BiDaemonTmpUseOutCmdPyld;
import com.kt.icis.oder.baseinfo.common.command.payload.out.dto.BiDaemonTmpUseOutCmdDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @Project: ICIS TR 오더공통 icis-oder-wrlincomn-daemon
 * @FileName: OrgTypeObdngCdCurnCmdApi
 * @Class 설명 : 조직유형, 국사코드 현행화 데몬 API 작업
 * @작성일 : 2024. 08. 06.
 * @작성자 : 리그시스템 / 91347489 / 강동순
 */
@RestController
@RequestMapping("/cbod")
@RequiredArgsConstructor
@Tag(name = "조직유형국사코드현행화데몬", description = "조직유형, 국사코드 현행화 데몬-CBOD905D")
public class OrgTypeObdngCdCurnCmdApi {

	private final OrgTypeObdngCdCurnCmdCntr  contrller;
	/**
	 * @Method Name : cbod905d
	 * @Method 설명 : 조직유형, 국사코드 현행화 데몬 한다.
	 * @작성일 : 2024. 08. 06.
	 * @작성자 : 리그시스템 / 91347489 / 강동순
	 * @param : BiDaemonTmpUseInCmdPyld
	 * @return : BiDaemonTmpUseOutCmdPyld
	 */
	@Operation(summary = "조직유형국사코드현행화데몬", description = "조직유형, 국사코드 현행화 데몬-CBOD905D")
    @PostMapping(path = "/orgtype/obdngcdcurn")
    public BiDaemonTmpUseOutCmdPyld cbod905d(@RequestBody BiDaemonTmpUseInCmdPyld inPyld) {
		BiDaemonTmpUseOutCmdDto rtnDto = new BiDaemonTmpUseOutCmdDto();
		contrller.cbod905d();
        return  null;  // Json 변환 후 response 전송
	}
}
