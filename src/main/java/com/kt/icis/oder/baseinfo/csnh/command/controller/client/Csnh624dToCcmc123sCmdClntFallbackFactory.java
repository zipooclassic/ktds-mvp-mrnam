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
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.Csnh624dToCcmc123sInCmdPyld;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.out.Ccmc123sOutCmdPyld;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.out.Csnh624dToCcmc123sOutCmdPyld;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.out.dto.Csnh624dToCcmc123sOutCmdDto;

@Component
public class Csnh624dToCcmc123sCmdClntFallbackFactory implements FallbackFactory<Csnh624dToCcmc123sCmdClnt> {
    @Override
    public Csnh624dToCcmc123sCmdClnt create(Throwable cause) {
        ResponeMessage responeMessage = ExceptionUtil.getResponseMessage(cause);
        return new Csnh624dToCcmc123sCmdClnt() {

            @Override
            public Csnh624dToCcmc123sOutCmdPyld callCcmc123s(String feignHeader, Csnh624dToCcmc123sInCmdPyld ccmc123sInPyld) {

                LogUtil.info("********** inPyld : " + ccmc123sInPyld.toString());
                LogUtil.info("********** ResponseCode : " + responeMessage.getResponseCode());
                LogUtil.info("********** ResponseType : " + responeMessage.getResponseType());
                LogUtil.info("********** ResponseTitle : " + responeMessage.getResponseTitle());
                LogUtil.info("********** ResponseBasc : " + responeMessage.getResponseBasc());
                LogUtil.info("********** ResponseDtal : " + responeMessage.getResponseDtal());
                
                return Csnh624dToCcmc123sOutCmdPyld.builder().ccmc123sOutDto(Csnh624dToCcmc123sOutCmdDto.builder().build()).build();
                
                

            }

        };
    }

}