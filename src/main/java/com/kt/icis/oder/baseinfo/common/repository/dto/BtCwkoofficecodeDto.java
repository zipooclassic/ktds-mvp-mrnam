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
 * 국사정보변동정보수신테이블 db table dto
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Schema(description = "국사정보변동정보수신테이블")
public class BtCwkoofficecodeDto implements Serializable {
	private static final long serialVersionUID = 1672049504769906872L;
	
	@Schema(description = "국사코드", nullable = true, maxLength = 6)
    private String officecode;
    
	@Schema(description = "관리조직(기타)", nullable = true, maxLength = 20)
    private String orgcodeetc;
    
	@Schema(description = "관리조직(기타)명칭", nullable = true, maxLength = 200)
    private String orgcodeetcName;
    
	@Schema(description = "위치코드", nullable = true, maxLength = 11)
    private String loccode;
    
	@Schema(description = "국사유형코드", nullable = true, maxLength = 10)
    private String officetype;
    
	@Schema(description = "국사유형코드명칭", nullable = true, maxLength = 100)
    private String officetypeName;
    
	@Schema(description = "법정동코드", nullable = true, maxLength = 10)
    private String dongcode;
    
	@Schema(description = "법정동명칭", nullable = true, maxLength = 200)
    private String dongcodeName;
    
	@Schema(description = "주소유형코드", nullable = true, maxLength = 2)
    private String addrtype;
    
	@Schema(description = "주소유형명", nullable = true, maxLength = 20)
    private String addrtypename;
    
	@Schema(description = "번지", nullable = true, maxLength = 22)
    private BigDecimal bunji;
    
	@Schema(description = "국사명", nullable = true, maxLength = 50)
    private String officename;
    
	@Schema(description = "호", nullable = true, maxLength = 22)
    private BigDecimal ho;
    
	@Schema(description = "비고", nullable = true, maxLength = 1000)
    private String note;
    
	@Schema(description = "닉네임", nullable = true, maxLength = 200)
    private String officenick;
    
	@Schema(description = "상세주소", nullable = true, maxLength = 200)
    private String detailaddr;
    
	@Schema(description = "생성일시", nullable = true, maxLength = 8)
    private Timestamp creationdtime;
    
	@Schema(description = "변경일시", nullable = true, maxLength = 8)
    private Timestamp lastmodificationdtime;
    
	@Schema(description = "최종변경자", nullable = true, maxLength = 20)
    private String lastmodificationusrid;
    
	@Schema(description = "상태USE:사용STOP:사용중지", nullable = true, maxLength = 4)
    private String status;
    
	@Schema(description = "활성여부", nullable = true, maxLength = 1)
    private String active;
    
	@Schema(description = "순번uence", nullable = false, maxLength = 22)
    private BigDecimal eaiSeq;
    
	@Schema(description = "상위국사코드", nullable = true, maxLength = 6)
    private String parentcodeid;
    
	@Schema(description = "송신상태", nullable = true, maxLength = 1)
    private String eaiState;
    
	@Schema(description = "작업구분코드", nullable = true, maxLength = 1)
    private String eaiOp;
    
	@Schema(description = "전송요청시간", nullable = true, maxLength = 17)
    private String eaiCdate;
    
	@Schema(description = "전송완료시간", nullable = true, maxLength = 17)
    private String eaiUdate;
    
	@Schema(description = "응답메시지", nullable = true, maxLength = 256)
    private String eaiMsg;
    
	@Schema(description = "EAI시스템코드", nullable = true, maxLength = 20)
    private String eaiSystemCd;
    
	@Schema(description = "ICIS처리여부", nullable = true, maxLength = 1)
    private String woFlag;
    
	@Schema(description = "작업완료시간", nullable = true, maxLength = 8)
    private Timestamp woTime;
    
	@Schema(description = "작업결과에러내용", nullable = true, maxLength = 300)
    private String woErrMsg;
    
	@Schema(description = "본부코드", nullable = true, maxLength = 20)
    private String orgcodebu;
    
	@Schema(description = "본부코드명칭", nullable = true, maxLength = 200)
    private String orgcodebuName;
    
	@Schema(description = "관리조직(지점)", nullable = true, maxLength = 20)
    private String orgcodebr;
    
	@Schema(description = "관리조직(지점)명칭", nullable = true, maxLength = 200)
    private String orgcodebrName;
    
	@Schema(description = "관리조직(네트워크서비스센터)", nullable = true, maxLength = 20)
    private String orgcodenw;
    
	@Schema(description = "관리조직(네트워크서비스센터)명칭", nullable = true, maxLength = 200)
    private String orgcodenwName;
	
	private String rowId;
    
}