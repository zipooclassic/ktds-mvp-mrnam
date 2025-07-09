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
 * CCPIPRODHIER db table dto
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Schema(description = "CCPIPRODHIER")
public class BtCcpiprodhierDto implements Serializable {
    private static final long serialVersionUID = 1678709634848160136L;
	
	@Schema(description = "서비스계약아이디", nullable = false, maxLength = 11)
    private String saId;
    
	@Schema(description = "시작일자", nullable = true, maxLength = 8)
    private String startDate;
    
	@Schema(description = "종료일자", nullable = true, maxLength = 8)
    private String endDate;
    
	@Schema(description = "테이블아이디", nullable = false, maxLength = 30)
    private String tableId;
    
	@Schema(description = "상품유형코드", nullable = true, maxLength = 1)
    private String prodTypeCd;
    
	@Schema(description = "서비스계약코드", nullable = false, maxLength = 4)
    private String saCd;
    
	@Schema(description = "서비스계약상세코드", nullable = false, maxLength = 4)
    private String saDtlCd;
    
	@Schema(description = "상위노드유형코드", nullable = false, maxLength = 1)
    private String pNodeType;
    
	@Schema(description = "상위노드아이디", nullable = false, maxLength = 11)
    private String pNodeId;
    
	@Schema(description = "최상위서비스계약아이디", nullable = true, maxLength = 11)
    private String topSaId;
    
	@Schema(description = "청구계정노드유형코드", nullable = true, maxLength = 1)
    private String iaNodeType;
    
	@Schema(description = "청구계정노드아이디", nullable = true, maxLength = 12)
    private String iaNodeId;
    
	@Schema(description = "청구서비스계약아이디유형코드", nullable = true, maxLength = 1)
    private String billSaIdType;
    
	@Schema(description = "최초시작일자", nullable = true, maxLength = 8)
    private String initStartDate;
    
	@Schema(description = "데이터이관아이디", nullable = true, maxLength = 5)
    private String migId;
    
	@Schema(description = "데이터이관일시", nullable = true, maxLength = 8)
    private Timestamp migDate;
    
	@Schema(description = "사용자고객아이디", nullable = true, maxLength = 11)
    private String userCustId;
    
	@Schema(description = "최조신규접수일자", nullable = true, maxLength = 8)
    private String initRcvDate;

}