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
import java.math.BigDecimal;
import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ERP 사원정보 db table dto
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Schema(description = "ERP 사원정보")
public class BtMsyserpempinfoDto implements Serializable {
	private static final long serialVersionUID = 1672049506295277311L;
	
	@Schema(description = "사번", nullable = false, maxLength = 8)
    private String empNo;
    
	@Schema(description = "직업코드", nullable = true, maxLength = 6)
    private String jobCd;
    
	@Schema(description = "직무명", nullable = true, maxLength = 75)
    private String content;
    
	@Schema(description = "수정일", nullable = true, maxLength = 8)
    private String replDt;
    
	@Schema(description = "직원구분코드", nullable = true, maxLength = 1)
    private String persg;
    
	@Schema(description = "본부코드", nullable = true, maxLength = 6)
    private String zbizunitCd;
    
	@Schema(description = "결재자구사원번호", nullable = true, maxLength = 9)
    private String zmanagerNo;
    
	@Schema(description = "ICIS사원번호", nullable = true, maxLength = 9)
    private String icisEmpNo;
    
	@Schema(description = "등록일시", nullable = true, maxLength = 8)
    private Timestamp regDate;
    
	@Schema(description = "변경일시", nullable = true, maxLength = 8)
    private Timestamp chngDate;
    
	@Schema(description = "EAI시퀀스", nullable = true, maxLength = 22)
    private BigDecimal eaiSeq;
    
	@Schema(description = "OLD사번", nullable = true, maxLength = 9)
    private String empNoOld;
    
	@Schema(description = "주민등록번호", nullable = true, maxLength = 13)
    private String socialNo;
    
	@Schema(description = "업체코드", nullable = true, maxLength = 10)
    private String companyCd;
    
	@Schema(description = "사원명", nullable = true, maxLength = 120)
    private String empNm;
    
	@Schema(description = "상태코드", nullable = true, maxLength = 1)
    private String statusCd;
    
	@Schema(description = "부서코드", nullable = true, maxLength = 6)
    private String deptCd;
    
	@Schema(description = "비편제부서코드", nullable = true, maxLength = 6)
    private String noFormCd;
    
	@Schema(description = "회사전화번호", nullable = true, maxLength = 15)
    private String norTelNo;
    
	@Schema(description = "이메일", nullable = true, maxLength = 90)
    private String mailid;
    
	@Schema(description = "핸드폰번호", nullable = true, maxLength = 30)
    private String handphoneNo;
    
}