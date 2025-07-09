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
 * 고객오더 db table dto
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Schema(description = "고객오더")
public class BtCordordinfoQryDto implements Serializable {
	private static final long serialVersionUID = 1687585994066204590L;
	
	@Schema(description = "오더번호", nullable = false, maxLength = 15)
    private String ordNo;
    
	@Schema(description = "서비스계약아이디", nullable = false, maxLength = 11)
    private String saId;
    
	@Schema(description = "서비스계약코드", nullable = false, maxLength = 4)
    private String saCd;
    
	@Schema(description = "서비스상품유형코드", nullable = true, maxLength = 2)
    private String svcTypeCd;
    
	@Schema(description = "계약유형코드", nullable = true, maxLength = 1)
    private String termCntrctType;
    
	@Schema(description = "KT사업사용여부", nullable = true, maxLength = 1)
    private String ktUseFlag;
    
	@Schema(description = "서비스번호", nullable = true, maxLength = 20)
    private String svcNo;
    
	@Schema(description = "오더관리번호", nullable = true, maxLength = 10)
    private String ordMngNo;
    
	@Schema(description = "서비스국코드", nullable = true, maxLength = 9)
    private String svcOfcCd;
    
	@Schema(description = "교환국코드", nullable = true, maxLength = 6)
    private String exchOfcCd;
    
	@Schema(description = "설치관리국코드", nullable = true, maxLength = 6)
    private String instlMngOfcCd;
    
	@Schema(description = "고객아이디", nullable = true, maxLength = 11)
    private String custId;
    
	@Schema(description = "고객명", nullable = true, maxLength = 50)
    private String custName;
    
	@Schema(description = "고객식별번호유형코드", nullable = true, maxLength = 1)
    private String custNoType;
    
	@Schema(description = "고객식별번호", nullable = true, maxLength = 20)
    private String custNo;
    
	@Schema(description = "사용자오더유형코드", nullable = false, maxLength = 2)
    private String userOrdTypeCd;
    
	@Schema(description = "오더유형코드", nullable = false, maxLength = 2)
    private String ordTypeCd;
    
	@Schema(description = "오더유형코드1", nullable = true, maxLength = 2)
    private String ordTypeCdN1;
    
	@Schema(description = "오더유형코드2", nullable = true, maxLength = 2)
    private String ordTypeCdN2;
    
	@Schema(description = "원오더유형코드", nullable = true, maxLength = 2)
    private String orgOrdTypeCd;
    
	@Schema(description = "오더사유코드", nullable = true, maxLength = 2)
    private String ordRsnCd;
    
	@Schema(description = "오더상태코드", nullable = false, maxLength = 2)
    private String ordStatusCd;
    
	@Schema(description = "전송유형코드", nullable = true, maxLength = 2)
    private String tranTypeCd;
    
	@Schema(description = "신청자명", nullable = true, maxLength = 50)
    private String reqerName;
    
	@Schema(description = "고객관계코드", nullable = true, maxLength = 2)
    private String custRelCd;
    
	@Schema(description = "구성희망일시각", nullable = true, maxLength = 10)
    private String completedResvDateHh;
    
	@Schema(description = "구성완료일자", nullable = true, maxLength = 8)
    private String constDate;
    
	@Schema(description = "완료일시", nullable = true, maxLength = 10)
    private String completedDateHh;
    
	@Schema(description = "공사지연사유코드", nullable = true, maxLength = 4)
    private String workDelayRsnCd;
    
	@Schema(description = "하위국공사지연사유코드", nullable = true, maxLength = 4)
    private String lWorkDelayRsnCd;
    
	@Schema(description = "테스트일수", nullable = true, maxLength = 22)
    private BigDecimal testDdCnt;
    
	@Schema(description = "개통예약일자", nullable = true, maxLength = 8)
    private String constResvDate;
    
	@Schema(description = "상위가설서비스번호", nullable = true, maxLength = 20)
    private String pInstlSvcNo;
    
	@Schema(description = "재사용서비스번호유무", nullable = true, maxLength = 1)
    private String reuseSvcNoFlag;
    
	@Schema(description = "재사용서비스번호", nullable = true, maxLength = 16)
    private String reuseSvcNo;
    
	@Schema(description = "청구지주소변경여부", nullable = true, maxLength = 1)
    private String iaAddrFlag;
    
	@Schema(description = "고객접촉매체코드", nullable = true, maxLength = 2)
    private String cntcMediaCd;
    
	@Schema(description = "연락전화번호", nullable = true, maxLength = 20)
    private String cntcTelNo;
    
	@Schema(description = "연락전화번호1", nullable = true, maxLength = 12)
    private String cntcTelNoN1;
    
	@Schema(description = "연락이메일주소", nullable = true, maxLength = 50)
    private String cntcEmailAddr;
    
	@Schema(description = "연락주소", nullable = true, maxLength = 100)
    private String cntcAddr;
    
	@Schema(description = "참조내용", nullable = true, maxLength = 100)
    private String ref;
    
	@Schema(description = "주소내용", nullable = true, maxLength = 100)
    private String addrText;
    
	@Schema(description = "서비스개수", nullable = true, maxLength = 22)
    private BigDecimal svcCnt;
    
	@Schema(description = "연락자식별번호", nullable = true, maxLength = 13)
    private String cntcCustNo;
    
	@Schema(description = "이용정지서비스계약아이디", nullable = true, maxLength = 11)
    private String pauseSaId;
    
	@Schema(description = "접수국코드", nullable = true, maxLength = 9)
    private String rcvOfcCd;
    
	@Schema(description = "접수일자", nullable = false, maxLength = 8)
    private String rcvDate;
    
	@Schema(description = "접수자사번", nullable = true, maxLength = 9)
    private String rcverEmpNo;
    
	@Schema(description = "접수자명", nullable = true, maxLength = 50)
    private String rcverEmpName;
    
	@Schema(description = "접수자전화번호", nullable = true, maxLength = 12)
    private String rcverEmpTelNo;
    
	@Schema(description = "등록일시", nullable = false, maxLength = 8)
    private Timestamp regDate;
    
	@Schema(description = "구오더번호", nullable = true, maxLength = 20)
    private String legacyOrdNo;
    
	@Schema(description = "무출동코드", nullable = true, maxLength = 1)
    private String reuseSvcCd;
    
	@Schema(description = "완료여부", nullable = false, maxLength = 1)
    private String completeFlag;
    
	@Schema(description = "취소사유코드", nullable = true, maxLength = 2)
    private String cancelRsnCd;
    
	@Schema(description = "취소참조내용", nullable = true, maxLength = 100)
    private String cancelRef;
    
	@Schema(description = "취소등록자명", nullable = true, maxLength = 50)
    private String cancelRegerName;
    
	@Schema(description = "CSR노트내용", nullable = true, maxLength = 100)
    private String csrNote;
    
	@Schema(description = "데이터이관아이디", nullable = true, maxLength = 5)
    private String migId;
    
	@Schema(description = "데이터이관일시", nullable = true, maxLength = 8)
    private Timestamp migDate;
    
	@Schema(description = "변경서비스번호", nullable = true, maxLength = 20)
    private String chngSvcNo;
    
	@Schema(description = "계약유지지역코드", nullable = true, maxLength = 2)
    private String pauseRsCd;
    
	@Schema(description = "RS오더번호", nullable = true, maxLength = 15)
    private String rsOrdNo;
    
	@Schema(description = "RS서비스계약아이디", nullable = true, maxLength = 11)
    private String rsSaId;
    
	@Schema(description = "번호이동성구분코드", nullable = true, maxLength = 1)
    private String npKind;
    
	@Schema(description = "변호이동전화번호", nullable = true, maxLength = 20)
    private String dorSvcNo;
    
	@Schema(description = "타사사업코드", nullable = true, maxLength = 1)
    private String otherBusiCd;
    
	@Schema(description = "최조예약일자시각", nullable = true, maxLength = 10)
    private String initResvDateHh;
    
	@Schema(description = "대표오더번호", nullable = true, maxLength = 15)
    private String reprOrdNo;
    
	@Schema(description = "가설자구분코드", nullable = true, maxLength = 1)
    private String installerType;
    
	@Schema(description = "실제개통일시", nullable = true, maxLength = 8)
    private Timestamp realCompletedDate;
    
	@Schema(description = "경쟁업체구분코드", nullable = true, maxLength = 2)
    private String competeCompanyCd;
    
	@Schema(description = "작업팀코드", nullable = true, maxLength = 2)
    private String workTeamCd;
    
	@Schema(description = "동시제어오더번호", nullable = true, maxLength = 15)
    private String smtContlOrdNo;
    
	@Schema(description = "개별서비스계약코드", nullable = true, maxLength = 4)
    private String sglSaCd;
    
	@Schema(description = "개별서비스계약상세코드", nullable = true, maxLength = 4)
    private String sglSaDtlCd;
    
	@Schema(description = "개통전해제시점구분코드", nullable = true, maxLength = 2)
    private String cancelTime;
    
	@Schema(description = "변경일시", nullable = true, maxLength = 8)
    private Timestamp chgDate;
    
	@Schema(description = "명의도용검증여부", nullable = true, maxLength = 1)
    private String identityFlag;
    
	@Schema(description = "인지세대상여부", nullable = true, maxLength = 1)
    private String stampTaxtFlag;
    
	@Schema(description = "서류구분코드", nullable = true, maxLength = 1)
    private String paperCd;
    
	@Schema(description = "고객환경내용", nullable = true, maxLength = 20)
    private String custEnv;
    
	@Schema(description = "문서확보필요여부", nullable = true, maxLength = 1)
    private String docSecReqFlag;
    
	@Schema(description = "긴급요청신청여부", nullable = true, maxLength = 1)
    private String emgReqFlag;
    
	@Schema(description = "WM작업자사번", nullable = true, maxLength = 20)
    private String wmEmpNo;
    
	@Schema(description = "오전오후구분코드", nullable = true, maxLength = 2)
    private String dayDiv;
    
	@Schema(description = "해약불가플래그", nullable = true, maxLength = 1)
    private String cancelRefuseFlag;
    
	@Schema(description = "도로명전체주소", nullable = true, maxLength = 200)
    private String raddrText;
    
	@Schema(description = "특수작업유형코드", nullable = true, maxLength = 1)
    private String bnaFlag;
    
	@Schema(description = "응용요청아이디", nullable = true, maxLength = 15)
    private String appReqId;
    
	@Schema(description = "대리점코드", nullable = true, maxLength = 9)
    private String agencyCd;
    
	@Schema(description = "고객연락처일련번호", nullable = true, maxLength = 22)
    private BigDecimal custCntplcSeq;
    
	@Schema(description = "가설구분코드", nullable = true, maxLength = 2)
    private String istDivCd;
    
	@Schema(description = "KNOTE항목아이디", nullable = true, maxLength = 100)
    private String knoteItemId;
    
	@Schema(description = "가설연락처", nullable = true, maxLength = 20)
    private String instlTelNo;
    
}