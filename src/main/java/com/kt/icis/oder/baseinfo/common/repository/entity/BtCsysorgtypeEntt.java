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
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCsysorgtypeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 조직특성구분코드 테이블 db table entity
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Table("BI_CSYSORGTYPE")
public class BtCsysorgtypeEntt implements Persistable<String> {
	
    // description = "조직특성구분코드", nullable = false, maxLength = 4
	@Id
	@Column("ORGTYPE")	
    private String orgtype;
    
    // description = "등록일시", example = "YYYYMMDDHHMISS", nullable = true
	@Column("REG_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")	
    private Timestamp regDate;
    
    // description = "변경일시", example = "YYYYMMDDHHMISS", nullable = true
	@Column("CHNG_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")	
    private Timestamp chngDate;
    
    // description = "EAI시퀀스", nullable = true, maxLength = 22
	@Column("EAI_SEQ")	
    private BigDecimal eaiSeq;
    
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
    
    // description = "생성일자", example = "YYYYMMDDHHMISS", nullable = true
	@Column("CREATION_TIME")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")	
    private Timestamp creationTime;
    
    // description = "변경일자", example = "YYYYMMDDHHMISS", nullable = true
	@Column("UPDATED_TIME")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")	
    private Timestamp updatedTime;
    
	@Override
	public boolean isNew() {
		return false;
	}
	
	@Override
	public String getId() {
		return this.getOrgtype();
	}
	
	public static BtCsysorgtypeEntt of(BtCsysorgtypeDto dto) {
		
		return BtCsysorgtypeEntt.builder()
			.orgtype(dto.getOrgtype())
			.regDate(dto.getRegDate())
			.chngDate(dto.getChngDate())
			.eaiSeq(dto.getEaiSeq())
			.businessgb(dto.getBusinessgb())
			.orgtypename(dto.getOrgtypename())
			.disseq(dto.getDisseq())
			.active(dto.getActive())
			.creationdtime(dto.getCreationdtime())
			.updatedtime(dto.getUpdatedtime())
			.creationTime(dto.getCreationTime())
			//.updatedTime(dto.u)
			.build();
		}
}