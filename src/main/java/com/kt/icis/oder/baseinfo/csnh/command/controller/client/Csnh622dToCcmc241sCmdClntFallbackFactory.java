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
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.Csnh622dToCcmc241sInCmdPyld;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.out.Csnh622dToCcmc241sOutCmdPyld;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.out.dto.Csnh622dToCcmc241sOutCmdDto;

@Component
public class Csnh622dToCcmc241sCmdClntFallbackFactory implements FallbackFactory<Csnh622dToCcmc241sCmdClnt> {
    @Override
    public Csnh622dToCcmc241sCmdClnt create(Throwable cause) {
        ResponeMessage responeMessage = ExceptionUtil.getResponseMessage(cause);
        return new Csnh622dToCcmc241sCmdClnt() {



            @Override
            public Csnh622dToCcmc241sOutCmdPyld callCcmc241s(String feignHeader, Csnh622dToCcmc241sInCmdPyld inpyld) {

                LogUtil.info("********** inPyld : " + inpyld.toString());
                LogUtil.info("********** ResponseCode : " + responeMessage.getResponseCode());
                LogUtil.info("********** ResponseType : " + responeMessage.getResponseType());
                LogUtil.info("********** ResponseTitle : " + responeMessage.getResponseTitle());
                LogUtil.info("********** ResponseBasc : " + responeMessage.getResponseBasc());
                LogUtil.info("********** ResponseDtal : " + responeMessage.getResponseDtal());
                return Csnh622dToCcmc241sOutCmdPyld.builder().ccmc241sOutDto(Csnh622dToCcmc241sOutCmdDto.builder().build()).build();


            }

        };
    }

}