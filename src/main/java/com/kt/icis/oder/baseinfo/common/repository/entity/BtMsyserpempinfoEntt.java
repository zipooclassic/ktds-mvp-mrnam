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

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtMsyserpempinfoDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ERP 사원정보 db table entity
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Table("BI_MSYSERPEMPINFO")
public class BtMsyserpempinfoEntt implements Persistable<String> {

    // description = "사번", nullable = false, maxLength = 8
    @Id
    @Column("EMP_NO")
    private String empNo;

    // description = "직업코드", nullable = true, maxLength = 6
    @Column("JOB_CD")
    private String jobCd;

    // description = "직무명", nullable = true, maxLength = 75
    @Column("CONTENT")
    private String content;

    // description = "수정일", nullable = true, maxLength = 8
    @Column("REPL_DT")
    private String replDt;

    // description = "직원구분코드", nullable = true, maxLength = 1
    @Column("PERSG")
    private String persg;

    // description = "본부코드", nullable = true, maxLength = 6
    @Column("ZBIZUNIT_CD")
    private String zbizunitCd;

    // description = "결재자구사원번호", nullable = true, maxLength = 9
    @Column("ZMANAGER_NO")
    private String zmanagerNo;

    // description = "ICIS사원번호", nullable = true, maxLength = 9
    @Column("ICIS_EMP_NO")
    private String icisEmpNo;

    // description = "등록일시", example = "YYYYMMDDHHMISS", nullable = true
    @Column("REG_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp regDate;

    // description = "변경일시", example = "YYYYMMDDHHMISS", nullable = true
    @Column("CHNG_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp chngDate;

    // description = "EAI시퀀스", nullable = true, maxLength = 22
    @Column("EAI_SEQ")
    private BigDecimal eaiSeq;

    // description = "OLD사번", nullable = true, maxLength = 9
    @Column("EMP_NO_OLD")
    private String empNoOld;

    // description = "주민등록번호", nullable = true, maxLength = 13
    @Column("SOCIAL_NO")
    private String socialNo;

    // description = "업체코드", nullable = true, maxLength = 10
    @Column("COMPANY_CD")
    private String companyCd;

    // description = "사원명", nullable = true, maxLength = 120
    @Column("EMP_NM")
    private String empNm;

    // description = "상태코드", nullable = true, maxLength = 1
    @Column("STATUS_CD")
    private String statusCd;

    // description = "부서코드", nullable = true, maxLength = 6
    @Column("DEPT_CD")
    private String deptCd;

    // description = "비편제부서코드", nullable = true, maxLength = 6
    @Column("NO_FORM_CD")
    private String noFormCd;

    // description = "회사전화번호", nullable = true, maxLength = 15
    @Column("NOR_TEL_NO")
    private String norTelNo;

    // description = "이메일", nullable = true, maxLength = 90
    @Column("MAILID")
    private String mailid;

    // description = "핸드폰번호", nullable = true, maxLength = 30
    @Column("HANDPHONE_NO")
    private String handphoneNo;

    @Transient
    public boolean isNew = false;

    @Override
    public String getId() {
        return this.getEmpNo();
    }

    public static BtMsyserpempinfoEntt of(BtMsyserpempinfoDto dto) {
        return BtMsyserpempinfoEntt.builder()
                .empNo(dto.getEmpNo())
                .jobCd(dto.getJobCd())
                .content(dto.getContent())
                .replDt(dto.getReplDt())
                .persg(dto.getPersg())
                .zbizunitCd(dto.getZbizunitCd())
                .zmanagerNo(dto.getZmanagerNo())
                .icisEmpNo(dto.getIcisEmpNo())
                .regDate(dto.getRegDate())
                .chngDate(dto.getChngDate())
                .eaiSeq(dto.getEaiSeq())
                .empNoOld(dto.getEmpNoOld())
                .socialNo(dto.getSocialNo())
                .companyCd(dto.getCompanyCd())
                .empNm(dto.getEmpNm())
                .statusCd(dto.getStatusCd())
                .deptCd(dto.getDeptCd())
                .noFormCd(dto.getNoFormCd())
                .norTelNo(dto.getNorTelNo())
                .mailid(dto.getMailid())
                .handphoneNo(dto.getHandphoneNo())
                .build();
    }
}