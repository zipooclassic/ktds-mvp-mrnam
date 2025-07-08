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
import java.sql.Timestamp;

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
@Schema(description = "CWKOORGMASTER")
public class BtCwkoorgmasterDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5172878470745788125L;

    private String woFlag;
    private String woErrMsg;

    private String rowId;
    private String orgcode;
    private String parentorgcode;
    private String orgname;

    private String orgengname;

    private String divisionwork;

    private String zipid;

    private String postalcd;

    private String addr1;

    private String addr2;

    private String businessgb;

    private String orgtype;

    private String ispayorg;

    private String telno;

    private String faxno;

    private String isprofitcenter;
    private String profitcenter;

    private String iscostcenter;

    private String costcenter;

    private String isevaluate;

    private String headcode;

    private String orglevel;

    private String orgorder;

    private String formorder;

    private String isleaf;

    private Timestamp creationdtime;

    private Timestamp updatedtime;

    private String active;

    private String capempno;

    private String orggrade;

    private String lastupdator;

    private String orgsymname;

    private String eaiSeq;

    private String eaiState;

    private String eaiOp;

    private String eaiCdate;

    private String eaiUdate;

    private String eaiMsg;


}