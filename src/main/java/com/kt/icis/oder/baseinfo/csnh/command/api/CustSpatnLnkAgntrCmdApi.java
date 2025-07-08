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
import com.kt.icis.oder.baseinfo.csnh.command.controller.CustSpatnLnkAgntrCmdCntr;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @Project: ICIS TR 오더공통 icis-oder-wrlincomn-daemon
 * @FileName: CustSpatnLnkAgntrCmdApi
 * @Class 설명 : 고객분리연동 오류시 재처리 데몬 API 작업
 * @작성일 : 2024. 08. 17.
 * @작성자 : 리그시스템 / 91347489 / 강동순
 */
@RestController
@RequestMapping("/csnh")
@RequiredArgsConstructor
@Tag(name = "고객분리연동오류시재처리", description = "고객분리연동 오류시 재처리 데몬-CSNH627D")
public class CustSpatnLnkAgntrCmdApi {

	private final CustSpatnLnkAgntrCmdCntr  contrller;
	/**
	 * @Method Name : custSpatnLnkAgntr
	 * @Method 설명 : 고객분리연동 오류시 재처리 데몬
	 * @작성일 : 2024. 08. 17.
	 * @작성자 : 리그시스템 / 91347489 / 강동순
	 * @param : BiDaemonTmpUseInCmdPyld
	 * @return : BiDaemonTmpUseOutCmdPyld
	 */
	@Operation(summary = "고객분리연동오류시재처리", description = "고객분리연동 오류시 재처리 데몬-CSNH627D")
    @PostMapping(path = "/custspatn/lnkagntr")
    public BiDaemonTmpUseOutCmdPyld custSpatnLnkAgntr(@RequestBody BiDaemonTmpUseInCmdPyld inPyld) {
		BiDaemonTmpUseOutCmdDto rtnDto = new BiDaemonTmpUseOutCmdDto();
		contrller.custSpatnLnkAgntr();
        return  null;  // Json 변환 후 response 전송
	}
}
