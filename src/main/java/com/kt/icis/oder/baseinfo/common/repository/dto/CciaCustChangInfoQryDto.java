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
 * 고객개명/식별번호변경 연동 db table dto
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Schema(description = "고객개명/식별번호변경 연동")
public class CciaCustChangInfoQryDto implements Serializable {
	private static final long serialVersionUID = 1687585982288531155L;
	
	@Schema(description = "고객아이디", nullable = false, maxLength = 11)
    private String custId;
    
	@Schema(description = "발생일시", nullable = false, maxLength = 14)
    private String occurDate;
    
	@Schema(description = "변경유형코드", nullable = true, maxLength = 2)
    private String changTypeCd;
    
	@Schema(description = "고객분류코드", nullable = true, maxLength = 2)
    private String custClCd;
    
	@Schema(description = "고객식별번호유형코드", nullable = true, maxLength = 1)
    private String cusidTypeCd;
    
	@Schema(description = "고객식별번호", nullable = true, maxLength = 20)
    private String custIdfyNo;
    
	@Schema(description = "고객명", nullable = true, maxLength = 50)
    private String custName;
    
	@Schema(description = "고객식별번호유형코드", nullable = true, maxLength = 1)
    private String custNoType;
    
	@Schema(description = "고객식별번호", nullable = true, maxLength = 20)
    private String custNo;
    
	@Schema(description = "신청자명", nullable = true, maxLength = 50)
    private String reqerName;
    
	@Schema(description = "신청자고객번호", nullable = true, maxLength = 20)
    private String reqerCustNo;
    
	@Schema(description = "신청자이동전화번호", nullable = true, maxLength = 20)
    private String reqerPhnNo;
    
	@Schema(description = "신청자이동전화번호", nullable = true, maxLength = 14)
    private String reqerCellphnNo;
    
	@Schema(description = "신청자이메일", nullable = true, maxLength = 30)
    private String reqerEmail;
    
	@Schema(description = "고객관계코드", nullable = true, maxLength = 2)
    private String custRelCd;
    
	@Schema(description = "신청자정보참고사항", nullable = true, maxLength = 100)
    private String reqerRef;
    
	@Schema(description = "처리일시", nullable = true, maxLength = 8)
    private Timestamp procDate;
    
	@Schema(description = "처리결과", nullable = true, maxLength = 1)
    private String procResult;
    
	@Schema(description = "에러메세지", nullable = true, maxLength = 100)
    private String errMsg;
    
	@Schema(description = "등록일시", nullable = true, maxLength = 8)
    private Timestamp regDate;
    
	@Schema(description = "등록국코드", nullable = true, maxLength = 6)
    private String regOfcCd;
    
	@Schema(description = "등록자아이디", nullable = true, maxLength = 9)
    private String regrId;
    
	@Schema(description = "등록자명", nullable = true, maxLength = 50)
    private String regerEmpName;
    
	@Schema(description = "프로그램아이디", nullable = true, maxLength = 10)
    private String pgmId;
    
	@Schema(description = "고객분류변경여부", nullable = true, maxLength = 1)
    private String custClChangYn;
    
	@Schema(description = "고객명변경여부", nullable = true, maxLength = 1)
    private String custNameChangYn;
    
	@Schema(description = "고객식별번호유형변경여부", nullable = true, maxLength = 1)
    private String cusidTypeChangYn;
    
	@Schema(description = "고객식별번호변경여부", nullable = true, maxLength = 1)
    private String custIdfyNoChangYn;
    
	@Schema(description = "고객번호유형변경여부", nullable = true, maxLength = 1)
    private String custNoTypeChangYn;
    
	@Schema(description = "고객번호변경여부", nullable = true, maxLength = 1)
    private String custNoChangYn;
    
	@Schema(description = "변경후고객분류코드", nullable = true, maxLength = 2)
    private String aftCustClCd;
    
	@Schema(description = "AFT_CUSID_TYPE_CD", nullable = true, maxLength = 1)
    private String aftCusidTypeCd;
    
	@Schema(description = "변경후고객식별번호", nullable = true, maxLength = 20)
    private String aftCustIdfyNo;
    
	@Schema(description = "변경후고객명", nullable = true, maxLength = 50)
    private String aftCustName;
    
	@Schema(description = "변경후고객번호유형코드", nullable = true, maxLength = 1)
    private String aftCustNoType;
    
	@Schema(description = "변경후고객번호", nullable = true, maxLength = 20)
    private String aftCustNo;
    
}