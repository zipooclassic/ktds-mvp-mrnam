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
import org.springframework.web.bind.annotation.RequestHeader;

import com.kt.icis.cmmnfrwk.constant.Constant;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.CustPartiTrtInCmdPyld;
import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.out.CustPartiTrtOutCmdPyld;

@Component
public class EiCustPartiTrtCmdClntFallbackFactory implements FallbackFactory<EiCustPartiTrtCmdClnt>{
	@Override
	public EiCustPartiTrtCmdClnt create(Throwable cause) {
		return new EiCustPartiTrtCmdClnt() {

			@Override
			public CustPartiTrtOutCmdPyld regLobCustPartiTrt(@RequestHeader(Constant.FEIGN_HEADER) String header, CustPartiTrtInCmdPyld custPartiTrtInCmdPyld) {
				CustPartiTrtOutCmdPyld out = new CustPartiTrtOutCmdPyld();
				return out;
			}
		};
	}
}