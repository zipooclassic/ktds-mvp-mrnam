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
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCordordinfoQryDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 고객오더 db table entity
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Table("PP_CORDORDINFO")
public class BtCordordinfoQryEntt implements Persistable<String> {
	
    // description = "오더번호", nullable = false, maxLength = 15
	@Id
	@Column("ORD_NO")	
    private String ordNo;
    
    // description = "서비스계약아이디", nullable = false, maxLength = 11
	@Column("SA_ID")	
    private String saId;
    
    // description = "서비스계약코드", nullable = false, maxLength = 4
	@Column("SA_CD")	
    private String saCd;
    
    // description = "서비스상품유형코드", nullable = true, maxLength = 2
	@Column("SVC_TYPE_CD")	
    private String svcTypeCd;
    
    // description = "계약유형코드", nullable = true, maxLength = 1
	@Column("TERM_CNTRCT_TYPE")	
    private String termCntrctType;
    
    // description = "KT사업사용여부", nullable = true, maxLength = 1
	@Column("KT_USE_FLAG")	
    private String ktUseFlag;
    
    // description = "서비스번호", nullable = true, maxLength = 20
	@Column("SVC_NO")	
    private String svcNo;
    
    // description = "오더관리번호", nullable = true, maxLength = 10
	@Column("ORD_MNG_NO")	
    private String ordMngNo;
    
    // description = "서비스국코드", nullable = true, maxLength = 9
	@Column("SVC_OFC_CD")	
    private String svcOfcCd;
    
    // description = "교환국코드", nullable = true, maxLength = 6
	@Column("EXCH_OFC_CD")	
    private String exchOfcCd;
    
    // description = "설치관리국코드", nullable = true, maxLength = 6
	@Column("INSTL_MNG_OFC_CD")	
    private String instlMngOfcCd;
    
    // description = "고객아이디", nullable = true, maxLength = 11
	@Column("CUST_ID")	
    private String custId;
    
    // description = "고객명", nullable = true, maxLength = 50
	@Column("CUST_NAME")	
    private String custName;
    
    // description = "고객식별번호유형코드", nullable = true, maxLength = 1
	@Column("CUST_NO_TYPE")	
    private String custNoType;
    
    // description = "고객식별번호", nullable = true, maxLength = 20
	@Column("CUST_NO")	
    private String custNo;
    
    // description = "사용자오더유형코드", nullable = false, maxLength = 2
	@Column("USER_ORD_TYPE_CD")	
    private String userOrdTypeCd;
    
    // description = "오더유형코드", nullable = false, maxLength = 2
	@Column("ORD_TYPE_CD")	
    private String ordTypeCd;
    
    // description = "오더유형코드1", nullable = true, maxLength = 2
	@Column("ORD_TYPE_CD_1")	
    private String ordTypeCdN1;
    
    // description = "오더유형코드2", nullable = true, maxLength = 2
	@Column("ORD_TYPE_CD_2")	
    private String ordTypeCdN2;
    
    // description = "원오더유형코드", nullable = true, maxLength = 2
	@Column("ORG_ORD_TYPE_CD")	
    private String orgOrdTypeCd;
    
    // description = "오더사유코드", nullable = true, maxLength = 2
	@Column("ORD_RSN_CD")	
    private String ordRsnCd;
    
    // description = "오더상태코드", nullable = false, maxLength = 2
	@Column("ORD_STATUS_CD")	
    private String ordStatusCd;
    
    // description = "전송유형코드", nullable = true, maxLength = 2
	@Column("TRAN_TYPE_CD")	
    private String tranTypeCd;
    
    // description = "신청자명", nullable = true, maxLength = 50
	@Column("REQER_NAME")	
    private String reqerName;
    
    // description = "고객관계코드", nullable = true, maxLength = 2
	@Column("CUST_REL_CD")	
    private String custRelCd;
    
    // description = "구성희망일시각", nullable = true, maxLength = 10
	@Column("COMPLETED_RESV_DATE_HH")	
    private String completedResvDateHh;
    
    // description = "구성완료일자", nullable = true, maxLength = 8
	@Column("CONST_DATE")	
    private String constDate;
    
    // description = "완료일시", nullable = true, maxLength = 10
	@Column("COMPLETED_DATE_HH")	
    private String completedDateHh;
    
    // description = "공사지연사유코드", nullable = true, maxLength = 4
	@Column("WORK_DELAY_RSN_CD")	
    private String workDelayRsnCd;
    
    // description = "하위국공사지연사유코드", nullable = true, maxLength = 4
	@Column("L_WORK_DELAY_RSN_CD")	
    private String lWorkDelayRsnCd;
    
    // description = "테스트일수", nullable = true, maxLength = 22
	@Column("TEST_DD_CNT")	
    private BigDecimal testDdCnt;
    
    // description = "개통예약일자", nullable = true, maxLength = 8
	@Column("CONST_RESV_DATE")	
    private String constResvDate;
    
    // description = "상위가설서비스번호", nullable = true, maxLength = 20
	@Column("P_INSTL_SVC_NO")	
    private String pInstlSvcNo;
    
    // description = "재사용서비스번호유무", nullable = true, maxLength = 1
	@Column("REUSE_SVC_NO_FLAG")	
    private String reuseSvcNoFlag;
    
    // description = "재사용서비스번호", nullable = true, maxLength = 16
	@Column("REUSE_SVC_NO")	
    private String reuseSvcNo;
    
    // description = "청구지주소변경여부", nullable = true, maxLength = 1
	@Column("IA_ADDR_FLAG")	
    private String iaAddrFlag;
    
    // description = "고객접촉매체코드", nullable = true, maxLength = 2
	@Column("CNTC_MEDIA_CD")	
    private String cntcMediaCd;
    
    // description = "연락전화번호", nullable = true, maxLength = 20
	@Column("CNTC_TEL_NO")	
    private String cntcTelNo;
    
    // description = "연락전화번호1", nullable = true, maxLength = 12
	@Column("CNTC_TEL_NO_1")	
    private String cntcTelNoN1;
    
    // description = "연락이메일주소", nullable = true, maxLength = 50
	@Column("CNTC_EMAIL_ADDR")	
    private String cntcEmailAddr;
    
    // description = "연락주소", nullable = true, maxLength = 100
	@Column("CNTC_ADDR")	
    private String cntcAddr;
    
    // description = "참조내용", nullable = true, maxLength = 100
	@Column("REF")	
    private String ref;
    
    // description = "주소내용", nullable = true, maxLength = 100
	@Column("ADDR_TEXT")	
    private String addrText;
    
    // description = "서비스개수", nullable = true, maxLength = 22
	@Column("SVC_CNT")	
    private BigDecimal svcCnt;
    
    // description = "연락자식별번호", nullable = true, maxLength = 13
	@Column("CNTC_CUST_NO")	
    private String cntcCustNo;
    
    // description = "이용정지서비스계약아이디", nullable = true, maxLength = 11
	@Column("PAUSE_SA_ID")	
    private String pauseSaId;
    
    // description = "접수국코드", nullable = true, maxLength = 9
	@Column("RCV_OFC_CD")	
    private String rcvOfcCd;
    
    // description = "접수일자", nullable = false, maxLength = 8
	@Column("RCV_DATE")	
    private String rcvDate;
    
    // description = "접수자사번", nullable = true, maxLength = 9
	@Column("RCVER_EMP_NO")	
    private String rcverEmpNo;
    
    // description = "접수자명", nullable = true, maxLength = 50
	@Column("RCVER_EMP_NAME")	
    private String rcverEmpName;
    
    // description = "접수자전화번호", nullable = true, maxLength = 12
	@Column("RCVER_EMP_TEL_NO")	
    private String rcverEmpTelNo;
    
    // description = "등록일시", example = "YYYYMMDDHHMISS", nullable = false
	@Column("REG_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")	
    private Timestamp regDate;
    
    // description = "구오더번호", nullable = true, maxLength = 20
	@Column("LEGACY_ORD_NO")	
    private String legacyOrdNo;
    
    // description = "무출동코드", nullable = true, maxLength = 1
	@Column("REUSE_SVC_CD")	
    private String reuseSvcCd;
    
    // description = "완료여부", nullable = false, maxLength = 1
	@Column("COMPLETE_FLAG")	
    private String completeFlag;
    
    // description = "취소사유코드", nullable = true, maxLength = 2
	@Column("CANCEL_RSN_CD")	
    private String cancelRsnCd;
    
    // description = "취소참조내용", nullable = true, maxLength = 100
	@Column("CANCEL_REF")	
    private String cancelRef;
    
    // description = "취소등록자명", nullable = true, maxLength = 50
	@Column("CANCEL_REGER_NAME")	
    private String cancelRegerName;
    
    // description = "CSR노트내용", nullable = true, maxLength = 100
	@Column("CSR_NOTE")	
    private String csrNote;
    
    // description = "데이터이관아이디", nullable = true, maxLength = 5
	@Column("MIG_ID")	
    private String migId;
    
    // description = "데이터이관일시", example = "YYYYMMDDHHMISS", nullable = true
	@Column("MIG_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")	
    private Timestamp migDate;
    
    // description = "변경서비스번호", nullable = true, maxLength = 20
	@Column("CHNG_SVC_NO")	
    private String chngSvcNo;
    
    // description = "계약유지지역코드", nullable = true, maxLength = 2
	@Column("PAUSE_RS_CD")	
    private String pauseRsCd;
    
    // description = "RS오더번호", nullable = true, maxLength = 15
	@Column("RS_ORD_NO")	
    private String rsOrdNo;
    
    // description = "RS서비스계약아이디", nullable = true, maxLength = 11
	@Column("RS_SA_ID")	
    private String rsSaId;
    
    // description = "번호이동성구분코드", nullable = true, maxLength = 1
	@Column("NP_KIND")	
    private String npKind;
    
    // description = "변호이동전화번호", nullable = true, maxLength = 20
	@Column("DOR_SVC_NO")	
    private String dorSvcNo;
    
    // description = "타사사업코드", nullable = true, maxLength = 1
	@Column("OTHER_BUSI_CD")	
    private String otherBusiCd;
    
    // description = "최조예약일자시각", nullable = true, maxLength = 10
	@Column("INIT_RESV_DATE_HH")	
    private String initResvDateHh;
    
    // description = "대표오더번호", nullable = true, maxLength = 15
	@Column("REPR_ORD_NO")	
    private String reprOrdNo;
    
    // description = "가설자구분코드", nullable = true, maxLength = 1
	@Column("INSTALLER_TYPE")	
    private String installerType;
    
    // description = "실제개통일시", example = "YYYYMMDDHHMISS", nullable = true
	@Column("REAL_COMPLETED_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")	
    private Timestamp realCompletedDate;
    
    // description = "경쟁업체구분코드", nullable = true, maxLength = 2
	@Column("COMPETE_COMPANY_CD")	
    private String competeCompanyCd;
    
    // description = "작업팀코드", nullable = true, maxLength = 2
	@Column("WORK_TEAM_CD")	
    private String workTeamCd;
    
    // description = "동시제어오더번호", nullable = true, maxLength = 15
	@Column("SMT_CONTL_ORD_NO")	
    private String smtContlOrdNo;
    
    // description = "개별서비스계약코드", nullable = true, maxLength = 4
	@Column("SGL_SA_CD")	
    private String sglSaCd;
    
    // description = "개별서비스계약상세코드", nullable = true, maxLength = 4
	@Column("SGL_SA_DTL_CD")	
    private String sglSaDtlCd;
    
    // description = "개통전해제시점구분코드", nullable = true, maxLength = 2
	@Column("CANCEL_TIME")	
    private String cancelTime;
    
    // description = "변경일시", example = "YYYYMMDDHHMISS", nullable = true
	@Column("CHG_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")	
    private Timestamp chgDate;
    
    // description = "명의도용검증여부", nullable = true, maxLength = 1
	@Column("IDENTITY_FLAG")	
    private String identityFlag;
    
    // description = "인지세대상여부", nullable = true, maxLength = 1
	@Column("STAMP_TAXT_FLAG")	
    private String stampTaxtFlag;
    
    // description = "서류구분코드", nullable = true, maxLength = 1
	@Column("PAPER_CD")	
    private String paperCd;
    
    // description = "고객환경내용", nullable = true, maxLength = 20
	@Column("CUST_ENV")	
    private String custEnv;
    
    // description = "문서확보필요여부", nullable = true, maxLength = 1
	@Column("DOC_SEC_REQ_FLAG")	
    private String docSecReqFlag;
    
    // description = "긴급요청신청여부", nullable = true, maxLength = 1
	@Column("EMG_REQ_FLAG")	
    private String emgReqFlag;
    
    // description = "WM작업자사번", nullable = true, maxLength = 20
	@Column("WM_EMP_NO")	
    private String wmEmpNo;
    
    // description = "오전오후구분코드", nullable = true, maxLength = 2
	@Column("DAY_DIV")	
    private String dayDiv;
    
    // description = "해약불가플래그", nullable = true, maxLength = 1
	@Column("CANCEL_REFUSE_FLAG")	
    private String cancelRefuseFlag;
    
    // description = "도로명전체주소", nullable = true, maxLength = 200
	@Column("RADDR_TEXT")	
    private String raddrText;
    
    // description = "특수작업유형코드", nullable = true, maxLength = 1
	@Column("BNA_FLAG")	
    private String bnaFlag;
    
    // description = "응용요청아이디", nullable = true, maxLength = 15
	@Column("APP_REQ_ID")	
    private String appReqId;
    
    // description = "대리점코드", nullable = true, maxLength = 9
	@Column("AGENCY_CD")	
    private String agencyCd;
    
    // description = "고객연락처일련번호", nullable = true, maxLength = 22
	@Column("CUST_CNTPLC_SEQ")	
    private BigDecimal custCntplcSeq;
    
    // description = "가설구분코드", nullable = true, maxLength = 2
	@Column("IST_DIV_CD")	
    private String istDivCd;
    
    // description = "KNOTE항목아이디", nullable = true, maxLength = 100
	@Column("KNOTE_ITEM_ID")	
    private String knoteItemId;
    
    // description = "가설연락처", nullable = true, maxLength = 20
	@Column("INSTL_TEL_NO")	
    private String instlTelNo;
    
	@Override
	public boolean isNew() {
		return false;
	}
	
	@Override
	public String getId() {
		return this.getOrdNo();
	}
	
	public static BtCordordinfoQryEntt of(BtCordordinfoQryDto dto) {
		return BtCordordinfoQryEntt.builder()
			.ordNo(dto.getOrdNo())
			.saId(dto.getSaId())
			.saCd(dto.getSaCd())
			.svcTypeCd(dto.getSvcTypeCd())
			.termCntrctType(dto.getTermCntrctType())
			.ktUseFlag(dto.getKtUseFlag())
			.svcNo(dto.getSvcNo())
			.ordMngNo(dto.getOrdMngNo())
			.svcOfcCd(dto.getSvcOfcCd())
			.exchOfcCd(dto.getExchOfcCd())
			.instlMngOfcCd(dto.getInstlMngOfcCd())
			.custId(dto.getCustId())
			.custName(dto.getCustName())
			.custNoType(dto.getCustNoType())
			.custNo(dto.getCustNo())
			.userOrdTypeCd(dto.getUserOrdTypeCd())
			.ordTypeCd(dto.getOrdTypeCd())
			.ordTypeCdN1(dto.getOrdTypeCdN1())
			.ordTypeCdN2(dto.getOrdTypeCdN2())
			.orgOrdTypeCd(dto.getOrgOrdTypeCd())
			.ordRsnCd(dto.getOrdRsnCd())
			.ordStatusCd(dto.getOrdStatusCd())
			.tranTypeCd(dto.getTranTypeCd())
			.reqerName(dto.getReqerName())
			.custRelCd(dto.getCustRelCd())
			.completedResvDateHh(dto.getCompletedResvDateHh())
			.constDate(dto.getConstDate())
			.completedDateHh(dto.getCompletedDateHh())
			.workDelayRsnCd(dto.getWorkDelayRsnCd())
			.lWorkDelayRsnCd(dto.getLWorkDelayRsnCd())
			.testDdCnt(dto.getTestDdCnt())
			.constResvDate(dto.getConstResvDate())
			.pInstlSvcNo(dto.getPInstlSvcNo())
			.reuseSvcNoFlag(dto.getReuseSvcNoFlag())
			.reuseSvcNo(dto.getReuseSvcNo())
			.iaAddrFlag(dto.getIaAddrFlag())
			.cntcMediaCd(dto.getCntcMediaCd())
			.cntcTelNo(dto.getCntcTelNo())
			.cntcTelNoN1(dto.getCntcTelNoN1())
			.cntcEmailAddr(dto.getCntcEmailAddr())
			.cntcAddr(dto.getCntcAddr())
			.ref(dto.getRef())
			.addrText(dto.getAddrText())
			.svcCnt(dto.getSvcCnt())
			.cntcCustNo(dto.getCntcCustNo())
			.pauseSaId(dto.getPauseSaId())
			.rcvOfcCd(dto.getRcvOfcCd())
			.rcvDate(dto.getRcvDate())
			.rcverEmpNo(dto.getRcverEmpNo())
			.rcverEmpName(dto.getRcverEmpName())
			.rcverEmpTelNo(dto.getRcverEmpTelNo())
			.regDate(dto.getRegDate())
			.legacyOrdNo(dto.getLegacyOrdNo())
			.reuseSvcCd(dto.getReuseSvcCd())
			.completeFlag(dto.getCompleteFlag())
			.cancelRsnCd(dto.getCancelRsnCd())
			.cancelRef(dto.getCancelRef())
			.cancelRegerName(dto.getCancelRegerName())
			.csrNote(dto.getCsrNote())
			.migId(dto.getMigId())
			.migDate(dto.getMigDate())
			.chngSvcNo(dto.getChngSvcNo())
			.pauseRsCd(dto.getPauseRsCd())
			.rsOrdNo(dto.getRsOrdNo())
			.rsSaId(dto.getRsSaId())
			.npKind(dto.getNpKind())
			.dorSvcNo(dto.getDorSvcNo())
			.otherBusiCd(dto.getOtherBusiCd())
			.initResvDateHh(dto.getInitResvDateHh())
			.reprOrdNo(dto.getReprOrdNo())
			.installerType(dto.getInstallerType())
			.realCompletedDate(dto.getRealCompletedDate())
			.competeCompanyCd(dto.getCompeteCompanyCd())
			.workTeamCd(dto.getWorkTeamCd())
			.smtContlOrdNo(dto.getSmtContlOrdNo())
			.sglSaCd(dto.getSglSaCd())
			.sglSaDtlCd(dto.getSglSaDtlCd())
			.cancelTime(dto.getCancelTime())
			.chgDate(dto.getChgDate())
			.identityFlag(dto.getIdentityFlag())
			.stampTaxtFlag(dto.getStampTaxtFlag())
			.paperCd(dto.getPaperCd())
			.custEnv(dto.getCustEnv())
			.docSecReqFlag(dto.getDocSecReqFlag())
			.emgReqFlag(dto.getEmgReqFlag())
			.wmEmpNo(dto.getWmEmpNo())
			.dayDiv(dto.getDayDiv())
			.cancelRefuseFlag(dto.getCancelRefuseFlag())
			.raddrText(dto.getRaddrText())
			.bnaFlag(dto.getBnaFlag())
			.appReqId(dto.getAppReqId())
			.agencyCd(dto.getAgencyCd())
			.custCntplcSeq(dto.getCustCntplcSeq())
			.istDivCd(dto.getIstDivCd())
			.knoteItemId(dto.getKnoteItemId())
			.instlTelNo(dto.getInstlTelNo())
			.build();
		}
}