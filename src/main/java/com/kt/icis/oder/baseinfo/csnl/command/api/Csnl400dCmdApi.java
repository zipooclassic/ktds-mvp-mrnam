/**************************************************************************************
 * ICIS version 1.0
 *
 *  Copyright ⓒ 2023 kt/ktds corp. All rights reserved.
 *
 *  This is a proprietary software of kt corp, and you may not use this file except in
 *  compliance with license agreement with kt corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of kt corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 *************************************************************************************/

package com.kt.icis.oder.baseinfo.csnl.command.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.icis.oder.baseinfo.csnl.command.controller.Csnl400dCmdCntr;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/csnl")
@RequiredArgsConstructor
@Tag(name = "CRM 법정위임 대리인 정보 수신", description = "CRM 법정위임 대리인 정보 수신")
public class Csnl400dCmdApi {

    private final Csnl400dCmdCntr csnl400dCmdCntr;

    @Operation(summary = "CRM 법정위임 대리인 정보 수신", description = "CRM 법정위임 대리인 정보 수신")
    @PostMapping(path = "/csnl400d")
    public void csnl400d() {
        csnl400dCmdCntr.batch();
    }

}
