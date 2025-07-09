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

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import com.kt.icis.cmmnfrwk.payload.ResponeMessage;
import com.kt.icis.cmmnfrwk.utils.ExceptionUtil;
import com.kt.icis.cmmnfrwk.utils.LogUtil;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.Ccmc123sInCmdPyld;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.out.Ccmc123sOutCmdPyld;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.out.dto.Ccmc123sOutCmdDto;

@Component
public class Ccmc123sCmdClntFallbackFactory implements FallbackFactory<Ccmc123sCmdClnt> {
    @Override
    public Ccmc123sCmdClnt create(Throwable cause) {
        ResponeMessage responeMessage = ExceptionUtil.getResponseMessage(cause);
        return new Ccmc123sCmdClnt() {

            @Override
            public Ccmc123sOutCmdPyld callCcmc123s(String feignHeader, Ccmc123sInCmdPyld ccmc123sInPyld) {

                LogUtil.info("********** inPyld : " + ccmc123sInPyld.toString());
                LogUtil.info("********** ResponseCode : " + responeMessage.getResponseCode());
                LogUtil.info("********** ResponseType : " + responeMessage.getResponseType());
                LogUtil.info("********** ResponseTitle : " + responeMessage.getResponseTitle());
                LogUtil.info("********** ResponseBasc : " + responeMessage.getResponseBasc());
                LogUtil.info("********** ResponseDtal : " + responeMessage.getResponseDtal());
                return Ccmc123sOutCmdPyld.builder().ccmc123sOutDto(Ccmc123sOutCmdDto.builder().build()).build();

            }

        };
    }

}