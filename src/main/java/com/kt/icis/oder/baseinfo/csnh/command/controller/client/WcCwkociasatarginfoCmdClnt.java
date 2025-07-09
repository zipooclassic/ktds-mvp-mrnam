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

import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.WcCwkociasatarginfo2InCmdPyld;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.out.WcCwkociasatarginfo2OutCmdPyld;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.kt.icis.cmmnfrwk.constant.Constant;

@FeignClient(name = "WC.CwkociasatarginfoClnt", url = "${api.wrlincomn.url}", fallbackFactory = WcCwkociasatarginfoCmdClntFeignFallbackFactory.class)
public interface WcCwkociasatarginfoCmdClnt {

	@PostMapping("/cust/cwko/insert/regcwkociasatarginfo")
	WcCwkociasatarginfo2OutCmdPyld insertRegcwkociasatarginfo(@RequestHeader(Constant.FEIGN_HEADER) String feignHeader, WcCwkociasatarginfo2InCmdPyld inPyld);

}
