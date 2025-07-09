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
import com.kt.icis.oder.baseinfo.common.repository.dto.BtMsysofficecodeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 국사정보테이블(MDM) db table entity
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Table("BI_MSYSOFFICECODE")
public class BtMsysofficecodeEntt implements Persistable<String> {
	
    // description = "국사코드", nullable = false, maxLength = 6
	@Id
	@Column("OFFICECODE")	
    private String officecode;
    
    // description = "관리조직(기타)", nullable = true, maxLength = 20
	@Column("ORGCODEETC")	
    private String orgcodeetc;
    
    // description = "관리조직(기타)명칭", nullable = true, maxLength = 200
	@Column("ORGCODEETC_NAME")	
    private String orgcodeetcName;
    
    // description = "위치코드", nullable = true, maxLength = 11
	@Column("LOCCODE")	
    private String loccode;
    
    // description = "국사유형코드", nullable = true, maxLength = 10
	@Column("OFFICETYPE")	
    private String officetype;
    
    // description = "국사유형코드명칭", nullable = true, maxLength = 100
	@Column("OFFICETYPE_NAME")	
    private String officetypeName;
    
    // description = "법정동코드", nullable = true, maxLength = 10
	@Column("DONGCODE")	
    private String dongcode;
    
    // description = "법정동명칭", nullable = true, maxLength = 200
	@Column("DONGCODE_NAME")	
    private String dongcodeName;
    
    // description = "주소유형코드", nullable = true, maxLength = 2
	@Column("ADDRTYPE")	
    private String addrtype;
    
    // description = "주소유형명", nullable = true, maxLength = 20
	@Column("ADDRTYPENAME")	
    private String addrtypename;
    
    // description = "번지", nullable = true, maxLength = 22
	@Column("BUNJI")	
    private BigDecimal bunji;
    
    // description = "국사명", nullable = true, maxLength = 50
	@Column("OFFICENAME")	
    private String officename;
    
    // description = "호", nullable = true, maxLength = 22
	@Column("HO")	
    private BigDecimal ho;
    
    // description = "비고", nullable = true, maxLength = 1000
	@Column("NOTE")	
    private String note;
    
    // description = "닉네임", nullable = true, maxLength = 200
	@Column("OFFICENICK")	
    private String officenick;
    
    // description = "상세주소", nullable = true, maxLength = 200
	@Column("DETAILADDR")	
    private String detailaddr;
    
    // description = "생성일시", example = "YYYYMMDDHHMISS", nullable = true
	@Column("CREATIONDTIME")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")	
    private Timestamp creationdtime;
    
    // description = "변경일시", example = "YYYYMMDDHHMISS", nullable = true
	@Column("LASTMODIFICATIONDTIME")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")	
    private Timestamp lastmodificationdtime;
    
    // description = "최종변경자", nullable = true, maxLength = 20
	@Column("LASTMODIFICATIONUSRID")	
    private String lastmodificationusrid;
    
    // description = "상태USE:사용STOP:사용중지", nullable = true, maxLength = 4
	@Column("STATUS")	
    private String status;
    
    // description = "활성여부", nullable = true, maxLength = 1
	@Column("ACTIVE")	
    private String active;
    
    // description = "등록일시", example = "YYYYMMDDHHMISS", nullable = true
	@Column("REG_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")	
    private Timestamp regDate;
    
    // description = "상위국사코드", nullable = true, maxLength = 6
	@Column("PARENTCODEID")	
    private String parentcodeid;
    
    // description = "변경일시", example = "YYYYMMDDHHMISS", nullable = true
	@Column("CHNG_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")	
    private Timestamp chngDate;
    
    // description = "EAI시퀀스", nullable = true, maxLength = 22
	@Column("EAI_SEQ")	
    private BigDecimal eaiSeq;
    
    // description = "본부코드", nullable = true, maxLength = 20
	@Column("ORGCODEBU")	
    private String orgcodebu;
    
    // description = "본부코드명칭", nullable = true, maxLength = 200
	@Column("ORGCODEBU_NAME")	
    private String orgcodebuName;
    
    // description = "관리조직(지점)", nullable = true, maxLength = 20
	@Column("ORGCODEBR")	
    private String orgcodebr;
    
    // description = "관리조직(지점)명칭", nullable = true, maxLength = 200
	@Column("ORGCODEBR_NAME")	
    private String orgcodebrName;
    
    // description = "관리조직(네트워크서비스센터)", nullable = true, maxLength = 20
	@Column("ORGCODENW")	
    private String orgcodenw;
    
    // description = "관리조직(네트워크서비스센터)명칭", nullable = true, maxLength = 200
	@Column("ORGCODENW_NAME")	
    private String orgcodenwName;
    
	@Override
	public boolean isNew() {
		return false;
	}
	
	@Override
	public String getId() {
		return this.getOfficecode();
	}
	
	public static BtMsysofficecodeEntt of(BtMsysofficecodeDto dto) {
		return BtMsysofficecodeEntt.builder()
			.officecode(dto.getOfficecode())
			.orgcodeetc(dto.getOrgcodeetc())
			.orgcodeetcName(dto.getOrgcodeetcName())
			.loccode(dto.getLoccode())
			.officetype(dto.getOfficetype())
			.officetypeName(dto.getOfficetypeName())
			.dongcode(dto.getDongcode())
			.dongcodeName(dto.getDongcodeName())
			.addrtype(dto.getAddrtype())
			.addrtypename(dto.getAddrtypename())
			.bunji(dto.getBunji())
			.officename(dto.getOfficename())
			.ho(dto.getHo())
			.note(dto.getNote())
			.officenick(dto.getOfficenick())
			.detailaddr(dto.getDetailaddr())
			.creationdtime(dto.getCreationdtime())
			.lastmodificationdtime(dto.getLastmodificationdtime())
			.lastmodificationusrid(dto.getLastmodificationusrid())
			.status(dto.getStatus())
			.active(dto.getActive())
			.regDate(dto.getRegDate())
			.parentcodeid(dto.getParentcodeid())
			.chngDate(dto.getChngDate())
			.eaiSeq(dto.getEaiSeq())
			.orgcodebu(dto.getOrgcodebu())
			.orgcodebuName(dto.getOrgcodebuName())
			.orgcodebr(dto.getOrgcodebr())
			.orgcodebrName(dto.getOrgcodebrName())
			.orgcodenw(dto.getOrgcodenw())
			.orgcodenwName(dto.getOrgcodenwName())
			.build();
		}
}