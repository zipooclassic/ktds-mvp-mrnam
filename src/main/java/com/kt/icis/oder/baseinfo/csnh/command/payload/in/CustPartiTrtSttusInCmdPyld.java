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

package com.kt.icis.oder.baseinfo.csnh.command.payload.in;

import com.kt.icis.oder.baseinfo.csnh.command.payload.in.dto.CustPartiTrtSttusInCmdDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustPartiTrtSttusInCmdPyld {
	//고객분리상태 설정을 위한 CustPartiTrtOutCmdPyld 정보값을 INPUT값으로 설정
	private CustPartiTrtSttusInCmdDto lobCustPartiTrtOutCmdDto;
}
