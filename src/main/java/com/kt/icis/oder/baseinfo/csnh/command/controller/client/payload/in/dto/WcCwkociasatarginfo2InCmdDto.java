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

package com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: ICIS TR 오더공통 icis-oder-wrlincomn-common
 * @FileName: Cwkociasatarginfo2InCmdDto
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WcCwkociasatarginfo2InCmdDto {
    @Schema(description = "고객아이디", nullable = false, maxLength = 11)
    private String custId;

    @Schema(description = "발생일자", nullable = false, maxLength = 14)
    private String occurDate;

    @Schema(description = "서비스구분")
    private String svcType;
}
