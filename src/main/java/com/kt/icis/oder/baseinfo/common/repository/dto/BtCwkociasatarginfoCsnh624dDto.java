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
 * 서비스계약별 고객정보 현행화 처리대상 db table dto
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Schema(description = "서비스계약별 고객정보 현행화 처리대상")
public class BtCwkociasatarginfoCsnh624dDto implements Serializable {
	private static final long serialVersionUID = 1691623276179305002L;
	
	@Schema(description = "CUST_PROC_TYPE", nullable = false, maxLength = 1)
    private String custProcType;
    
	@Schema(description = "고객아이디", nullable = false, maxLength = 11)
    private String custId;
    
	@Schema(description = "발생일자", nullable = false, maxLength = 14)
    private String occurDate;
    
	@Schema(description = "서비스계약아이디", nullable = false, maxLength = 11)
    private String saId;
    
	@Schema(description = "서비스계약코드", nullable = true, maxLength = 4)
    private String saCd;
    
	@Schema(description = "접수일", nullable = true, maxLength = 8)
    private Timestamp regDate;
    
	@Schema(description = "REG_MODULE_ID", nullable = true, maxLength = 20)
    private String regModuleId;
    
	@Schema(description = "오더번호", nullable = true, maxLength = 15)
    private String ordNo;
    
	@Schema(description = "상태코드", nullable = true, maxLength = 1)
    private String statusCd;
    
	@Schema(description = "결과코드", nullable = true, maxLength = 4)
    private String resultCd;
    
	@Schema(description = "결과메시지", nullable = true, maxLength = 1000)
    private String resultMsg;
    
	@Schema(description = "처리일시", nullable = true, maxLength = 8)
    private Timestamp procDate;
    
	@Schema(description = "재전송횟수", nullable = true, maxLength = 22)
    private BigDecimal retryCnt;
    
	@Schema(description = "처리예약일자", nullable = true, maxLength = 8)
    private Timestamp procResvDate;
    
	@Schema(description = "COMB_RESULT_CD", nullable = true, maxLength = 4)
    private String combResultCd;
    
	@Schema(description = "COMB_RESULT_MSG", nullable = true, maxLength = 1000)
    private String combResultMsg;
    
	@Schema(description = "COMB_PROC_DATE", nullable = true, maxLength = 8)
    private Timestamp combProcDate;
    
	@Schema(description = "결합서비스계약아이디", nullable = true, maxLength = 11)
    private String combSaId;
    
}