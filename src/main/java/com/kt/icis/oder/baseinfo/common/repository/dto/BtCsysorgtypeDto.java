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
 * 조직특성구분코드 테이블 db table dto
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Schema(description = "조직특성구분코드 테이블")
public class BtCsysorgtypeDto implements Serializable {
	private static final long serialVersionUID = 1672049505302373967L;
	
	@Schema(description = "조직특성구분코드", nullable = false, maxLength = 4)
    private String orgtype;
    
	@Schema(description = "등록일시", nullable = true, maxLength = 8)
    private Timestamp regDate;
    
	@Schema(description = "변경일시", nullable = true, maxLength = 8)
    private Timestamp chngDate;
    
	@Schema(description = "EAI시퀀스", nullable = true, maxLength = 22)
    private BigDecimal eaiSeq;
    
	@Schema(description = "사업장구분코드", nullable = true, maxLength = 4)
    private String businessgb;
    
	@Schema(description = "조직특성구분코드", nullable = true, maxLength = 45)
    private String orgtypename;
    
	@Schema(description = "표시순서", nullable = true, maxLength = 22)
    private BigDecimal disseq;
    
	@Schema(description = "활성여부", nullable = true, maxLength = 1)
    private String active;
    
	@Schema(description = "생성일자", nullable = true, maxLength = 8)
    private Timestamp creationdtime;
    
	@Schema(description = "변경일자", nullable = true, maxLength = 8)
    private Timestamp updatedtime;
    
	@Schema(description = "생성일자", nullable = true, maxLength = 8)
    private Timestamp creationTime;
    
	@Schema(description = "변경일자", nullable = true, maxLength = 8)
    private Timestamp updatedTime;
    
}