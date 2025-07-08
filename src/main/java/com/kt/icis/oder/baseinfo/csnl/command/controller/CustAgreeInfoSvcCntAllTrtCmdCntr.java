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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.icis.cmmnfrwk.utils.LogUtil;
import com.kt.icis.oder.baseinfo.common.query.repository.ccpi.BiCcpisecprivacyQryRepo;
import com.kt.icis.oder.baseinfo.common.query.repository.csys.BiCsyscdQryRepo;
import com.kt.icis.oder.baseinfo.common.query.repository.cwko.BiCwkocustinfoagreeQryRepo;
import com.kt.icis.oder.baseinfo.common.query.repository.dto.ccpi.BiCcpisecprivacyOutQryDto;
import com.kt.icis.oder.baseinfo.common.query.repository.dto.cwko.BiCwkocustinfoagreeQryOutDto;
import com.kt.icis.oder.baseinfo.common.query.repository.dto.mprd.BiMprdcdQryOutDto;
import com.kt.icis.oder.baseinfo.common.query.repository.entity.ccpi.BiCcpisecprivacy1QryEntt;
import com.kt.icis.oder.baseinfo.common.query.repository.mprd.BiMprdcdQryRepo;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustAgreeInfoSvcCntAllTrtCmdCntr {
	
	/* 고객 회선별 동의정보 처리작업 조회 Service*/
	private final BiCwkocustinfoagreeQryRepo cwkocustinfoagreeRepo;
	/* 서비스계약코드 조회 Service*/
	private final BiMprdcdQryRepo mprdcdQryRepo;
	/* 코드 조회 Service*/
	private final BiCsyscdQryRepo csyscdQryRepo;
	/* 개인정보보호관리 조회 Service*/
	private final BiCcpisecprivacyQryRepo ccpisecprivacyQryRepo;
	
	
	public void custAgreeInfoSvcCntAllTrt() {
		List<BiCwkocustinfoagreeQryOutDto> custAgreeInfoQueryOutDtoList = new ArrayList<>();
		
		/** 동의정보 현행화 대상 작업 테이블(BI_CWKOCUSTINFOAGREE) 조회*/
		try {
			custAgreeInfoQueryOutDtoList = cwkocustinfoagreeRepo.findByCustAgreeInfoAll();
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
			return;
		}
		//LogUtil.info("●●●●● {동의정보 현행화 대상 조회} ●●●●●", custAgreeInfoQueryOutDtoList.toString());
		if (ObjectUtils.isEmpty(custAgreeInfoQueryOutDtoList)) {
			LogUtil.info(" ●●●●●●●●●● 동의정보 현행화 대상 조회 데이타가 존재 하지 않습니다. ●●●●●●●●●● ");
		}
		else {
			LogUtil.debug("●●●●● {} ●●●●●", custAgreeInfoQueryOutDtoList.toString());
			LogUtil.debug(" ●●●●●●●●●● {} ::: [{}]{} ●●●●●●●●●● ", "동의정보 현행화 대상 작업 테이블 Total Size", custAgreeInfoQueryOutDtoList.size(), "건");
	
			/**
			 * @ 작업 처리 데이타의 유효성 검증 & 서비스 계약 동의정보 현행화
			 */		
			for (BiCwkocustinfoagreeQryOutDto dto : custAgreeInfoQueryOutDtoList) {
				
				/** 
				 * BI_CCPISECPRIVACY 유효성 검증 후 이력 생성
				 */
				String skipCode  = dto.getSkipCode();
				String skipMsg   = dto.getSkipMsg();
				String ifHstNo   = dto.getIfHstNo();
				String ifRegDate = dto.getIfRegDate();
				String saId      = dto.getSaId();
				String ordNo     = dto.getOrdNo();
				String regDate   = dto.getRegDate();
				
				try {
					dto = this.setPrivacy(this.chkDataValidation(dto));
				} catch(ParseException pe) {
					LogUtil.error(pe.getMessage(), pe);
					return;
				}
				LogUtil.info("●●●●● {결과 적용 업데이트 } ●●●●●", dto.toString());
				int updateCnt = 0;
				
				/** 
				 * BI_CWKOCUSTINFOAGREE 이력 Update
				 */
				if ("E".equals(dto.getSkipCode())) {
					updateCnt = this.update(skipCode, skipMsg, ifHstNo, ifRegDate, saId, ordNo, regDate);
				} else {
					dto.setSkipCode("1");
					updateCnt = this.update(skipCode, skipMsg, ifHstNo, ifRegDate, saId, ordNo, regDate);
				}
				
				if (updateCnt > 0) {
					LogUtil.info("●●●●● {} ●●●●●", "BI_CWKOCUSTINFOAGREE Update Success!");
				} else {
					LogUtil.info("●●●●● {} ●●●●●", "BI_CWKOCUSTINFOAGREE Update Fail!");
				}
			}
		}
	}
	
	@Transactional
	private BiCwkocustinfoagreeQryOutDto setPrivacy(BiCwkocustinfoagreeQryOutDto dto) {
		LogUtil.info("●●●●● {} ●●●●●", "custAgreeInfoSvcCntAllTrtTaskJobService.setPrivacy");
		
		int nMktUse = 0;
	    int nMktAd  = 0;
	    
		BiCwkocustinfoagreeQryOutDto outDto = new BiCwkocustinfoagreeQryOutDto();

	    BiCcpisecprivacyOutQryDto findByCcpisecprivacy = new BiCcpisecprivacyOutQryDto();

		try {
			findByCcpisecprivacy = ccpisecprivacyQryRepo.findByCcpisecprivacy(dto.getSaId());
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}

		if(ObjectUtils.isEmpty(findByCcpisecprivacy)) {
			LogUtil.debug("●●●●● findByCcpisecprivacy is null ●●●●●");
		} else {
			LogUtil.debug("●●●●● findByCcpisecprivacy : {} ●●●●●", findByCcpisecprivacy.toString());
			LogUtil.info("●●●●● findByCcpisecprivacy : {} ●●●●●", findByCcpisecprivacy.toString());
			
			if (StringUtils.isEmpty(findByCcpisecprivacy.getMktAdvmYn())) {
				findByCcpisecprivacy.setMktAdvmYn("N");	
			}
			if (StringUtils.isEmpty(findByCcpisecprivacy.getMktColecYn())) {
				findByCcpisecprivacy.setMktColecYn("N");	
			}
			
			/**
			 * @ 마케팅 수집이용 동의 
			 */
			if ("55".equals(dto.getCustInfoAgreeCd())) {
				if (!findByCcpisecprivacy.getMktColecYn().equals(dto.getCustInfoAgreeYn())) {
					nMktUse = 1;
				}
			}
			else if ("56".equals(dto.getCustInfoAgreeCd())) {
				if (!findByCcpisecprivacy.getMktAdvmYn().equals(dto.getCustInfoAgreeYn())) {
					nMktAd = 1;
				}
			}
			
			/**
			 * @ CDM 입수 시스템이 NS,RS 인 경우 동의 항목 동일 경우 SKIP
			 */
			if ( "NS".equals(dto.getOccSysDivVal()) && "RS".equals(dto.getOccSysDivVal()) ) {
				if (nMktUse == 0  && nMktAd == 0) {
					LogUtil.info("●●●●● {} ●●●●●", "NS,RS 처리 동의값  SKIP [ " + dto.getSaId() + " ]" );	
					// return
				} else {
					LogUtil.info("●●●●● {} ●●●●●", "NS,RS 처리 연관회선 이력 처리 REF");
					dto.setProcSys("REF");
					/* ICIS 처리분 추가 회선 REF로 이력 처리*/
					outDto = this.insert(dto, nMktUse, nMktAd, 1);
					LogUtil.info("●●●●● outDto : {} ●●●●●", outDto.toString());
				}
			} else {
				dto.setProcSys(dto.getIfOccSysDivVal());
				if (nMktUse == 0  && nMktAd == 0) {
					LogUtil.info("●●●●● {} ●●●●●", "타 시스템 처리 동의항목 동일 추가정보 UPDATE" );	
					
					/* 타시템 처리분 추가 항목 UPDATE*/
					this.insert(dto, nMktUse, nMktAd, 0);
				} else {
					LogUtil.info("●●●●● {} ●●●●●", "타 시스템 처리 동의항목 이력 처리" );	
					
					/* 타시템 처리분 이력 처리*/
					outDto = this.insert(dto, nMktUse, nMktAd, 1);
					LogUtil.info("●●●●● outDto : {} ●●●●●", outDto.toString());
				}
			}
		}
		return outDto;
	}
	
	private BiCwkocustinfoagreeQryOutDto chkDataValidation(BiCwkocustinfoagreeQryOutDto dto) 
			throws ParseException {
		
		LogUtil.info("●●●●● {} ●●●●●", "custAgreeInfoSvcCntAllTrtTaskJobService.chkDataValidation");

		/**
		 * @ 상품유형 체크 (유지형 모상품 처리만 처리 대상) 
		 */
		if (StringUtils.isEmpty(dto.getSaCd())) {
			dto.setSkipCode("E");
			dto.setSkipMsg("상품코드 미존재 처리 제외");
			return dto;
		}
		
		BiMprdcdQryOutDto mprdcdQueryOutDto = new BiMprdcdQryOutDto();

		/** BI_MPRDCD 조회*/
		try {
			mprdcdQueryOutDto = mprdcdQryRepo.findByMprdcdGetMenuProdTypeCd(dto.getSaCd());
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
			dto.setSkipCode("E");
			dto.setSkipMsg("상품코드 미존재 처리 제외");
			return dto;
		}
		
		if (ObjectUtils.isEmpty(mprdcdQueryOutDto)) {
			dto.setSkipCode("E");
			dto.setSkipMsg("모 상품 외 처리 제외");
			return dto;
		}
		
		if (!"0".equals(mprdcdQueryOutDto.getKosProdFlag())) {
			dto.setSkipCode("E");
			dto.setSkipMsg("KOS 상품 처리 제외");
			return dto;
		}
		
		LogUtil.debug("●●●●● mprdcdQueryOutDto : {} ●●●●●", mprdcdQueryOutDto.toString());
		
		/**
		 * @ 지능망 상품 제외 (912) 
		 */
		if (!StringUtils.isEmpty(dto.getSvcContDivCd())) {
			if ("912".equals(dto.getSvcContDivCd())) {
				dto.setSkipCode("E");
				dto.setSkipMsg("지능망회선 [ " + dto.getSvcContDivCd() + " ]  [ " + dto.getSaCd() + " ] 처리 제외");
				return dto;
			}
		} else {
			if ("C".equals(mprdcdQueryOutDto.getMenuProdTypeCd()) || "D".equals(mprdcdQueryOutDto.getMenuProdTypeCd())) {
				dto.setSkipCode("E");
				dto.setSkipMsg("지능망회선 [ " + dto.getSvcContDivCd() + " ]  [ " + dto.getSaCd() + " ] 처리 제외");
				return dto;
			}
		}
		
		/**
		 * @ 고객동의 대상 고객(개인) 여부 체크  
		 */
		if (StringUtils.isEmpty(dto.getCustNoType())) {
			dto.setSkipCode("E");
			dto.setSkipMsg("동의등록 대상 고객 정보 VW_BI_CCSTBASICINFO 미존재건 제외 [ " + dto.getCustNoType() + " ]");
			return dto;
		} else {
			if ( "1".equals(dto.getCustNoType()) && "2".equals(dto.getCustNoType()) && "5".equals(dto.getCustNoType()) 
					&& "7".equals(dto.getCustNoType())  ) {
				dto.setSkipCode("E");
				dto.setSkipMsg("동의등록 대상 고객 구분 [ " + dto.getCustNoType() + " ] 개인외 제외");
				return dto;
			}
		}
		
		/**
		 * @ 개인사업자 적용 기준정보 체크 
		 */
		String findByCsyscdGetBusiFlag = "";
		
		try {
			findByCsyscdGetBusiFlag = csyscdQryRepo.findByCsyscdGetBusiFlag();
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
			dto.setSkipCode("E");
			dto.setSkipMsg(e.getMessage().substring(0, 100));
			return dto;
		}
		
		if (StringUtils.isEmpty(findByCsyscdGetBusiFlag)) {
			dto.setSkipCode("E");
			dto.setSkipMsg("BI_CSYSCD 개인사업자 적용 여부 기준정보 조회 오류");
			return dto;
		}
		
		LogUtil.debug("●●●●● findByCsyscdGetBusiFlag : {} ●●●●●", findByCsyscdGetBusiFlag);
		
		if ( "1".equals(dto.getCustNoType())) {
			if ("11".equals(dto.getCustNoType()) && "Y".equals(findByCsyscdGetBusiFlag)) {
				dto.setSkipCode("E");
				dto.setSkipMsg("동의등록 대상 고객 구분 개인사업자 [ " + dto.getCustNoType() +" ] [ " +dto.getCustClassCd() + " ]" );
				return dto;
			}
		}
		
		/**
		 * @ 시스템  구분값 체크
		 */
		if (StringUtils.isEmpty(dto.getOccSysDivVal())) {
			dto.setSkipCode("E");
			dto.setSkipMsg("시스템 구분값 미정의 자료 제외");
			return dto;
		}
		
		/**
		 * @ 동의코드 및 동의값 체크 
		 */
		if ( !"55".equals(dto.getCustInfoAgreeCd()) &&  !"56".equals(dto.getCustInfoAgreeCd()) ) {
			dto.setSkipCode("E");
			dto.setSkipMsg("처리 대상 동의코드 아님 제외 [ " + dto.getCustInfoAgreeCd() + " ]");	
			return dto;		
		}
		
		/**
		 * @ 체널 및 버전정보 체크  
		 */
		if ( StringUtils.isEmpty(dto.getAgreeChCd()) || StringUtils.isEmpty(dto.getAgreeVerNo())) {
			dto.setSkipCode("E");
			dto.setSkipMsg("체널 및 버전정보 오류 체널 [ " + dto.getAgreeChCd() + " ] 버전 [ " +dto.getAgreeVerNo() + " ]");	
			return dto;		
		}
		
		/**
		 * @ 동일고객 동일동의코드에 대한 동일시간 중복 처리 체크
		 */
		String existCustAgreeInfo = "";

		try {
			existCustAgreeInfo = cwkocustinfoagreeRepo.existCustAgreeInfo(
				dto.getCustId(), dto.getCustInfoAgreeCd(), dto.getEfctStDt(), dto.getIfHstNo()
				);
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
		
		if (StringUtils.isEmpty(existCustAgreeInfo)) {
			dto.setSkipCode("E");
			dto.setSkipMsg("동일시간 중복건 Check Error");
		}
		
		if (Integer.valueOf(existCustAgreeInfo) > 0) {
			dto.setSkipCode("E");
			dto.setSkipMsg("동일시간 중복 자료 존재 처리 제외");
		}
		
		/**
		 * @ 타시스템 유효일시 이후 ICIS 등록 자료 체크
		 */
		if ( !"NS".equals(dto.getOccSysDivVal()) && !"RS".equals(dto.getOccSysDivVal()) ) {
			if ( "NS".equals(dto.getOccSysDivVal()) || "RS".equals(dto.getOccSysDivVal()) ) {
				SimpleDateFormat dateForm = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
				Date startDate = dateForm.parse(dto.getRegDate());
				Date latestChngDate = dateForm.parse(dto.getEfctStDt());
				int dateCompareTo = startDate.compareTo(latestChngDate);
				if (dateCompareTo > 0) {
					dto.setSkipCode("E");
					dto.setSkipMsg("타시스템 유효일시 이후 ICIS 등록 자료 제외");
					return dto;
				}
			}
		}

		dto.setSkipCode("S");
		LogUtil.debug("●●●●● dto : {} ●●●●●", dto.toString());

		return dto;
	}
	
	@Transactional
	private BiCwkocustinfoagreeQryOutDto insert(BiCwkocustinfoagreeQryOutDto inDto, int nMktUse, int nMktAd, int cnt) {
		LogUtil.info("●●●●● {} ●●●●●", "custAgreeInfoSvcCntAllTrtTaskJobService.insert");
		LogUtil.debug("●●●●● inDto : {} ●●●●●", inDto.toString());
		LogUtil.info("●●●●● inDto : {} ●●●●●", inDto.toString());
		
		String checkCnt = "";
		
		try {
			checkCnt = ccpisecprivacyQryRepo.existCcpidecprivacyCheck(inDto.getSaId(), inDto.getOrdNo());
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
			checkCnt = "1";
		}

		if ("1".equals(checkCnt)) {
			LogUtil.info("●●●●● {} ●●●●●", "Sleep?");
		}
		
		LogUtil.info("●●●●● checkCnt : {} ●●●●●", checkCnt);

		/**
		 * @ 동시처리자료에 대한 동일 계약 최종자료 등록일 조회
		 */
		String maxRegDate = "";
				
		try {
			maxRegDate = ccpisecprivacyQryRepo.findByCcpisecprivacyGetRegDate(inDto.getSaId(), inDto.getOrdNo());
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
			inDto.setSkipCode("E");
			inDto.setSkipMsg(e.getMessage().substring(0, 100));
			return inDto;
		}
		
		int updateCnt = 0;
		int insertCnt = 0;	
		
		BiCcpisecprivacy1QryEntt inEntt = new BiCcpisecprivacy1QryEntt();

		LogUtil.info("●●●●● cnt : {} ●●●●●", cnt);
		
		if (cnt == 0) {
			LogUtil.info("●●●●● {} ●●●●●", "BI_CCPISECPRIVACY UPDATE 타 시스템 동일값 처리 자료");
			try {
				updateCnt = ccpisecprivacyQryRepo.updateCcpisedcprivacyRegDate(inDto.getAgreeChCd(), inDto.getProcSys(), 
														inDto.getCustInfoAgreeCd(), inDto.getAgreeVerNo(), inDto.getSaId(), inDto.getOrdNo(), maxRegDate
														);
				LogUtil.info("●●●●● updateCnt : {} ●●●●●", updateCnt);
			} catch (Exception e) {
				LogUtil.error(e.getMessage(), e);
				inDto.setSkipCode("E");
				inDto.setSkipMsg(e.getMessage().substring(0, 100));
				LogUtil.info("BI_CCPISECPRIVACY UPDATE 타 시스템 동일값 처리 자료 수정 오류");
				return inDto;
			}
		} else {
			LogUtil.info("●●●●● {} ●●●●●", "BI_CCPISECPRIVACY UPDATE 이력처리");
			try {
				updateCnt = ccpisecprivacyQryRepo.updateCcpisecprivacy(inDto.getProcSys(), inDto.getSaId(), inDto.getOrdNo(), maxRegDate);
				LogUtil.info("●●●●● updateCnt : {} ●●●●●", updateCnt);
			} catch (Exception e) {
				LogUtil.error(e.getMessage(), e);
				inDto.setSkipCode("E");
				inDto.setSkipMsg(e.getMessage().substring(0, 100));
				return inDto;
			}

			/** Insert를 위한 Select*/
			LogUtil.info("●●●●● {} ●●●●●", "BI_CCPISECPRIVACY Insert를 위한 Select");
			try {
				inEntt = ccpisecprivacyQryRepo.findByCcpisecprivacyGetInsertValues(nMktUse, nMktAd, inDto.getCustInfoAgreeYn(), inDto.getAgreeChCd(), inDto.getProcSys(), 
									inDto.getCustInfoAgreeCd(), inDto.getAgreeVerNo(), inDto.getSaId(), inDto.getOrdNo(), maxRegDate);
			} catch (Exception e) {
				LogUtil.error(e.getMessage(), e);
				inDto.setSkipCode("E");
				inDto.setSkipMsg(e.getMessage().substring(0, 100));
				return inDto;
			}
			
			LogUtil.info("●●●●● inEntt : {} ●●●●●", inEntt.toString());

			/**Isnert*/
			LogUtil.info("●●●●● {} ●●●●●", "BI_CCPISECPRIVACY Insert 처리");
			try {
				insertCnt = ccpisecprivacyQryRepo.insertCcpisecprivacy(
						inEntt.getSaId(),
						inEntt.getOrdNo(),
						inEntt.getRegDate(),
						inEntt.getProcType(),
						inEntt.getRcverGubun(),
						inEntt.getRcvOfcCd(),
						inEntt.getRcverEmpNo(),
						inEntt.getBusiCd(),
						inEntt.getRcvMediaCd(),
						inEntt.getCustNoType(),
						inEntt.getCustNo(),
						inEntt.getPgmName(),
						inEntt.getCustId(),
						inEntt.getSvcNo(),
						inEntt.getCustInfoAgree(),
						inEntt.getCustInfoUseAgree(),
						inEntt.getCustInfoCommitAgree(),
						inEntt.getCustInfoKtgroupAgree(),
						inEntt.getProdInfoRcv(),
						inEntt.getCustInfoFurnishAgree(),
						inEntt.getCustInfoAdAgree(),
						inEntt.getCustInfoKtgroupAgree1(),
						inEntt.getCustInfoKtgroupAgree2(),
						inEntt.getCustInfoKtgroupAgree3(),
						inEntt.getCustInfoFurnishAgree1(),
						inEntt.getCustInfoFurnishAgree2(),
						inEntt.getCustInfoFnceAgree(),
						inEntt.getFrmpapTypeVal(),
						inEntt.getSvcContDivCd(),
						inEntt.getMrktgPtuseAdvmReagYn(),
						inEntt.getMrktgPtuseAdvmReagYn(),
						inEntt.getAgreeChCd(),					
						inEntt.getOccSysDivVal(),
						inEntt.getAgreeVerNo(),
						inEntt.getAdvmAgreeVerNo()					
						);
			} catch (Exception e) {
				LogUtil.error(e.getMessage(), e);
				inDto.setSkipCode("E");
				inDto.setSkipMsg(e.getMessage().substring(0, 100));
				return inDto;
			}
		}
		
		if (insertCnt > 0) {
			inDto.setSkipCode("S");
		} else {
			inDto.setSkipCode("E");
			inDto.setSkipMsg("BI_CCPISECPRIVACY 테이블 Insert Error");
		}
		
		LogUtil.debug("●●●●● outDto : {} ●●●●●", inDto.toString());
		return inDto;
	}
	
	@Transactional
	private int update(String skipCode, String skipMsg, String ifHstNo, String ifRegDate, String saId, String ordNo, String regDate) {
		LogUtil.info("●●●●● {} ●●●●●", "custAgreeInfoSvcCntAllTrtTaskJobService.update Start!");
		int cnt = 0;
		try {
			cnt = cwkocustinfoagreeRepo.updateCwkocustinfoagree(
					skipCode
				  , skipMsg
				  , ifHstNo
				  , ifRegDate
				  , saId
				  , ordNo
				  , regDate
					);
			LogUtil.debug("●●●●● updateCwkocustinfoagree cnt : {} ●●●●●", cnt);
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
			return cnt;
		}
		return cnt;
	}

}
