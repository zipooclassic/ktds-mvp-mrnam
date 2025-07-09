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

package com.kt.icis.oder.baseinfo.csnl.command.repository.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.data.relational.core.mapping.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BI_CCSTCUSTAGENT db table dto
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Table("BI_CCSTCUSTAGENT")
public class BiCcstcustagentCmdEntt implements Serializable {
    private static final long serialVersionUID = 1672049507159002749L;

    @Schema(description = "AGNT_BTHDAY", nullable = true, maxLength = 8)
    //	private Timestamp agntBthday;
    private String agntBthday;

    @Schema(description = "ACTV_YN", nullable = true, maxLength = 1)
    private String actvYn;

    @Schema(description = "AGNT_NAME", nullable = true, maxLength = 100)
    private String agntName;

    @Schema(description = "RCVER_BTHDAY", nullable = true, maxLength = 8)
    //    private Timestamp rcverBthday;
    private String rcverBthday;

    @Schema(description = "접수자이메일", nullable = true, maxLength = 50)
    private String rcverEmail;

    @Schema(description = "접수자이동전화번호", nullable = true, maxLength = 40)
    private String rcverMphonNo;

    @Schema(description = "접수자명", nullable = true, maxLength = 100)
    private String rcverName;

    @Schema(description = "접수자성별코드", nullable = true, maxLength = 30)
    private String rcverSexCd;

    @Schema(description = "EAI_SEQ", nullable = false, maxLength = 22)
    private BigDecimal eaiSeq;

    @Schema(description = "EAI대상시스템아이디", nullable = true, maxLength = 4)
    private String eaiTgtSysId;

    @Schema(description = "EAI상태코드", nullable = true, maxLength = 8)
    private String eaiSttusCd;

    @Schema(description = "AGNT_EMAIL", nullable = true, maxLength = 120)
    private String agntEmail;

    @Schema(description = "EAI대상구분코드", nullable = true, maxLength = 3)
    private String eaiTrtDivCd;

    @Schema(description = "EAI_CRET_DT", nullable = true, maxLength = 12)
    private Timestamp eaiCretDt;

    @Schema(description = "EAI_CHG_DT", nullable = true, maxLength = 12)
    private Timestamp eaiChgDt;

    @Schema(description = "EAI_ERR_MSG_SBST", nullable = true, maxLength = 500)
    private String eaiErrMsgSbst;

    @Schema(description = "GENESIS_AGENT_ID", nullable = true, maxLength = 100)
    private String genesisAgentId;

    @Schema(description = "WO_FLAG", nullable = true, maxLength = 1)
    private String woFlag;

    @Schema(description = "작업일시", nullable = true, maxLength = 8)
    private Timestamp woDate;

    @Schema(description = "작업결과에러내용", nullable = true, maxLength = 100)
    private String woErrMsg;

    @Schema(description = "AGNT_END_DATE", nullable = true, maxLength = 8)
    //    private Timestamp agntEndDate;
    private String agntEndDate;

    @Schema(description = "AGNT_SEQ", nullable = true, maxLength = 22)
    private String agntSeq;

    @Schema(description = "AGNT_MPHON_NO", nullable = true, maxLength = 40)
    private String agntMphonNo;

    @Schema(description = "AGNT_SEX_CD", nullable = true, maxLength = 30)
    private String agntSexCd;

    @Schema(description = "AGNT_START_DATE", nullable = true, maxLength = 8)
    //    private Timestamp agntStartDate;
    private String agntStartDate;

    @Schema(description = "GENESIS_CUST_ID", nullable = true, maxLength = 15)
    private String genesisCustId;

    @Schema(description = "GENESIS_CUST_NAME", nullable = true, maxLength = 100)
    private String genesisCustName;

    @Schema(description = "ICIS_CUST_ID", nullable = true, maxLength = 11)
    private String icisCustId;

    @Schema(description = "COP_REG_NO", nullable = true, maxLength = 10)
    private String copRegNo;

}