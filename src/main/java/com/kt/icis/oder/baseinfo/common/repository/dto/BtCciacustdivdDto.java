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
 * 계약고객분리 연동 db table dto
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Schema(description = "계약고객분리 연동")
public class BtCciacustdivdDto implements Serializable {
	private static final long serialVersionUID = 1672049508204675857L;
	
	@Schema(description = "서비스계약아이디", nullable = false, maxLength = 11)
    private String saId;
    
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
    
	@Schema(description = "고객아이디", nullable = false, maxLength = 11)
    private String custId;
    
	@Schema(description = "IPS처리시간", nullable = true, maxLength = 8)
    private Timestamp ipsProcDate;
    
	@Schema(description = "IPS처리여부", nullable = true, maxLength = 1)
    private String ipsProcResult;
    
	@Schema(description = "IF_HST_NO", nullable = true, maxLength = 22)
    private BigDecimal ifHstNo;
    
	@Schema(description = "발생일자", nullable = false, maxLength = 14)
    private String occurDate;
    
	@Schema(description = "분리후고객아이디", nullable = true, maxLength = 11)
    private String newCustId;
    
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
    
}