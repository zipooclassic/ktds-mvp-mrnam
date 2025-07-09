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
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


@Component
public class WcCwkociasatarginfoCmdClntFeignFallbackFactory implements FallbackFactory<WcCwkociasatarginfoCmdClnt>{

	@Override
	public WcCwkociasatarginfoCmdClnt create(Throwable cause) {
		return new WcCwkociasatarginfoCmdClnt() {

			@Override
			public WcCwkociasatarginfo2OutCmdPyld insertRegcwkociasatarginfo(String feignHeader, WcCwkociasatarginfo2InCmdPyld inPyld) {
				return new WcCwkociasatarginfo2OutCmdPyld();
			}
		};

	}
}
