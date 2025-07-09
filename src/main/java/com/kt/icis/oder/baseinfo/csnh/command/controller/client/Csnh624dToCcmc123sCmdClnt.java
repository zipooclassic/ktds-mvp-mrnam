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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.kt.icis.cmmnfrwk.constant.Constant;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.Csnh624dToCcmc123sInCmdPyld;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.out.Csnh624dToCcmc123sOutCmdPyld;

//ms.현재모듈.호출할모듈
/**
 * 수신 API가 개발이 완료 되지 않아 sample 프로젝트에 dummy api 호출
 */
@FeignClient(name="BT.csnh624d.ccmc123s", url = "http://icis-oder-sample.t-order.svc/sample", fallbackFactory=Csnh624dToCcmc123sCmdClntFallbackFactory.class)
public interface Csnh624dToCcmc123sCmdClnt {

    @PostMapping(value="/dummy/ccmc123s")
    Csnh624dToCcmc123sOutCmdPyld callCcmc123s(@RequestHeader(Constant.FEIGN_HEADER) String feignHeader, @RequestBody Csnh624dToCcmc123sInCmdPyld ccmc123sInPyld);




}
