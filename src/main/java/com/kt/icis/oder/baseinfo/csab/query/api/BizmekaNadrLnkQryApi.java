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

package com.kt.icis.oder.baseinfo.csab.query.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.icis.oder.baseinfo.csab.query.controller.BizmekaNadrLnkQryCntr;
import com.kt.icis.oder.baseinfo.csab.query.payload.in.BizmekaNadrLnkInQryPyld;
import com.kt.icis.oder.baseinfo.csab.query.payload.out.BizmekaNadrLnkOutQryPyld;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
 
@RequestMapping("/csab")
@RequiredArgsConstructor
@RestController
public class BizmekaNadrLnkQryApi {
	private final BizmekaNadrLnkQryCntr bizmekaNadrLnkQryCntr;

	/** 
	 * 비즈메카 새주소 연동
	 * @tuxedo CSAB801D 
	 * @param inPyld
	 * @return
	 */  
	@Tag(name = "비즈메카 새주소 연동", description = "비즈메카 새주소 연동")
	@PostMapping(path = "/bizmekanadrlnkqry")
	public BizmekaNadrLnkOutQryPyld getBizmekaNadr(@RequestBody BizmekaNadrLnkInQryPyld inPyld) {
		return bizmekaNadrLnkQryCntr.csab801dJob(inPyld.getBizmekaNadrLnkInQryDto());
	}
}
