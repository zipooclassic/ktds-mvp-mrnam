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
import com.kt.icis.oder.baseinfo.common.repository.dto.BtMsysorgmasterDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 조직정보테이블(MDM) db table entity
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Table("BI_MSYSORGMASTER")
public class BtMsysorgmasterEntt implements Persistable<String> {

    // description = "조직코드", nullable = false, maxLength = 6
    @Id
    @Column("ORGCODE")
    private String orgcode;

    // description = "상위조직코드", nullable = true, maxLength = 6
    @Column("PARENTORGCODE")
    private String parentorgcode;

    // description = "조직명", nullable = true, maxLength = 80
    @Column("ORGNAME")
    private String orgname;

    // description = "조직영문명", nullable = true, maxLength = 60
    @Column("ORGENGNAME")
    private String orgengname;

    // description = "분장업무", nullable = true, maxLength = 4000
    @Column("DIVISIONWORK")
    private String divisionwork;

    // description = "우편번호", nullable = true, maxLength = 6
    @Column("ZIPID")
    private String zipid;

    // description = "우편번호", nullable = true, maxLength = 10
    @Column("POSTALCD")
    private String postalcd;

    // description = "주소1", nullable = true, maxLength = 128
    @Column("ADDR1")
    private String addr1;

    // description = "주소2", nullable = true, maxLength = 128
    @Column("ADDR2")
    private String addr2;

    // description = "사업장구분코드", nullable = true, maxLength = 4
    @Column("BUSINESSGB")
    private String businessgb;

    // description = "조직특성구분코드", nullable = true, maxLength = 4
    @Column("ORGTYPE")
    private String orgtype;

    // description = "급여지급기관여부", nullable = true, maxLength = 1
    @Column("ISPAYORG")
    private String ispayorg;

    // description = "전화번호", nullable = true, maxLength = 25
    @Column("TELNO")
    private String telno;

    // description = "팩스번호", nullable = true, maxLength = 25
    @Column("FAXNO")
    private String faxno;

    // description = "수익센터여부", nullable = true, maxLength = 1
    @Column("ISPROFITCENTER")
    private String isprofitcenter;

    // description = "수익센터", nullable = true, maxLength = 10
    @Column("PROFITCENTER")
    private String profitcenter;

    // description = "비용센터여부", nullable = true, maxLength = 1
    @Column("ISCOSTCENTER")
    private String iscostcenter;

    // description = "비용센터", nullable = true, maxLength = 10
    @Column("COSTCENTER")
    private String costcenter;

    // description = "성과측정대상여부Y:YesN:No", nullable = true, maxLength = 1
    @Column("ISEVALUATE")
    private String isevaluate;

    // description = "본부조직코드", nullable = true, maxLength = 6
    @Column("HEADCODE")
    private String headcode;

    // description = "조직레벨", nullable = true, maxLength = 4
    @Column("ORGLEVEL")
    private String orglevel;

    // description = "순위", nullable = true, maxLength = 2
    @Column("ORGORDER")
    private String orgorder;

    // description = "편제순위", nullable = true, maxLength = 12
    @Column("FORMORDER")
    private String formorder;

    // description = "최하위조직여부", nullable = true, maxLength = 1
    @Column("ISLEAF")
    private String isleaf;

    // description = "생성일자", example = "YYYYMMDDHHMISS", nullable = true
    @Column("CREATIONDTIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmmss", timezone = "Asia/Seoul")
    private Timestamp creationdtime;

    // description = "변경일자", example = "YYYYMMDDHHMISS", nullable = true
    @Column("UPDATEDTIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmmss", timezone = "Asia/Seoul")
    private Timestamp updatedtime;

    // description = "활성여부", nullable = true, maxLength = 1
    @Column("ACTIVE")
    private String active;

    // description = "부서장사번", nullable = true, maxLength = 10
    @Column("CAPEMPNO")
    private String capempno;

    // description = "조직급수", nullable = true, maxLength = 3
    @Column("ORGGRADE")
    private String orggrade;

    // description = "최종변경자사번", nullable = true, maxLength = 27
    @Column("LASTUPDATOR")
    private String lastupdator;

    // description = "기관약명", nullable = true, maxLength = 36
    @Column("ORGSYMNAME")
    private String orgsymname;

    // description = "등록일시", example = "YYYYMMDDHHMISS", nullable = true
    @Column("REG_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp regDate;

    // description = "변경일시", example = "YYYYMMDDHHMISS", nullable = true
    @Column("CHNG_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp chngDate;

    // description = "EAI일련번호", nullable = true, maxLength = 22
    @Column("EAI_SEQ")
    private BigDecimal eaiSeq;

    @Transient
    public boolean isNew = false;

    @Override
    public String getId() {
        return this.getOrgcode();
    }

    public static BtMsysorgmasterEntt of(BtMsysorgmasterDto dto) {
        return BtMsysorgmasterEntt.builder()
                .orgcode(dto.getOrgcode())
                .parentorgcode(dto.getParentorgcode())
                .orgname(dto.getOrgname())
                .orgengname(dto.getOrgengname())
                .divisionwork(dto.getDivisionwork())
                .zipid(dto.getZipid())
                .postalcd(dto.getPostalcd())
                .addr1(dto.getAddr1())
                .addr2(dto.getAddr2())
                .businessgb(dto.getBusinessgb())
                .orgtype(dto.getOrgtype())
                .ispayorg(dto.getIspayorg())
                .telno(dto.getTelno())
                .faxno(dto.getFaxno())
                .isprofitcenter(dto.getIsprofitcenter())
                .profitcenter(dto.getProfitcenter())
                .iscostcenter(dto.getIscostcenter())
                .costcenter(dto.getCostcenter())
                .isevaluate(dto.getIsevaluate())
                .headcode(dto.getHeadcode())
                .orglevel(dto.getOrglevel())
                .orgorder(dto.getOrgorder())
                .formorder(dto.getFormorder())
                .isleaf(dto.getIsleaf())
                .creationdtime(dto.getCreationdtime())
                .updatedtime(dto.getUpdatedtime())
                .active(dto.getActive())
                .capempno(dto.getCapempno())
                .orggrade(dto.getOrggrade())
                .lastupdator(dto.getLastupdator())
                .orgsymname(dto.getOrgsymname())
                .regDate(dto.getRegDate())
                .chngDate(dto.getChngDate())
                .eaiSeq(dto.getEaiSeq())
                .build();
    }
}