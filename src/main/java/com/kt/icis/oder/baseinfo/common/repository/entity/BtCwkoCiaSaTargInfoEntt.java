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
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCwkociasatarginfoCsnh625dDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 서비스계약별 고객정보 현행화 처리대상 db table entity
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Table("WC_CWKO_CIA_SA_TARG_INFO")
public class BtCwkoCiaSaTargInfoEntt implements Persistable<String> {

    // description = "CUST_PROC_TYPE", nullable = false, maxLength = 1
    //@Id 복합키 사용불가
    @Column("CUST_PROC_TYPE")
    private String custProcType;

    // description = "고객아이디", nullable = false, maxLength = 11
    //@Id 복합키 사용불가
    @Column("CUST_ID")
    private String custId;

    // description = "발생일자", nullable = false, maxLength = 14
    //@Id 복합키 사용불가
    @Column("OCCUR_DATE")
    private String occurDate;

    // description = "서비스계약아이디", nullable = false, maxLength = 11
    //@Id 복합키 사용불가
    @Column("SA_ID")
    private String saId;

    // description = "서비스계약코드", nullable = true, maxLength = 4
    @Column("SA_CD")
    private String saCd;

    // description = "접수일", example = "YYYYMMDDHHMISS", nullable = true
    @Column("REG_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp regDate;

    // description = "REG_MODULE_ID", nullable = true, maxLength = 20
    @Column("REG_MODULE_ID")
    private String regModuleId;

    // description = "오더번호", nullable = true, maxLength = 15
    @Column("ORD_NO")
    private String ordNo;

    // description = "상태코드", nullable = true, maxLength = 1
    @Column("STATUS_CD")
    private String statusCd;

    // description = "결과코드", nullable = true, maxLength = 4
    @Column("RESULT_CD")
    private String resultCd;

    // description = "결과메시지", nullable = true, maxLength = 1000
    @Column("RESULT_MSG")
    private String resultMsg;

    // description = "처리일시", example = "YYYYMMDDHHMISS", nullable = true
    @Column("PROC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp procDate;

    // description = "재전송횟수", nullable = true, maxLength = 22
    @Column("RETRY_CNT")
    private BigDecimal retryCnt;

    // description = "처리예약일자", example = "YYYYMMDDHHMISS", nullable = true
    @Column("PROC_RESV_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp procResvDate;

    // description = "COMB_RESULT_CD", nullable = true, maxLength = 4
    @Column("COMB_RESULT_CD")
    private String combResultCd;

    // description = "COMB_RESULT_MSG", nullable = true, maxLength = 1000
    @Column("COMB_RESULT_MSG")
    private String combResultMsg;

    // description = "COMB_PROC_DATE", example = "YYYYMMDDHHMISS", nullable = true
    @Column("COMB_PROC_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp combProcDate;

    // description = "결합서비스계약아이디", nullable = true, maxLength = 11
    @Column("COMB_SA_ID")
    private String combSaId;

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

    public static BtCwkoCiaSaTargInfoEntt of(BtCwkociasatarginfoCsnh625dDto dto) {
        return BtCwkoCiaSaTargInfoEntt.builder()
                .custProcType(dto.getCustProcType())
                .custId(dto.getCustId())
                .occurDate(dto.getOccurDate())
                .saId(dto.getSaId())
                .saCd(dto.getSaCd())
                .regDate(dto.getRegDate())
                .regModuleId(dto.getRegModuleId())
                .ordNo(dto.getOrdNo())
                .statusCd(dto.getStatusCd())
                .resultCd(dto.getResultCd())
                .resultMsg(dto.getResultMsg())
                .procDate(dto.getProcDate())
                .retryCnt(dto.getRetryCnt())
                .procResvDate(dto.getProcResvDate())
                .combResultCd(dto.getCombResultCd())
                .combResultMsg(dto.getCombResultMsg())
                .combProcDate(dto.getCombProcDate())
                .combSaId(dto.getCombSaId())
                .build();
    }
}