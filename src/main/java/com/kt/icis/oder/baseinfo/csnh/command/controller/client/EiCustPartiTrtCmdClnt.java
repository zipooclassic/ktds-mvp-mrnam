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

package com.kt.icis.oder.baseinfo.csnh.command.controller.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.kt.icis.cmmnfrwk.constant.Constant;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.CustPartiTrtInCmdPyld;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.out.CustPartiTrtOutCmdPyld;

@FeignClient(name="EI.EiCustPartiTrtCmdClnt", url = "${api.entprinet.url}", fallbackFactory=EiCustPartiTrtCmdClntFallbackFactory.class)
public interface EiCustPartiTrtCmdClnt {

	@PostMapping("/cust/partitrt/reglobcustpartitrt")
	CustPartiTrtOutCmdPyld regLobCustPartiTrt(@RequestHeader(Constant.FEIGN_HEADER) String header, CustPartiTrtInCmdPyld custPartiTrtInCmdPyld);
	
}