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
package com.kt.icis.oder.baseinfo.csnl.command.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import com.kt.icis.cmmnfrwk.monitoring.util.StringUtils;
import com.kt.icis.cmmnfrwk.utils.LogUtil;
import com.kt.icis.oder.baseinfo.common.command.payload.out.dto.BiDaemonTmpUseOutCmdDto;
import com.kt.icis.oder.baseinfo.common.query.repository.ccpi.BiCcpidmbasicinfoQryRepo;
import com.kt.icis.oder.baseinfo.common.query.repository.ccus.BiCcustInfoPtuseAgreeQryRepo;
import com.kt.icis.oder.baseinfo.common.query.repository.ccus.BiCcustinfoagreehstQryRepo;
import com.kt.icis.oder.baseinfo.common.query.repository.ccus.IfrCcustInfoPtuseAgreeQryRepo;
import com.kt.icis.oder.baseinfo.common.query.repository.csys.BiCsyscdQryRepo;
import com.kt.icis.oder.baseinfo.common.query.repository.csys.BiCsysrscdallocQryRepo;
import com.kt.icis.oder.baseinfo.common.query.repository.cwko.BiCwkocustinfoagreeQryRepo;
import com.kt.icis.oder.baseinfo.common.query.repository.dto.ccus.BiCcustInfoPtuseAgreeQryDto;
import com.kt.icis.oder.baseinfo.common.query.repository.dto.ccus.IfrCcustInfoPtuseAgreeQryDto;
import com.kt.icis.oder.baseinfo.common.query.repository.dto.cwko.BiCwkocustinfoagreeQryDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustInfoAgreeTrtCmdCntr {
	
	private final IfrCcustInfoPtuseAgreeQryRepo ifrCcustInfoPtuseAgreeQryRepo;
	private final BiCcustinfoagreehstQryRepo biCcustinfoagreehstQryRepo;
	private final BiCcustInfoPtuseAgreeQryRepo biCcustInfoPtuseAgreeQryRepo;
	private final BiCsysrscdallocQryRepo biCsysrscdallocQryRepo;
	private final BiCsyscdQryRepo biCsyscdQryRepo;
	private final BiCcpidmbasicinfoQryRepo biCcpidmbasicinfoQryRepo;
	private final BiCwkocustinfoagreeQryRepo biCwkocustinfoagreeQryRepo;
	
	public BiDaemonTmpUseOutCmdDto custInfoAgreeTrt() {

//		int nTotCnt 	= 0;
//		int nFetchCnt 	= 0;
//		int nTPCnt 		= 0;
//		int nErrCnt 	= 0;
				
		/* 대상 자료 발췌 */
		List<IfrCcustInfoPtuseAgreeQryDto> ifrCcustInfoPtuseAgreeQryDtoList = new ArrayList<>(); 
		
		try {
			/* 인터페이스 테이블 IFR_CCUST_INFO_PTUSE_AGREE (고객동의 반영 NS/RS_CDM_연동) 에서 고객동의 대상을 발췌한다 */
			ifrCcustInfoPtuseAgreeQryDtoList = ifrCcustInfoPtuseAgreeQryRepo.findByCcustInfoPtuseAgree();
		} catch(Exception e) {
			LogUtil.error(e.getMessage(), e);
			return BiDaemonTmpUseOutCmdDto.builder()
					.resltFlag("F")
					.resltMsg("대상 자료 발췌 실패")
					.build();
		}
		
		if(ObjectUtils.isEmpty(ifrCcustInfoPtuseAgreeQryDtoList)) {
			LogUtil.info(" ●●●●●●●●●● 고객정보동의 처리 대상 조회 데이터가 존재 하지 않습니다. ●●●●●●●●●● ");
			return BiDaemonTmpUseOutCmdDto.builder()
					.resltFlag("S")
					.resltMsg("고객정보동의 처리 대상 없음")
					.build();
		} else {
			LogUtil.info("●●●●● {} ●●●●●", ifrCcustInfoPtuseAgreeQryDtoList.toString());
			LogUtil.info("●●●●● 처리 데이터 건수 : {} ●●●●●", ifrCcustInfoPtuseAgreeQryDtoList.size());
			
			/* 조회된 레코드 수 만큼 순환한다.*/
			for(IfrCcustInfoPtuseAgreeQryDto dto : ifrCcustInfoPtuseAgreeQryDtoList) {
				
				/* 변수 셋팅 */
				String vcCustInfoAgreeCd 	= dto.getCustInfoAgreeCd();
				
				/*-----------------------------------------------------*/
                /* 2020.07.25(20200128)고객서식지 마케팅동의 보완      */
				/* 연동자료 역전현상에 따른 이력 만료자료 처리 제외    */
                /*-----------------------------------------------------*/
				
				/* 동의정보 처리 자료 정합성 체크  */ 
				if(!this.ChkAgreeProc(dto)) {
					SetResultUpdate(dto, "E", "");					
					continue;
				}
				
				/*-----------------------------------------------------*/
                /* 2021.07.05(20210136)그룹사 동의항목 추가 및 구조개선*/
				/* 고객기준 동의정보 row 구조테이블에 전체 자료 관리   */
				/* 기존 01,04,55,56 동의 기존 테이블 데이터 유지       */
				/* 01,04,55,56 포함한 모든 고객기준 동의정보           */
				/* TB_CCUST_INFO_AGREE_HST 테이블 관리                 */
				/*-----------------------------------------------------*/
				
				/* 고객기준 동의 정보 처리 (전체)*/
				if(!this.SetCustInfoAgreeHst(dto)) {
					SetResultUpdate(dto, "E", "");					
					continue;
				}
				
				/*-----------------------------------------------------*/
                /* 2020.06.15(20200099)고객서식지 마케팅동의 변경      */
                /* 마케팅수집이용동의"55", 마케팅광고수신동의"56" 추가 */
                /*-----------------------------------------------------*/

                /* 01:이용동의 04:위탁동의, 55:마케팅사용동의, 56:마케팅광고수신동의에 대해서만 처리 */
				/* 20170109 CUST_ID가 없는경우 스킵처리 추가 */
				if("01".equals(vcCustInfoAgreeCd) || "04".equals(vcCustInfoAgreeCd)
					|| "55".equals(vcCustInfoAgreeCd) || "56".equals(vcCustInfoAgreeCd)) {
					
					/* 기존테이블에 CUST_ID 가 존재하는지 체크 */
					String vcCustId = SelectPtuseAgree(dto);
					if("X".equals(vcCustId)) {
						continue;
					}
					
					/* 오더번호 획득 */
					String newOrdNo = GetOrdNo();
					dto.setNewOrdNo(newOrdNo);
					
					/* 기존테이블에 CUST_ID가 존재하지 않는 데이타는 신규 처리한다. */
					if("".equals(vcCustId) || StringUtils.isEmpty(vcCustId)) {
						/* 고객동의(BI_CCUST_INFO_PTUSE_AGREE) 테이블에 데이타를 입력한다 */
						LogUtil.info("고객동의(BI_CCUST_INFO_PTUSE_AGREE) 테이블에 데이타를 입력");
						InsertPtuseAgree(dto);
						SetResultUpdate(dto, "Y", "");
					} else {						
						/* 고객동의(BI_CCUST_INFO_PTUSE_AGREE) 테이블에 데이타를 업데이트한다 */
						LogUtil.info("고객동의(BI_CCUST_INFO_PTUSE_AGREE) 테이블에 데이타를 업데이트");
						if(this.UpdatePtuseAgree(dto)) {
							
							/* 고객기본정보(BI_CCUST_INFO_PTUSE_AGREE) 테이블에 데이타를 입력한다 */
							LogUtil.info("고객기본정보(BI_CCUST_INFO_PTUSE_AGREE) 테이블에 데이타를 입력");							
							
							if(this.InsertPtuseAgree2(dto)) {
								/* 인터페이스테이블 처리결과(성공) 셋팅 */
								LogUtil.info("인터페이스테이블 처리결과(성공)");
								SetResultUpdate(dto, "Y", "");
							} else {
								/* 인터페이스테이블 처리결과(실패) 셋팅 */
								LogUtil.info("인터페이스테이블 처리결과(실패)");
								SetResultUpdate(dto, "E", "");
							}
						} else {
							/* 인터페이스테이블 처리결과(실패) 셋팅 */
							LogUtil.info("인터페이스테이블 처리결과(실패)");
							SetResultUpdate(dto, "E", "");
						}						
					}
				} else {
					/* 인터페이스테이블 처리결과(SKIP) 셋팅 */
					LogUtil.info("인터페이스테이블 처리결과(정의하지 않은 코드)");
					SetResultUpdate(dto, "E", "SKIP: 정의하지 않은 코드임");
				}
				
				/* BI_CCUST_INFO_PTUSE_AGREE 반영 종료*/
				
				/*--------------------------------------------------------*/
				/* 2020.06.16(20200099)고객서시지 마케팅 동의 항목 변경   */
				/* 마케팅동의 항목에 대한 TB_CCPISECPRIVACY 변경          */
				/*--------------------------------------------------------*/
				if("55".equals(vcCustInfoAgreeCd) || "56".equals(vcCustInfoAgreeCd)) {					
					if(!this.ChkMktAgreeProc(dto)) {
						LogUtil.info("마케팅 동의항목 업무 적용 상태 체크 SKIP ...");
						continue;
					}
					
					LogUtil.info("마케팅 동의 회선별 정보 처리 대상 설정 {}", vcCustInfoAgreeCd);
					if(!this.SetCwkoSecprivacy(dto)) {
						continue;
					}
				} 				
			} /* FOR */
			
//			LogUtil.info("고객단위 동의정보 처리 성공 건수 : {}", 0);
//			LogUtil.info("고객단위 동의정보 처리 실패 건수 : {}", 0);
		}
		return BiDaemonTmpUseOutCmdDto.builder()
				.resltFlag("S")
				.resltMsg("CSNL920D Excecution")
				.build();
	}
	
	
	/**
     * @Method Name : SetResultUpdate
     * @Method 설명 : 인터페이스테이블에 결과값 셋팅
     * @작성일 : 2025. 02. 12
     * @작성자 : 리그시스템 / 91361628 / 최현
     * @param IfrCcustInfoPtuseAgreeQryDto dto, String vcProcFlag, String vcProcResult
     * @return 
     */
	private void SetResultUpdate(IfrCcustInfoPtuseAgreeQryDto dto, String vcProcFlag, String vcProcResult) {
		
		String vcIfHstNo = dto.getIfHstNo();
		
		try {
			ifrCcustInfoPtuseAgreeQryRepo.updateCcustInfoPtuseAgree(vcIfHstNo, vcProcFlag, vcProcResult);
		} catch(Exception e) {
			LogUtil.info("Error : IFR_CCUST_INFO_PTUSE_AGREE UPDATE(결과셋팅) 오류 [CUST_ID : {}]", dto.getCustId());
		}
	}
	
	/**
     * @Method Name : ChkAgreeProc
     * @Method 설명 : 고객동의 자료 정합 체크
     * @작성일 : 2025. 02. 12
     * @작성자 : 리그시스템 / 91361628 / 최현
     * @param IfrCcustInfoPtuseAgreeQryDto dto
     * @return boolean
     */
	private boolean ChkAgreeProc(IfrCcustInfoPtuseAgreeQryDto dto) {
		/* 고객 ID 체크 */
		if(StringUtils.isEmpty(dto.getCustId())) {
			LogUtil.info("고객ID 미 존재 Error");
			LogUtil.info("ChkAgreeProc() 고객ID 미존재 자료 처리 제외 {}", dto.getCustId());
			return false;
		}		
		
		/*--------------------------------------------------*/
	    /* 2020.07.25(20200128) 고객서식지 마케팅동의 보완  */
		/* 연동 역전현상에따른 이력 만료 처리 제외 최종     */
		/* 유효한 자료로 이력 처리                          */
	    /*--------------------------------------------------*/
	    
	    /* 역전현상에 따른 이력 만료처리 제외 */
		if(!"99991231".equals(dto.getEndDt())) {
			LogUtil.info("이력 만료 중복 자료 SKIP");
			LogUtil.info("ChkAgreeProc() 이력처리 만료(중복)자료 처리 제외 {}, {}", dto.getCustId(), dto.getEndDt());
			return false;
		}
		
		return true;
	}
	
	/**
     * @Method Name : SetCustInfoAgreeHst
     * @Method 설명 : 고객 기준 동의정보 이력처리 
     * @작성일 : 2025. 02. 12
     * @작성자 : 리그시스템 / 91361628 / 최현
     * @param IfrCcustInfoPtuseAgreeQryDto dto
     * @return boolean
     */
	private boolean SetCustInfoAgreeHst(IfrCcustInfoPtuseAgreeQryDto dto) {
		String vcInfCustId 			= dto.getCustId();
		String vcCustInfoAgreeCd 	= dto.getCustInfoAgreeCd();
		String vcCustInfoAgreeYn	= dto.getCustInfoAgreeYn();
		String vcStartDt			= dto.getStartDt();
		
		String vcAgreeYn = dto.getCustInfoAgreeYn();
		String vcChkFlg = "";
		/*-------------------------------------------*/
	    /* 1.기등록 동의정보 조회                    */
		/*-------------------------------------------*/
		try {
			vcChkFlg = biCcustinfoagreehstQryRepo.selectCcustInfoAgreeHstCsnl920d(vcInfCustId, vcCustInfoAgreeCd, vcCustInfoAgreeYn);
		} catch(Exception e) {
			LogUtil.info("Error : BI_CCUST_INFO_AGREE_HST SELECT 오류 [CUST_ID : {}]", vcInfCustId);
			return false;
		}
				
		/*-------------------------------------------*/ 
	    /* 2.기등록자료 동의 이력 처리               */
		/*-------------------------------------------*/
		if(!StringUtils.isEmpty(vcChkFlg)) {
			/* 동의코드값 동일 Check  */ 
			if("Y".equals(vcChkFlg)) {
				LogUtil.info("동의값 동일 SKIP Old : {} = New : {}", vcCustInfoAgreeYn, vcAgreeYn);
				return true;
			}
			
			LogUtil.info("●●●●● SetCustInfoAgreeHst() BI_CCUST_INFO_AGREE_HST UPDATE ... ●●●●●");
			
			/* 만료처리 */
			try {
				biCcustinfoagreehstQryRepo.updateCcustInfoAgreeHstCsnl920d(vcStartDt, vcInfCustId, vcCustInfoAgreeCd);
			} catch(Exception e) {
				LogUtil.info("Error : BI_CCUST_INFO_AGREE_HST UPDATE 오류");
				return false;
			}		
		}
		
		/*-------------------------------------------*/ 
	    /* 3.동의 최종 이력 처리                     */
		/*-------------------------------------------*/
		LogUtil.info("●●●●● SetCustInfoAgreeHst() BI_CCUST_INFO_AGREE_HST INSERT ... ●●●●●");
		try {
			biCcustinfoagreehstQryRepo.insertCcustInfoAgreeHstCsnl920d(dto);
		} catch(Exception e) {
			LogUtil.info("Error : BI_CCUST_INFO_AGREE_HST INSERT 오류");
			return false;
		}
		
		return true;			
	}

	/**
     * @Method Name : SelectPtuseAgree
     * @Method 설명 : 기존테이블에 CUST_ID 가 존재하는지 체크
     * @작성일 : 2025. 02. 12
     * @작성자 : 리그시스템 / 91361628 / 최현
     * @param IfrCcustInfoPtuseAgreeQryDto dto
     * @return String
     */
	private String SelectPtuseAgree(IfrCcustInfoPtuseAgreeQryDto dto) {
		String vcCustId = "";
		String vcInfCustId = dto.getCustId();
		try {
			vcCustId = biCcustInfoPtuseAgreeQryRepo.selectCcustInfoPtuseAgreeCsnl920d(vcInfCustId);
			LogUtil.info("●●●●● vcCustId 		: {}", vcCustId);
			LogUtil.info("●●●●● vcInfCustId 	: {}", vcInfCustId);
		} catch(Exception e) {
			LogUtil.info("Error : TB_CCUST_INFO_PTUSE_AGREE SELECT 오류 [CUST_ID : {}]", vcInfCustId);
			return "X";
		}
		
		/*--------------------------------------------------*/
	    /* 2020.07.25(20200128) 고객서식지 마케팅동의 보완  */
		/* 연동 역전현상에따른 이력 만료 처리 제외 최종     */
		/* 유효한 자료로 이력 처리                          */
	    /*--------------------------------------------------*/
	    
	    /* 역전현상에 따른 이력 만료처리 제외 */
		if(!"99991231".equals(dto.getEndDt())) {
			LogUtil.info("SelectPtuseAgree() 이력처리 만료(중복)자료 처리 제외 {}", dto.getEndDt());
			return "X";
		}
		
		return vcCustId;
	}
	
	/**
     * @Method Name : GetOrdNo
     * @Method 설명 : 오더번호 생성
     * @작성일 : 2025. 02. 12
     * @작성자 : 리그시스템 / 91361628 / 최현
     * @param 
     * @return ordNo
     */
	private String GetOrdNo() {
		return biCsysrscdallocQryRepo.selectFnBiCsab10601();
	}
	
	/**
     * @Method Name : InsertPtuseAgree
     * @Method 설명 : 고객동의 테이블에 데이타를 입력한다
     * @작성일 : 2025. 02. 12
     * @작성자 : 리그시스템 / 91361628 / 최현
     * @param IfrCcustInfoPtuseAgreeQryDto dto
     * @return 
     */
	private boolean InsertPtuseAgree(IfrCcustInfoPtuseAgreeQryDto dto) {		
		LogUtil.info("InsertPtuseAgree() BI_CCUST_INFO_PTUSE_AGREE INSERT ... ");
		
		try {
			
			biCcustInfoPtuseAgreeQryRepo.insertCcustInfoPtuseAgreeCsnl920d(dto);
			
			LogUtil.info("●●●●● vcCustInfoAgreeCd : {}", dto.getCustInfoAgreeCd());
			LogUtil.info("●●●●● vcCustInfoAgreeYn : {}", dto.getCustInfoAgreeYn());
		} catch(Exception e) {
			LogUtil.info("Error : BI_CCUST_INFO_PTUSE_AGREE INSERT 오류 [CUST_ID : {}]", dto.getCustId());
			return false;
		}
		
		return true;
	}
	
	/**
     * @Method Name : InsertPtuseAgree2
     * @Method 설명 : 고객동의 테이블에 데이타를 입력한다
     * @작성일 : 2025. 02. 12
     * @작성자 : 리그시스템 / 91361628 / 최현
     * @param IfrCcustInfoPtuseAgreeQryDto dto
     * @return boolean
     */
	private boolean InsertPtuseAgree2(IfrCcustInfoPtuseAgreeQryDto dto) {
		LogUtil.info("InsertPtuseAgree2() BI_CCUST_INFO_PTUSE_AGREE INSERT ... ");
		
		try {
			// SELECT INSERT
			List<BiCcustInfoPtuseAgreeQryDto> biCcustInfoPtuseAgreeQryDtoList = biCcustInfoPtuseAgreeQryRepo.selectCcustInfoPtuseAgreeCsnl920d(dto);
			
			if(biCcustInfoPtuseAgreeQryDtoList.size() > 0) {
				for(BiCcustInfoPtuseAgreeQryDto inDto : biCcustInfoPtuseAgreeQryDtoList) {
					biCcustInfoPtuseAgreeQryRepo.insertCcustInfoPtuseAgree2Csnl920d(inDto);
				}
			}
		
			LogUtil.info("●●●●● vcCustInfoAgreeCd : {}", dto.getCustInfoAgreeCd());
			LogUtil.info("●●●●● vcCustInfoAgreeYn : {}", dto.getCustInfoAgreeYn());
		} catch(Exception e) {
			LogUtil.info("Error : BI_CCUST_INFO_PTUSE_AGREE INSERT 오류 [CUST_ID : {}]", dto.getCustId());
			return false;
		}
		
		return true;
	}
	
	/**
     * @Method Name : ChkMktAgreeProc
     * @Method 설명 : 마케팅 동의 업무 적용 여부 조회
     * @작성일 : 2025. 02. 12
     * @작성자 : 리그시스템 / 91361628 / 최현
     * @param IfrCcustInfoPtuseAgreeQryDto dto
     * @return boolean
     */
	private boolean ChkMktAgreeProc(IfrCcustInfoPtuseAgreeQryDto dto) {
		/*------------------------------------------------*/
	    /* 업무적용 기준정보 체크                         */
	    /*------------------------------------------------*/
		int nChk = 0;
		try {
			nChk = biCsyscdQryRepo.selectCsyscdCsnl920d();
		} catch(Exception e) {
			LogUtil.info("BI_CSYSCD SELECT ERROR");
		}
		
		/* 업무 미적용 기간 */
		if(nChk < 1) {
			return false;
		}
		
		LogUtil.info("ChkMktAgreeProc() 업무적용(1:적용) {} ", nChk);
		
		/*------------------------------------------------*/
	    /* 시스템 장애 상태 체크                          */
	    /*------------------------------------------------*/
		String vcStatusCd = "";
		try {
			vcStatusCd = biCcpidmbasicinfoQryRepo.selectStatusCodeCsnl920d();
		} catch(Exception e) {
			LogUtil.info("BI_CCPIDMBASICINFO SELECT ERROR");			
		}
		
		/* 'Y': 정상(업무처리) , 'N': 장애(업무미처리)  */
		LogUtil.info("ChkMktAgreeProc() 시스템장애 상태 {}", vcStatusCd);
		
		if(!"Y".equals(vcStatusCd)) {
			return false;
		}
		
		return true;
	}
	
	/**
     * @Method Name : UpdatePtuseAgree
     * @Method 설명 : 고객기본정보 테이블에 데이타를 업데이트한다
     * @작성일 : 2025. 02. 12
     * @작성자 : 리그시스템 / 91361628 / 최현
     * @param IfrCcustInfoPtuseAgreeQryDto dto
     * @return boolean
     */
	private boolean UpdatePtuseAgree(IfrCcustInfoPtuseAgreeQryDto dto) {		
		
		try {			
			biCcustInfoPtuseAgreeQryRepo.updateCcustInfoPtuseAgreeCsnl920d(dto);
		} catch(Exception e) {
			LogUtil.info("Error : BI_CCUST_INFO_PTUSE_AGREE UPDATE 오류 [CUST_ID : {}]", dto.getCustId());
			return false;
		}
		
		return true;
	}
	
	/**
     * @Method Name : SetCwkoSecprivacy
     * @Method 설명 : 고객정보동의 자료 고객단위 일괄처리 작업 대상 자료 생성
     * @작성일 : 2025. 02. 12
     * @작성자 : 리그시스템 / 91361628 / 최현
     * @param IfrCcustInfoPtuseAgreeQryDto dto
     * @return boolean
     */
	private boolean SetCwkoSecprivacy(IfrCcustInfoPtuseAgreeQryDto dto) {
		LogUtil.info("SetCwkoSecprivacy() BI_CWKOCUSTINFOAGREE INSERT ...");
		
		try {
			List<BiCwkocustinfoagreeQryDto> biCwkocustinfoagreeQryDtoList = biCwkocustinfoagreeQryRepo.selectCwkocustinfoagreeCsnl920d(dto);
			
			if(biCwkocustinfoagreeQryDtoList.size() > 0) {
				for(BiCwkocustinfoagreeQryDto inDto : biCwkocustinfoagreeQryDtoList) {
					biCwkocustinfoagreeQryRepo.insertCwkocustinfoagreeCsnl920d(inDto);
				}			
			}			
			
		} catch(Exception e) {
			LogUtil.info("Error : BI_CWKOCUSTINFOAGREE INSERT 오류 [CUST_ID : {}]", dto.getCustId());
			return false;
		}

		return true;
	}

}
