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

import java.sql.Timestamp;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCcpiprodhierDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WC_CBOAUTOSTOP db table entity
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Table("BI_CCIA_CUST_DIVD")
public class BtCcpiprodhierEntt implements Persistable<String> {
	
    // description = "서비스계약아이디", nullable = false, maxLength = 11
	@Id
	@Column("SA_ID")	
    private String saId;
    
    // description = "시작일자", nullable = true, maxLength = 8
	@Column("START_DATE")	
    private String startDate;
    
    // description = "종료일자", nullable = true, maxLength = 8
	@Column("END_DATE")	
    private String endDate;
    
    // description = "테이블아이디", nullable = false, maxLength = 30
	@Column("TABLE_ID")	
    private String tableId;
    
    // description = "상품유형코드", nullable = true, maxLength = 1
	@Column("PROD_TYPE_CD")	
    private String prodTypeCd;
    
    // description = "서비스계약코드", nullable = false, maxLength = 4
	@Column("SA_CD")	
    private String saCd;
    
    // description = "서비스계약상세코드", nullable = false, maxLength = 4
	@Column("SA_DTL_CD")	
    private String saDtlCd;
    
    // description = "상위노드유형코드", nullable = false, maxLength = 1
	@Column("P_NODE_TYPE")	
    private String pNodeType;
    
    // description = "상위노드아이디", nullable = false, maxLength = 11
	@Column("P_NODE_ID")	
    private String pNodeId;
    
    // description = "최상위서비스계약아이디", nullable = true, maxLength = 11
	@Column("TOP_SA_ID")	
    private String topSaId;
    
    // description = "청구계정노드유형코드", nullable = true, maxLength = 1
	@Column("IA_NODE_TYPE")	
    private String iaNodeType;
    
    // description = "청구계정노드아이디", nullable = true, maxLength = 12
	@Column("IA_NODE_ID")	
    private String iaNodeId;
    
    // description = "청구서비스계약아이디유형코드", nullable = true, maxLength = 1
	@Column("BILL_SA_ID_TYPE")	
    private String billSaIdType;
    
    // description = "최초시작일자", nullable = true, maxLength = 8
	@Column("INIT_START_DATE")	
    private String initStartDate;
    
    // description = "데이터이관아이디", nullable = true, maxLength = 5
	@Column("MIG_ID")	
    private String migId;
    
    // description = "데이터이관일시", example = "YYYYMMDDHHMISS", nullable = true
	@Column("MIG_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")	
    private Timestamp migDate;
    
    // description = "사용자고객아이디", nullable = true, maxLength = 11
	@Column("USER_CUST_ID")	
    private String userCustId;
    
    // description = "최조신규접수일자", nullable = true, maxLength = 8
	@Column("INIT_RCV_DATE")	
    private String initRcvDate;
    
	@Override
	public boolean isNew() {
		return false;
	}
	
	@Override
	public String getId() {
		return this.getSaId();
	}
	
	public static BtCcpiprodhierEntt of(BtCcpiprodhierDto dto) {
		return BtCcpiprodhierEntt.builder()
			.saId(dto.getSaId())
			.startDate(dto.getStartDate())
			.endDate(dto.getEndDate())
			.tableId(dto.getTableId())
			.prodTypeCd(dto.getProdTypeCd())
			.saCd(dto.getSaCd())
			.saDtlCd(dto.getSaDtlCd())
			.pNodeType(dto.getPNodeType())
			.pNodeId(dto.getPNodeId())
			.topSaId(dto.getTopSaId())
			.iaNodeType(dto.getIaNodeType())
			.iaNodeId(dto.getIaNodeId())
			.billSaIdType(dto.getBillSaIdType())
			.initStartDate(dto.getInitStartDate())
			.migId(dto.getMigId())
			.migDate(dto.getMigDate())
			.userCustId(dto.getUserCustId())
			.initRcvDate(dto.getInitRcvDate())
			.build();
		}}