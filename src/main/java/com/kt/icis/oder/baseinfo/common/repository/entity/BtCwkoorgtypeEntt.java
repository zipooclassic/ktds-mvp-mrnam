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
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCwkoorgtypeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 조직특성구분코드 변동정보수신테이블 db table entity
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Table("BI_CWKOORGTYPE")
public class BtCwkoorgtypeEntt implements Persistable<BigDecimal> {
	
    // description = "조직특성구분코드", nullable = true, maxLength = 4
	@Column("ORGTYPE")	
    private String orgtype;
    
    // description = "작업구분코드", nullable = true, maxLength = 1
	@Column("EAI_OP")	
    private String eaiOp;
    
    // description = "전송요청시간", nullable = true, maxLength = 17
	@Column("EAI_CDATE")	
    private String eaiCdate;
    
    // description = "전송완료시간", nullable = true, maxLength = 17
	@Column("EAI_UDATE")	
    private String eaiUdate;
    
    // description = "응답메세지", nullable = true, maxLength = 256
	@Column("EAI_MSG")	
    private String eaiMsg;
    
    // description = "ICIS처리여부", nullable = true, maxLength = 1
	@Column("WO_FLAG")	
    private String woFlag;
    
    // description = "작업완료시간", example = "YYYYMMDDHHMISS", nullable = true
	@Column("WO_TIME")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")	
    private Timestamp woTime;
    
    // description = "작업결과에러내용", nullable = true, maxLength = 300
	@Column("WO_ERR_MSG")	
    private String woErrMsg;
    
    // description = "사업장구분코드", nullable = true, maxLength = 4
	@Column("BUSINESSGB")	
    private String businessgb;
    
    // description = "조직특성구분코드", nullable = true, maxLength = 45
	@Column("ORGTYPENAME")	
    private String orgtypename;
    
    // description = "표시순서", nullable = true, maxLength = 22
	@Column("DISSEQ")	
    private BigDecimal disseq;
    
    // description = "활성여부", nullable = true, maxLength = 1
	@Column("ACTIVE")	
    private String active;
    
    // description = "생성일자", example = "YYYYMMDDHHMISS", nullable = true
	@Column("CREATIONDTIME")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")	
    private Timestamp creationdtime;
    
    // description = "변경일자", example = "YYYYMMDDHHMISS", nullable = true
	@Column("UPDATEDTIME")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")	
    private Timestamp updatedtime;
    
    // description = "순번uence", nullable = false, maxLength = 22
	@Id
	@Column("EAI_SEQ")	
    private BigDecimal eaiSeq;
    
    // description = "송수신상태", nullable = true, maxLength = 1
	@Column("EAI_STATE")	
    private String eaiState;
    
	@Override
	public boolean isNew() {
		return false;
	}
	
	@Override
	public BigDecimal getId() {
		return this.getEaiSeq();
	}
	
	public static BtCwkoorgtypeEntt of(BtCwkoorgtypeDto dto) {
		return BtCwkoorgtypeEntt.builder()
			.orgtype(dto.getOrgtype())
			.eaiOp(dto.getEaiOp())
			.eaiCdate(dto.getEaiCdate())
			.eaiUdate(dto.getEaiUdate())
			.eaiMsg(dto.getEaiMsg())
			.woFlag(dto.getWoFlag())
			.woTime(dto.getWoTime())
			.woErrMsg(dto.getWoErrMsg())
			.businessgb(dto.getBusinessgb())
			.orgtypename(dto.getOrgtypename())
			.disseq(dto.getDisseq())
			.active(dto.getActive())
			.creationdtime(dto.getCreationdtime())
			.updatedtime(dto.getUpdatedtime())
			.eaiSeq(dto.getEaiSeq())
			.eaiState(dto.getEaiState())
			.build();
		}
}