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

package com.kt.icis.oder.baseinfo.common.repository.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BI_CBOCUSTAGENT db table dto
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Schema(description = "CWKOCRPEMPINFO")
public class BtCwkoerpempinfoDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6503876528218141090L;


    private String woFlag;
    private String woErrMsg;
    private String rowId;
    private String empNo;
    private String empNoOld;
    private String empNm;
    private String statusCd;
    private String deptCd;
    private String noFormCd;
    private String norTelNo;
    private String mailid;
    private String handphoneNo;
    private String jobCd;
    private String content;
    private String replDt;
    private String persg;
    private String zbizunitCd;
    private String zmanagerNo;
    private String eaiState;
    private String eaiOp;
    private String eaiCdate;
    private String eaiUdate;
    private String eaiSeq;
    private String socialNo;


}