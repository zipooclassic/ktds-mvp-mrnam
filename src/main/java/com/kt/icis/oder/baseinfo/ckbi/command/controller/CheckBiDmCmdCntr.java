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

package com.kt.icis.oder.baseinfo.ckbi.command.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kt.icis.cmmnfrwk.utils.LogUtil;
import com.kt.icis.oder.lob.conncheck.controller.ConnCheckCntr;
import com.kt.icis.oder.lob.conncheck.payload.in.ConnCheckInPyld;
import com.kt.icis.oder.lob.conncheck.payload.out.dto.ConnCheckResultOutDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class CheckBiDmCmdCntr {

	@Value("${oder.project-code}")
	private String projectCode;
	
	private final ConnCheckCntr connCheckCntr;
	
	
	public ConnCheckResultOutDto getResult(ConnCheckInPyld inPyld) {
		
		final ConnCheckResultOutDto result = connCheckCntr.connCheck(inPyld.getConnCheckInDs());
		
		log.info("");
		log.info("{}", "Batch 상태점검");
		log.info("-----------------------------------------------------------------");
		log.info("점검일시 : {}", LocalDateTime.now());
		log.info("프로젝트 : {}", projectCode.toUpperCase());
		log.info("노드    : {}", result.getCheckStatusOutDs().getNodeName());
		log.info("노드IP  : {}", result.getCheckStatusOutDs().getNodeIp());
		log.info("점검결과", result.getCheckStatusOutDs().getCheckResult());
		log.info("-----------------------------------------------------------------");
		
		return result;
	}

}
