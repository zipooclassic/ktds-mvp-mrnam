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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Csnh622dToCcmc241sInCmdDto {

    private String saId;
    private String custId;
    private String newCustId;
    private String regOfcCd;
    private String regrId;

    private String regerEmpName;
    private String reqerName;
    private String custRelCd;
    private String reqerCustNo;
    private String reqerPhnNo;

    private String reqerCellphnNo;
}
