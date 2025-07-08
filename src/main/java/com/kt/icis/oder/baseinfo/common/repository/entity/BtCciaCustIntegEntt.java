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

package com.kt.icis.oder.baseinfo.common.repository.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciacustintegDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 계약고객통합 연동 db table entity
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Table("BI_CCIA_CUST_INTEG")
public class BtCciaCustIntegEntt implements Persistable<String> {

    // description = "고객아이디", nullable = false, maxLength = 11
    //@Id 복합키 사용불가
    @Column("CUST_ID")
    private String custId;

    // description = "발생일자", nullable = false, maxLength = 14
    //@Id 복합키 사용불가
    @Column("OCCUR_DATE")
    private String occurDate;

    // description = "고객식별번호유형코드", nullable = false, maxLength = 1
    @Column("CUST_NO_TYPE")
    private String custNoType;

    // description = "고객식별번호", nullable = false, maxLength = 20
    @Column("CUST_NO")
    private String custNo;

    // description = "통합후고객아이디", nullable = true, maxLength = 11
    @Column("INTEG_CUST_ID")
    private String integCustId;

    // description = "신청자명", nullable = true, maxLength = 50
    @Column("REQER_NAME")
    private String reqerName;

    // description = "신청자고객번호", nullable = true, maxLength = 20
    @Column("REQER_CUST_NO")
    private String reqerCustNo;

    // description = "신청자이동전화번호", nullable = true, maxLength = 20
    @Column("REQER_PHN_NO")
    private String reqerPhnNo;

    // description = "신청자이동전화번호", nullable = true, maxLength = 14
    @Column("REQER_CELLPHN_NO")
    private String reqerCellphnNo;

    // description = "신청자이메일", nullable = true, maxLength = 30
    @Column("REQER_EMAIL")
    private String reqerEmail;

    // description = "고객관계코드", nullable = true, maxLength = 2
    @Column("CUST_REL_CD")
    private String custRelCd;

    // description = "신청자정보참고사항", nullable = true, maxLength = 100
    @Column("REQER_REF")
    private String reqerRef;

    // description = "처리일시", example = "YYYYMMDDHHMISS", nullable = true
    @Column("PROC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp procDate;

    // description = "처리결과", nullable = true, maxLength = 1
    @Column("PROC_RESULT")
    private String procResult;

    // description = "에러메세지", nullable = true, maxLength = 100
    @Column("ERR_MSG")
    private String errMsg;

    // description = "등록일시", example = "YYYYMMDDHHMISS", nullable = true
    @Column("REG_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp regDate;

    // description = "등록국코드", nullable = true, maxLength = 6
    @Column("REG_OFC_CD")
    private String regOfcCd;

    // description = "등록자아이디", nullable = true, maxLength = 9
    @Column("REGR_ID")
    private String regrId;

    // description = "등록자명", nullable = true, maxLength = 50
    @Column("REGER_EMP_NAME")
    private String regerEmpName;

    // description = "프로그램아이디", nullable = true, maxLength = 10
    @Column("PGM_ID")
    private String pgmId;

    // description = "고객정보처리결과", nullable = true, maxLength = 1
    @Column("CUST_PROC_RESULT")
    private String custProcResult;

    // description = "고객처리일시", example = "YYYYMMDDHHMISS", nullable = true
    @Column("CUST_PROC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp custProcDate;

    // description = "IPS처리일자", example = "YYYYMMDDHHMISS", nullable = true
    @Column("IPS_PROC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp ipsProcDate;

    // description = "IPS처리결과", nullable = true, maxLength = 1
    @Column("IPS_PROC_RESULT")
    private String ipsProcResult;

    // description = "인터페이스이력번호", nullable = true, maxLength = 22
    @Column("IF_HST_NO")
    private BigDecimal ifHstNo;

    @Transient
    public boolean isNew = false;

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public String getId() {
        return null;
    }

    public static BtCciaCustIntegEntt of(BtCciacustintegDto dto) {
        return BtCciaCustIntegEntt.builder()
                .custId(dto.getCustId())
                .occurDate(dto.getOccurDate())
                .custNoType(dto.getCustNoType())
                .custNo(dto.getCustNo())
                .integCustId(dto.getIntegCustId())
                .reqerName(dto.getReqerName())
                .reqerCustNo(dto.getReqerCustNo())
                .reqerPhnNo(dto.getReqerPhnNo())
                .reqerCellphnNo(dto.getReqerCellphnNo())
                .reqerEmail(dto.getReqerEmail())
                .custRelCd(dto.getCustRelCd())
                .reqerRef(dto.getReqerRef())
                .procDate(dto.getProcDate())
                .procResult(dto.getProcResult())
                .errMsg(dto.getErrMsg())
                .regDate(dto.getRegDate())
                .regOfcCd(dto.getRegOfcCd())
                .regrId(dto.getRegrId())
                .regerEmpName(dto.getRegerEmpName())
                .pgmId(dto.getPgmId())
                .custProcResult(dto.getCustProcResult())
                .custProcDate(dto.getCustProcDate())
                .ipsProcDate(dto.getIpsProcDate())
                .ipsProcResult(dto.getIpsProcResult())
                .ifHstNo(dto.getIfHstNo())
                .build();
    }
}