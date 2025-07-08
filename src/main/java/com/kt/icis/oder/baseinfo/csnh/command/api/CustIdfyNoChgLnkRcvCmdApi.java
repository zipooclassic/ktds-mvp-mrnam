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

import com.kt.icis.oder.baseinfo.common.command.payload.in.BiDaemonTmpUseInCmdPyld;
import com.kt.icis.oder.baseinfo.common.command.payload.out.BiDaemonTmpUseOutCmdPyld;
import com.kt.icis.oder.baseinfo.common.command.payload.out.dto.BiDaemonTmpUseOutCmdDto;
import com.kt.icis.oder.baseinfo.csnh.command.controller.CustIdfyNoChgLnkRcvCmdCntr;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @Project: ICIS TR 오더공통 icis-oder-wrlincomn-daemon
 * @FileName: CustIdfyNoChgLnkRcvCmdApi
 * @Class 설명 : 고객식별번호변경 연동 수신 처리 데몬 API 작업
 * @작성일 : 2024. 08. 17.
 * @작성자 : 리그시스템 / 91347489 / 강동순
 */
@RestController
@RequestMapping("/csnh")
@RequiredArgsConstructor
@Tag(name = "고객식별번호변경연동수신처리", description = "고객식별번호변경 연동 수신 처리 데몬-CSNH624D")
public class CustIdfyNoChgLnkRcvCmdApi {

	private final CustIdfyNoChgLnkRcvCmdCntr  contrller;
	/**
	 * @Method Name : custIdfyNoChgLnkRcv
	 * @Method 설명 : 고객식별번호변경 연동 수신 처리
	 * @작성일 : 2024. 08. 17.
	 * @작성자 : 리그시스템 / 91347489 / 강동순
	 * @param : BiDaemonTmpUseInCmdPyld
	 * @return : BiDaemonTmpUseOutCmdPyld
	 */
	@Operation(summary = "고객식별번호변경연동수신처리", description = "고객식별번호변경 연동 수신 처리 데몬-CSNH624D")
    @PostMapping(path = "/custidfy/nochglnkrcv")
    public BiDaemonTmpUseOutCmdPyld custIdfyNoChgLnkRcv(@RequestBody BiDaemonTmpUseInCmdPyld inPyld) {
		BiDaemonTmpUseOutCmdDto rtnDto = new BiDaemonTmpUseOutCmdDto();
		contrller.custIdfyNoChgLnkRcv();
        return  null;  // Json 변환 후 response 전송
	}
}
