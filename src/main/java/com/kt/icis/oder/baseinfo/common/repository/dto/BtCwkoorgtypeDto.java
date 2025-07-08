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
 * 조직특성구분코드 변동정보수신테이블 db table dto
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Schema(description = "조직특성구분코드 변동정보수신테이블")
public class BtCwkoorgtypeDto implements Serializable {
	private static final long serialVersionUID = 1672049510389457096L;
	
	@Schema(description = "조직특성구분코드", nullable = true, maxLength = 4)
    private String orgtype;
    
	@Schema(description = "작업구분코드", nullable = true, maxLength = 1)
    private String eaiOp;
    
	@Schema(description = "전송요청시간", nullable = true, maxLength = 17)
    private String eaiCdate;
    
	@Schema(description = "전송완료시간", nullable = true, maxLength = 17)
    private String eaiUdate;
    
	@Schema(description = "응답메세지", nullable = true, maxLength = 256)
    private String eaiMsg;
    
	@Schema(description = "ICIS처리여부", nullable = true, maxLength = 1)
    private String woFlag;
    
	@Schema(description = "작업완료시간", nullable = true, maxLength = 8)
    private Timestamp woTime;
    
	@Schema(description = "작업결과에러내용", nullable = true, maxLength = 300)
    private String woErrMsg;
    
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
    
	@Schema(description = "순번uence", nullable = false, maxLength = 22)
    private BigDecimal eaiSeq;
    
	@Schema(description = "송수신상태", nullable = true, maxLength = 1)
    private String eaiState;
	
	private String rowId;
    
}