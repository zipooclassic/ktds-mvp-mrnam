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
 * 조직정보테이블(MDM) db table dto
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Schema(description = "조직정보테이블(MDM)")
public class BtMsysorgmasterDto implements Serializable {
	private static final long serialVersionUID = 1687585983705075613L;
	
	@Schema(description = "조직코드", nullable = false, maxLength = 6)
    private String orgcode;
    
	@Schema(description = "상위조직코드", nullable = true, maxLength = 6)
    private String parentorgcode;
    
	@Schema(description = "조직명", nullable = true, maxLength = 80)
    private String orgname;
    
	@Schema(description = "조직영문명", nullable = true, maxLength = 60)
    private String orgengname;
    
	@Schema(description = "분장업무", nullable = true, maxLength = 4000)
    private String divisionwork;
    
	@Schema(description = "우편번호", nullable = true, maxLength = 6)
    private String zipid;
    
	@Schema(description = "우편번호", nullable = true, maxLength = 10)
    private String postalcd;
    
	@Schema(description = "주소1", nullable = true, maxLength = 128)
    private String addr1;
    
	@Schema(description = "주소2", nullable = true, maxLength = 128)
    private String addr2;
    
	@Schema(description = "사업장구분코드", nullable = true, maxLength = 4)
    private String businessgb;
    
	@Schema(description = "조직특성구분코드", nullable = true, maxLength = 4)
    private String orgtype;
    
	@Schema(description = "급여지급기관여부", nullable = true, maxLength = 1)
    private String ispayorg;
    
	@Schema(description = "전화번호", nullable = true, maxLength = 25)
    private String telno;
    
	@Schema(description = "팩스번호", nullable = true, maxLength = 25)
    private String faxno;
    
	@Schema(description = "수익센터여부", nullable = true, maxLength = 1)
    private String isprofitcenter;
    
	@Schema(description = "수익센터", nullable = true, maxLength = 10)
    private String profitcenter;
    
	@Schema(description = "비용센터여부", nullable = true, maxLength = 1)
    private String iscostcenter;
    
	@Schema(description = "비용센터", nullable = true, maxLength = 10)
    private String costcenter;
    
	@Schema(description = "성과측정대상여부Y:YesN:No", nullable = true, maxLength = 1)
    private String isevaluate;
    
	@Schema(description = "본부조직코드", nullable = true, maxLength = 6)
    private String headcode;
    
	@Schema(description = "조직레벨", nullable = true, maxLength = 4)
    private String orglevel;
    
	@Schema(description = "순위", nullable = true, maxLength = 2)
    private String orgorder;
    
	@Schema(description = "편제순위", nullable = true, maxLength = 12)
    private String formorder;
    
	@Schema(description = "최하위조직여부", nullable = true, maxLength = 1)
    private String isleaf;
    
	@Schema(description = "생성일자", nullable = true, maxLength = 8)
    private Timestamp creationdtime;
    
	@Schema(description = "변경일자", nullable = true, maxLength = 8)
    private Timestamp updatedtime;
    
	@Schema(description = "활성여부", nullable = true, maxLength = 1)
    private String active;
    
	@Schema(description = "부서장사번", nullable = true, maxLength = 10)
    private String capempno;
    
	@Schema(description = "조직급수", nullable = true, maxLength = 3)
    private String orggrade;
    
	@Schema(description = "최종변경자사번", nullable = true, maxLength = 27)
    private String lastupdator;
    
	@Schema(description = "기관약명", nullable = true, maxLength = 36)
    private String orgsymname;
    
	@Schema(description = "등록일시", nullable = true, maxLength = 8)
    private Timestamp regDate;
    
	@Schema(description = "변경일시", nullable = true, maxLength = 8)
    private Timestamp chngDate;
    
	@Schema(description = "EAI일련번호", nullable = true, maxLength = 22)
    private BigDecimal eaiSeq;
    
}