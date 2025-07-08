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

package com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.out;



import com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.out.dto.WcCwkociasatarginfo2OutCmdDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: ICIS TR 오더공통 icis-oder-wrlincomn-common
 * @FileName: CwkoerpempinfoOutCmdPyld
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WcCwkociasatarginfo2OutCmdPyld {

	WcCwkociasatarginfo2OutCmdDto cwkociasatarginfo2OutCmdDto;
}
