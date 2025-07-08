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

package com.kt.icis.oder.baseinfo.csnh.command.controller;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.kt.icis.cmmnfrwk.utils.LogUtil;
import com.kt.icis.oder.baseinfo.common.command.repository.dto.cloc.ClocbldgCmdDto;
import com.kt.icis.oder.baseinfo.common.command.repository.dto.cloc.ClocbldghistCmdDto;
import com.kt.icis.oder.baseinfo.common.command.repository.dto.cloc.ClocsdmbldgCmdDto;
import com.kt.icis.oder.baseinfo.common.command.repository.dto.cloc.ClocsdmbldgsimiljibunCmdDto;
import com.kt.icis.oder.baseinfo.common.command.repository.dto.cloc.ClocsdmbldgsimilnmCmdDto;
import com.kt.icis.oder.baseinfo.common.command.service.cloc.ClocbldgCmdSvc;
import com.kt.icis.oder.baseinfo.common.command.service.cloc.ClocbldghistCmdSvc;
import com.kt.icis.oder.baseinfo.common.command.service.cloc.ClocsdmbldgCmdSvc;
import com.kt.icis.oder.baseinfo.common.command.service.cloc.ClocsdmbldgsimiljibunCmdSvc;
import com.kt.icis.oder.baseinfo.common.command.service.cloc.ClocsdmbldgsimilnmCmdSvc;
import com.kt.icis.oder.baseinfo.common.command.service.cwko.CwkosdmbldgchgCmdSvc;
import com.kt.icis.oder.baseinfo.common.query.repository.dto.cloc.BiClocbldgScopeQryDto;
import com.kt.icis.oder.baseinfo.common.query.repository.dto.cloc.BiClocbldgStEdBldgNoQryDto;
import com.kt.icis.oder.baseinfo.common.query.repository.dto.cloc.BiClocbldghistQryDto;
import com.kt.icis.oder.baseinfo.common.query.repository.dto.cwko.BiCwkosdmbldgchgIfQryDto;
import com.kt.icis.oder.baseinfo.common.query.repository.entity.cloc.BiClocbldgQryEntt;
import com.kt.icis.oder.baseinfo.common.query.service.cloc.BiClocbldgQrySvc;
import com.kt.icis.oder.baseinfo.common.query.service.cwco.BiCwkosdmbldgchgQrySvc;
import com.kt.icis.oder.common.exception.OrderException;
import com.kt.icis.oder.common.utils.DateUtil;
import com.kt.icis.oder.common.utils.StringUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SdmBldgInfoTrCmdCntr {
	
	private final BiCwkosdmbldgchgQrySvc cwkosdmbldgchgQrySvc;
	private final CwkosdmbldgchgCmdSvc cwkosdmbldgchgCmdSvc;
	private final BiClocbldgQrySvc biClocbldgQrySvc;
	private final ClocbldgCmdSvc clocbldgCmdSvc;
	private final ClocsdmbldgCmdSvc clocsdmbldgCmdSvc;
	private final ClocbldghistCmdSvc clocbldghistCmdSvc;
	private final ClocsdmbldgsimilnmCmdSvc clocsdmbldgsimilnmCmdSvc;
	private final ClocsdmbldgsimiljibunCmdSvc clocsdmbldgsimiljibunCmdSvc;
	
	private enum Err{
		ADDR("4001"),   /* 오류유형-주소정보오류 */
		HIER("4002"),    /* 오류유형-계층화오류 */
        REV("4003"),    /* 오류유형-선행요청오류 */
		TRAN("4004");    /* 오류유형-처리유형 부정확 */
		
		private String type;		
		private Err(String type) {
			this.type = type;
		}
		public String getType() {
			return this.type;
		}
	}
	
	private enum ReqType{
		INSERT("I"),   /* 요청유형-등록 */
		UPDATE("U"),   /* 요청유형-수정 */
		DELETE("D"),   /* 요청유형-삭제 */
		INTGRT("T");   /* 요청유형-통합 */
		
		private String type;
		private ReqType(String type) {
			this.type = type;
		}
		public String getType() {
			return this.type;
		}
	}
	
	private enum HistType{
		INSERT("C"),   /* 이력유형-등록 */
		UPDATE("U"),   /* 이력유형-수정 */
		DELETE("D"),   /* 이력유형-삭제 */
		INTGRT("T");   /* 이력유형-통합 */
		
		private String type;
		private HistType(String type) {
			this.type = type;
		}
		public String getType() {
			return this.type;
		}
	}
	
	private enum BldgFlag{
		REPR("R"),    /* 건물유형-대표건물 */
		NAME("S"),    /* 건물유형-유사명칭 */
		JIBN("J");    /* 건물유형-유사지번 */
		
		private String flag;
		private BldgFlag(String flag) {
			this.flag = flag;
		}
		public String getFlag() {
			return this.flag;
		}
	}
	
	private enum Rslt{
		DEFAULT("0"),  /* DEFAULT */
		SUCC("S"),     /* 처리결과-성공 */
		ADDR("A"),     /* 처리결과-주소오류 */
		HIER("H"),     /* 처리결과-계층화오류 */
		PREV("P"),     /* 처리결과-선행요청오류 */
		NOFO("N"),     /* 처리결과-미존재건물 */
		FAIL("F"),     /* 처리결과-실패 */
		NOT_FOUND("T");/* 처리결과-DB NOT FOUND */
		
		private final String flag;
		private Rslt(String flag) {
			this.flag = flag;
		}
		public String getFlag() {
			return this.flag;
		}
		public static Rslt getRslt(String flag) {
			for(Rslt rslt:Rslt.values()) {
				if(rslt.getFlag().equals(flag)) {
					return rslt;
				}
			}		
			throw new OrderException("정의되지 않은 결과 상태코드[%s]입니다".formatted(flag));
		}
	}
		
	//@Value("${oder.batch.csnh040d.fetch_limit}")
    private int fetchLimit;
    
    //@Value("${oder.batch.csnh040d.program_id}")
    private String programId;
	
	/**
	 * 대표건물 CRUD
	 * @tuxedo CSNH040D
	 * @param icisBldgDao
	 * @param sdmBldgDao
	 */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
	public void processBldgRepr(BiClocbldgScopeQryDto icisBldgDao, BiCwkosdmbldgchgIfQryDto sdmBldgDao) {		
		if(this.isTopBldgUpdate(sdmBldgDao)) {
			this.updateTopBldg(icisBldgDao, sdmBldgDao);
		}
		
		if(this.isBefTopBldgUpdate(icisBldgDao, sdmBldgDao)) {
			this.updateBefTopBldg(icisBldgDao);
		}
		
		if(this.isChildBldgUpdate(icisBldgDao, sdmBldgDao)) {
			this.updateChildBldg(icisBldgDao);
		}
				
		if(ReqType.INSERT.getType().equals(sdmBldgDao.getReqTypeFlag())) {
			this.createClocsdmbldg(sdmBldgDao);			
			this.createClocbldgHist(icisBldgDao);			
			this.createClocbldg(icisBldgDao);	
		}else if(ReqType.UPDATE.getType().equals(sdmBldgDao.getReqTypeFlag())) {
			this.updateClocsdmbldg(sdmBldgDao);			
			this.updateClocbldgHist(icisBldgDao);
			this.updateClocbldg(icisBldgDao);	// 건물이름을 START_BLDG_NO 오류		
		}else if(ReqType.DELETE.getType().equals(sdmBldgDao.getReqTypeFlag())) {
			this.deleteClocsdmbldg(sdmBldgDao);			
			this.deleteClocbldgHist(icisBldgDao);
			this.deleteClocbldg(icisBldgDao);
		}else if(ReqType.INTGRT.getType().equals(sdmBldgDao.getReqTypeFlag())) {
			this.deleteClocsdmbldg(sdmBldgDao);			
			this.integrateClocbldgHist(icisBldgDao);
			this.deleteClocbldg(icisBldgDao);
		}else{
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("유효하지 않은 요청: REQ_TYPE_FLAG(%s)".formatted(sdmBldgDao.getReqTypeFlag())));
		}
	}
	
	/**
	 * SDM 대표건물 생성
	 * @tuxedo CSNH040D
	 * @param sdmBldgDao
	 */
	public void createClocsdmbldg(BiCwkosdmbldgchgIfQryDto sdmBldgDao) {
		
		try {
			ClocsdmbldgCmdDto clocsdmbldgCmdDto = ClocsdmbldgCmdDto.builder()
					.bldgId(sdmBldgDao.getBldgId())
					.bDongCd(sdmBldgDao.getDongCd())
					.arnoId(sdmBldgDao.getArnoId())
					.addrNoType(sdmBldgDao.getAddrNoType())
					.addrNo(sdmBldgDao.getAddrNo())
					.addrHo(sdmBldgDao.getAddrHo())
					.addrRef(sdmBldgDao.getAddrRef())
					.bldgTypeCd(sdmBldgDao.getBldgTypeCd())
					.bldgName(sdmBldgDao.getBldgName())
					.bldngName(sdmBldgDao.getSdmBldngName())
					.zipcdId(sdmBldgDao.getZipcdId())
					.upBldgId(sdmBldgDao.getUpBldgId())
					.topBldgId(sdmBldgDao.getTopBldgId())
					.lstBldgYn(sdmBldgDao.getLstBldgYn())
					.startDate(sdmBldgDao.getReqDate())				
					.regEmpNo("CSNH040D")
					.eaiReqNo(sdmBldgDao.getReqNo())
					.acptOfcCd(sdmBldgDao.getAcptOfcCd())
					.instlEngnTypeCd(sdmBldgDao.getInstlEngnTypeCd())
					.cmplnBldgYn(sdmBldgDao.getCmplnBldgYn())
					.pnlDay(sdmBldgDao.getPnlDay())
					.uniteNetFlag(sdmBldgDao.getUniteNetFlag())
					.bldgCsType(sdmBldgDao.getBldgCsType())
					.rbldgId(sdmBldgDao.getRbldgId())
					.rbldgNmId(sdmBldgDao.getRbldgNmId())
					.roadNmId(sdmBldgDao.getRoadNmId())
					.basementFlag(sdmBldgDao.getBasementFlag())
					.rbldgNo(sdmBldgDao.getRbldgNo())
					.rbldgHo(sdmBldgDao.getRbldgHo())
					.locType(sdmBldgDao.getLocType())
					.willYn(sdmBldgDao.getWillYn())
					.unityBldgId(sdmBldgDao.getUnityBldgId())
					.build();				
			
			clocsdmbldgCmdSvc.insertClocsdmbldg(clocsdmbldgCmdDto);
		}catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("INSERT 건물정보 ERROR"));
		}
	}
	
	/**
	 * 대표건물 생성
	 * @tuxedo CSNH040D
	 * @param icisBldgDao
	 */
	public void createClocbldg(BiClocbldgScopeQryDto icisBldgDao) {		
		try {
			Timestamp startDate = (icisBldgDao.getStartDate() == null) ? null : DateUtil.stringToTimestamp(icisBldgDao.getStartDate());
			Timestamp endDate = (icisBldgDao.getEndDate() == null) ? null : DateUtil.stringToTimestamp(icisBldgDao.getEndDate());
			
			ClocbldgCmdDto clocbldgCmdDto = ClocbldgCmdDto.builder()
					.bldgId(icisBldgDao.getBldgId())
					.addrBldgNo("01")
					.bDongCd(icisBldgDao.getBDongCd())
					.addrNoType(icisBldgDao.getAddrNoType())
					.addrNo(icisBldgDao.getAddrNo())
					.addrHo(icisBldgDao.getAddrHo())
					.bldgTypeCd(icisBldgDao.getBldgTypeCd())
					.bldgName(icisBldgDao.getBldgName())
					.hDongCd(icisBldgDao.getHDongCd())
					.acptOfcCd(icisBldgDao.getAcptOfcCd())
					.prodList(icisBldgDao.getProdList())
					.zip(icisBldgDao.getZip())
					.startBldgNo(icisBldgDao.getStartBldgNo())
					.endBldgNo(icisBldgDao.getEndBldgNo())
					.pBldgId(icisBldgDao.getPBldgId())
					.startDate(startDate)
					.endDate(endDate)				
					.regOfcCd(icisBldgDao.getRegOfcCd())
					.regerEmpNo(icisBldgDao.getRegerEmpNo())
					.regerEmpName(icisBldgDao.getRegerEmpName())				
					.mngEmpNo(icisBldgDao.getMngEmpNo())
					.lifeCycle(icisBldgDao.getLifeCycle())
					.infoCd(icisBldgDao.getInfoCd())
					.topBldgId(icisBldgDao.getTopBldgId())
					.lstBldgYn(icisBldgDao.getLstBldgYn())
					.listYn(icisBldgDao.getListYn())
					.cmplnBldgYn(icisBldgDao.getCmplnBldgYn())
					.pnlDay(icisBldgDao.getPnlDay())
					.uniteNetFlag(icisBldgDao.getUniteNetFlag())
					.bldgCsType(icisBldgDao.getBldgCsType())
					.arnoId(icisBldgDao.getArnoId())
					.rbldgId(icisBldgDao.getRbldgId())
					.rbldgNmId(icisBldgDao.getRbldgNmId())
					.roadNmId(icisBldgDao.getRoadNmId())
					.basementFlag(icisBldgDao.getBasementFlag())
					.rbldgNo(icisBldgDao.getRbldgNo())
					.rbldgHo(icisBldgDao.getRbldgHo())
					.gigaFlag(icisBldgDao.getGigaFlag())
					.saleStartDate(icisBldgDao.getSaleStartDate())
					.locType(icisBldgDao.getLocType())
					.willYn(icisBldgDao.getWillYn())
					.unityBldgId(icisBldgDao.getUnityBldgId())
					.build();
					
			clocbldgCmdSvc.insertClocbldg(clocbldgCmdDto);
		}catch(Exception e) { 
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("INSERT 건물정보 ERROR"));
		}
	}
	
	/**
	 * 대표건물 History 생성
	 * @tuxedo CSNH040D
	 * @param icisBldgDao
	 */
	public void createClocbldgHist(BiClocbldgScopeQryDto icisBldgDao) {		
		try {
			String acptOfcCd = icisBldgDao.getAcptOfcCd();
			if(acptOfcCd == null) {
				acptOfcCd = "000000";
			}
			
			ClocbldghistCmdDto clocbldghistCmdDto = ClocbldghistCmdDto.builder()
					.bldgId(icisBldgDao.getBldgId())
					.bldgIdNo("01")			
					.masterIdFlag("1")
					.acptOfcCd(acptOfcCd)
					.cudType(HistType.INSERT.getType())
					.bDongCd(icisBldgDao.getBDongCd())
					.beDongCd(icisBldgDao.getBDongCd())
					.beAddrNoType(icisBldgDao.getAddrNoType())
					.beAddrNo(icisBldgDao.getAddrNo())
					.beAddrHo(icisBldgDao.getAddrHo())
					.beBldgTypeCd(icisBldgDao.getBldgTypeCd())
					.beBldgName(icisBldgDao.getBldgName())
					.afAddrNoType(icisBldgDao.getAddrNoType())
					.afAddrNo(icisBldgDao.getAddrNo())
					.afAddrHo(icisBldgDao.getAddrHo())
					.afBldgTypeCd(icisBldgDao.getBldgTypeCd())
					.afBldgName(icisBldgDao.getBldgName())
					.regOfcCd(icisBldgDao.getRegOfcCd())
					.regerEmpNo(icisBldgDao.getRegerEmpNo())
					.regerEmpName(icisBldgDao.getRegerEmpName())
					.afBldgId(icisBldgDao.getBldgId())
					.afBldgIdNo("01")
					.infoCd(icisBldgDao.getInfoCd())
					.uniteNetFlag(icisBldgDao.getUniteNetFlag())
					.bldgCsType(icisBldgDao.getBldgCsType())
					.beArnoId(icisBldgDao.getArnoId())
					.beRbldgId(icisBldgDao.getRbldgId())
					.beRbldgNmId(icisBldgDao.getRbldgNmId())
					.beRoadNmId(icisBldgDao.getRoadNmId())
					.beBasementFlag(icisBldgDao.getBasementFlag())
					.beRbldgNo(icisBldgDao.getRbldgNo())
					.beRbldgHo(icisBldgDao.getRbldgHo())
					.afArnoId(icisBldgDao.getArnoId())
					.afRbldgId(icisBldgDao.getRbldgId())
					.afRbldgNmId(icisBldgDao.getRbldgNmId())
					.afRoadNmId(icisBldgDao.getRoadNmId())
					.afBasementFlag(icisBldgDao.getBasementFlag())
					.afRbldgNo(icisBldgDao.getRbldgNo())
					.afRbldgHo(icisBldgDao.getRbldgHo())
					.unityBldgId(icisBldgDao.getUnityBldgId())
					.build();
			
			clocbldghistCmdSvc.insertClocbldghist(clocbldghistCmdDto);
			
		}catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("INSERT 건물정보 변경이력 ERROR"));
		}
	}
	
	/**
	 * SDM 대표건물 UPDATE
	 * @tuxedo CSNH040D
	 * @param sdmBldgDao
	 */
	public void updateClocsdmbldg(BiCwkosdmbldgchgIfQryDto sdmBldgDao) {
		try {		
			ClocsdmbldgCmdDto clocsdmbldgCmdDto = ClocsdmbldgCmdDto.builder()
					.bDongCd(sdmBldgDao.getDongCd())
					.arnoId(sdmBldgDao.getArnoId())
					.addrNoType(sdmBldgDao.getAddrNoType())
					.addrNo(sdmBldgDao.getAddrNo())
					.addrHo(sdmBldgDao.getAddrHo())
					.addrRef(sdmBldgDao.getAddrRef())
					.bldgTypeCd(sdmBldgDao.getBldgTypeCd())
					.bldgName(sdmBldgDao.getBldgName())
					.bldngName(sdmBldgDao.getBldngName())
					.zipcdId(sdmBldgDao.getZipcdId())
					.upBldgId(sdmBldgDao.getUpBldgId())
					.topBldgId(sdmBldgDao.getTopBldgId())
					.lstBldgYn(sdmBldgDao.getLstBldgYn())
					.regEmpNo(this.programId)
					.eaiReqNo(sdmBldgDao.getReqNo())
					.acptOfcCd(sdmBldgDao.getAcptOfcCd())
					.instlEngnTypeCd(sdmBldgDao.getInstlEngnTypeCd())				
					.cmplnBldgYn(sdmBldgDao.getCmplnBldgYn())
					.pnlDay(sdmBldgDao.getPnlDay())
					.uniteNetFlag(sdmBldgDao.getUniteNetFlag())
					.bldgCsType(sdmBldgDao.getBldgCsType())
					.rbldgId(sdmBldgDao.getRbldgId())
					.rbldgNmId(sdmBldgDao.getRbldgNmId())
					.roadNmId(sdmBldgDao.getRoadNmId())
					.basementFlag(sdmBldgDao.getBasementFlag())
					.rbldgNo(sdmBldgDao.getRbldgNo())
					.rbldgHo(sdmBldgDao.getRbldgHo())
					.locType(sdmBldgDao.getLocType())
					.willYn(sdmBldgDao.getWillYn())
					.unityBldgId(sdmBldgDao.getUnityBldgId())				
					.bldgId(sdmBldgDao.getBldgId())
					.build();
					
					clocsdmbldgCmdSvc.updateClocsdmbldg(clocsdmbldgCmdDto);
		}catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("UPDATE 건물정보 ERROR"));
		}
	}
	
	/**
	 * 대표건물 UPDATE
	 * @tuxedo CSNH040D
	 * @param sdmBldgDao
	 */
	public void updateClocbldg(BiClocbldgScopeQryDto icisBldgDao) {
		try {
			ClocbldgCmdDto clocbldgCmdDto = ClocbldgCmdDto.builder()
					.bDongCd(icisBldgDao.getBDongCd())
					.addrNoType(icisBldgDao.getAddrNoType())
					.addrNo(icisBldgDao.getAddrNo())
					.addrHo(icisBldgDao.getAddrHo())
					.bldgTypeCd(icisBldgDao.getBldgTypeCd())
					.bldgName(icisBldgDao.getBldgName())
					.hDongCd(icisBldgDao.getHDongCd())
					.acptOfcCd(icisBldgDao.getAcptOfcCd())
					.zip(icisBldgDao.getZip())
					.startBldgNo(icisBldgDao.getStartBldgNo())
					.endBldgNo(icisBldgDao.getEndBldgNo())
					.pBldgId(icisBldgDao.getPBldgId())
					.regerEmpNo(this.programId)
					.topBldgId(icisBldgDao.getTopBldgId())
					.lstBldgYn(icisBldgDao.getLstBldgYn())
					.listYn(icisBldgDao.getListYn())
					.prodList(icisBldgDao.getProdList())
					.cmplnBldgYn(icisBldgDao.getCmplnBldgYn())
					.pnlDay(icisBldgDao.getPnlDay())
					.uniteNetFlag(icisBldgDao.getUniteNetFlag())
					.bldgCsType(icisBldgDao.getBldgCsType())
					.arnoId(icisBldgDao.getArnoId())
					.rbldgId(icisBldgDao.getRbldgId())
					.rbldgNmId(icisBldgDao.getRbldgNmId())
					.roadNmId(icisBldgDao.getRoadNmId())
					.basementFlag(icisBldgDao.getBasementFlag())
					.rbldgNo(icisBldgDao.getRbldgNo())
					.rbldgHo(icisBldgDao.getRbldgHo())
					.gigaFlag(icisBldgDao.getGigaFlag())
					.saleStartDate(icisBldgDao.getSaleStartDate())
					.locType(icisBldgDao.getLocType())
					.willYn(icisBldgDao.getWillYn())
					.unityBldgId(icisBldgDao.getUnityBldgId())
					.bldgId(icisBldgDao.getBldgId())
					.build();
			
			this.clocbldgCmdSvc.updateClocbldg(clocbldgCmdDto);
		}catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("UPDATE 건물정보 ERROR"));
		}
	}
	
	/**
	 * 대표건물 History 생성
	 * @tuxedo CSNH040D
	 * @param icisBldgDao
	 */
	public void updateClocbldgHist(BiClocbldgScopeQryDto icisBldgDao) {
		List<BiClocbldgQryEntt> clocbldgQryEnttList = this.biClocbldgQrySvc.findByBldgId(icisBldgDao.getBldgId());
		
		for(BiClocbldgQryEntt entt:clocbldgQryEnttList) {
			this.updateClocbldgHist(entt, icisBldgDao);
		}
	}
	
	/**
	 * 대표건물 History 생성
	 * @tuxedo CSNH040D
	 * @param icisBldgDao
	 */
	public void updateClocbldgHist(BiClocbldgQryEntt clocbldgQryEntt, BiClocbldgScopeQryDto icisBldgDao) {
		try {
			String acptOfcCd = icisBldgDao.getAcptOfcCd();
			if(acptOfcCd == null) {
				acptOfcCd = "000000";
			}
			
			ClocbldghistCmdDto clocbldghistCmdDto = ClocbldghistCmdDto.builder()
					.bldgId(clocbldgQryEntt.getBldgId())
					.bldgIdNo("01")			
					.masterIdFlag("1")
					.acptOfcCd(acptOfcCd)
					.cudType(HistType.UPDATE.getType())
					.bDongCd(clocbldgQryEntt.getBDongCd())
					.beDongCd(icisBldgDao.getBDongCd())
					.beAddrNoType(clocbldgQryEntt.getAddrNoType())
					.beAddrNo(clocbldgQryEntt.getAddrNo())
					.beAddrHo(clocbldgQryEntt.getAddrHo())
					.beBldgTypeCd(clocbldgQryEntt.getBldgTypeCd())
					.beBldgName(clocbldgQryEntt.getBldgName())
					.afAddrNoType(icisBldgDao.getAddrNoType())
					.afAddrNo(icisBldgDao.getAddrNo())
					.afAddrHo(icisBldgDao.getAddrHo())
					.afBldgTypeCd(icisBldgDao.getBldgTypeCd())
					.afBldgName(icisBldgDao.getBldgName())
					.regOfcCd(icisBldgDao.getRegOfcCd())
					.regerEmpNo(icisBldgDao.getRegerEmpNo())
					.regerEmpName(icisBldgDao.getRegerEmpName())
					.afBldgId(clocbldgQryEntt.getBldgId())
					.afBldgIdNo("01")
					.infoCd(icisBldgDao.getInfoCd())
					.uniteNetFlag(icisBldgDao.getUniteNetFlag())
					.bldgCsType(icisBldgDao.getBldgCsType())
					.beArnoId(clocbldgQryEntt.getArnoId())
					.beRbldgId(clocbldgQryEntt.getRbldgId())
					.beRbldgNmId(clocbldgQryEntt.getRbldgNmId())
					.beRoadNmId(clocbldgQryEntt.getRoadNmId())
					.beBasementFlag(clocbldgQryEntt.getBasementFlag())
					.beRbldgNo(clocbldgQryEntt.getRbldgNo())
					.beRbldgHo(clocbldgQryEntt.getRbldgHo())
					.afArnoId(icisBldgDao.getArnoId())
					.afRbldgId(icisBldgDao.getRbldgId())
					.afRbldgNmId(icisBldgDao.getRbldgNmId())
					.afRoadNmId(icisBldgDao.getRoadNmId())
					.afBasementFlag(icisBldgDao.getBasementFlag())
					.afRbldgNo(icisBldgDao.getRbldgNo())
					.afRbldgHo(icisBldgDao.getRbldgHo())
					.unityBldgId(icisBldgDao.getUnityBldgId())
					.build();
			
			clocbldghistCmdSvc.insertClocbldghist(clocbldghistCmdDto);
		}catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("INSERT 건물정보 변경이력 ERROR"));
		}
	}
	
	/**
	 * SDM 대표건물 삭제
	 * @tuxedo CSNH040D
	 * @param icisBldgDao
	 */
	public void deleteClocsdmbldg(BiCwkosdmbldgchgIfQryDto sdmBldgDao) {
		
		try {
			this.clocsdmbldgCmdSvc.updateClocsdmbldgUnityBldg(sdmBldgDao.getBldgId(), sdmBldgDao.getUnityBldgId(), sdmBldgDao.getReqNo(), this.programId, sdmBldgDao.getReqDate());
		}catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("UPDATE 건물정보 ERROR"));
		}
	}
	
	/**
	 * 대표건물 삭제
	 * @tuxedo CSNH040D
	 * @param icisBldgDao
	 */
	public void deleteClocbldg(BiClocbldgScopeQryDto icisBldgDao) {
		try {
			clocbldgCmdSvc.updateClocbldgUnityBldgEndDate(icisBldgDao.getBldgId(), icisBldgDao.getUnityBldgId(), this.programId, icisBldgDao.getEndDate());
		}catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("UPDATE 건물정보 ERROR"));
		}
	}
	
	/**
	 * 대표건물 History 생성
	 * @tuxedo CSNH040D
	 * @param icisBldgDao
	 */
	public void deleteClocbldgHist(BiClocbldgScopeQryDto icisBldgDao) {
		List<BiClocbldgQryEntt> clocbldgQryEnttList = this.biClocbldgQrySvc.findByBldgIdAndEndDateIsNull(icisBldgDao.getBldgId());
		
		for(BiClocbldgQryEntt entt:clocbldgQryEnttList) {
			this.deleteClocbldgHist(entt, icisBldgDao);
		}
	}
	
	/**
	 * 대표건물 History 생성
	 * @tuxedo CSNH040D
	 * @param icisBldgDao
	 */
	public void deleteClocbldgHist(BiClocbldgQryEntt clocbldgQryEntt, BiClocbldgScopeQryDto icisBldgDao) {
		try {
			String acptOfcCd = icisBldgDao.getAcptOfcCd();
			if(acptOfcCd == null) {
				acptOfcCd = "000000";
			}
			
			ClocbldghistCmdDto clocbldghistCmdDto = ClocbldghistCmdDto.builder()
					.bldgId(clocbldgQryEntt.getBldgId())
					.bldgIdNo("01")			
					.masterIdFlag("1")
					.acptOfcCd(acptOfcCd)
					.cudType(HistType.DELETE.getType())
					.bDongCd(icisBldgDao.getBDongCd())
					.beDongCd(icisBldgDao.getBDongCd())
					.beAddrNoType(icisBldgDao.getAddrNoType())
					.beAddrNo(icisBldgDao.getAddrNo())
					.beAddrHo(icisBldgDao.getAddrHo())
					.beBldgTypeCd(icisBldgDao.getBldgTypeCd())
					.beBldgName(icisBldgDao.getBldgName())
					.afAddrNoType(icisBldgDao.getAddrNoType())
					.afAddrNo(icisBldgDao.getAddrNo())
					.afAddrHo(icisBldgDao.getAddrHo())
					.afBldgTypeCd(icisBldgDao.getBldgTypeCd())
					.afBldgName(icisBldgDao.getBldgName())
					.regOfcCd(icisBldgDao.getRegOfcCd())
					.regerEmpNo(icisBldgDao.getRegerEmpNo())
					.regerEmpName(icisBldgDao.getRegerEmpName())
					.afBldgId(clocbldgQryEntt.getBldgId())
					.afBldgIdNo("01")
					.infoCd(icisBldgDao.getInfoCd())
					.uniteNetFlag(icisBldgDao.getUniteNetFlag())
					.bldgCsType(icisBldgDao.getBldgCsType())
					.beArnoId(icisBldgDao.getArnoId())
					.beRbldgId(icisBldgDao.getRbldgId())
					.beRbldgNmId(icisBldgDao.getRbldgNmId())
					.beRoadNmId(icisBldgDao.getRoadNmId())
					.beBasementFlag(icisBldgDao.getBasementFlag())
					.beRbldgNo(icisBldgDao.getRbldgNo())
					.beRbldgHo(icisBldgDao.getRbldgHo())
					.afArnoId(icisBldgDao.getArnoId())
					.afRbldgId(icisBldgDao.getRbldgId())
					.afRbldgNmId(icisBldgDao.getRbldgNmId())
					.afRoadNmId(icisBldgDao.getRoadNmId())
					.afBasementFlag(icisBldgDao.getBasementFlag())
					.afRbldgNo(icisBldgDao.getRbldgNo())
					.afRbldgHo(icisBldgDao.getRbldgHo())
					.unityBldgId(icisBldgDao.getUnityBldgId())
					.build();
			
			clocbldghistCmdSvc.insertClocbldghist(clocbldghistCmdDto);
		}catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("INSERT 건물정보 변경이력 ERROR"));
		}
	}
	
	/**
	 * 대표 건물 통합 History 생성
	 * @tuxedo CSNH040D
	 * @param icisBldgDao
	 */
	public void integrateClocbldgHist(BiClocbldgScopeQryDto icisBldgDao) {
		List<BiClocbldghistQryDto> clocbldghistQryDtoList = this.biClocbldgQrySvc.selectClocbldghistByClocbldg(icisBldgDao.getBldgId(), icisBldgDao.getIntgBldgId());

		for(BiClocbldghistQryDto clocbldghistQryDto:clocbldghistQryDtoList) {
			this.integrateClocbldgHist(clocbldghistQryDto, icisBldgDao);
		}
	}
	
	/**
	 * 대표 건물 통합 History 생성
	 * @tuxedo CSNH040D 
	 * @param clocbldghistQryDto
	 * @param icisBldgDao
	 */
	public void integrateClocbldgHist(BiClocbldghistQryDto clocbldghistQryDto, BiClocbldgScopeQryDto icisBldgDao) {
		try {
			String acptOfcCd = icisBldgDao.getAcptOfcCd();
			if(acptOfcCd == null) {
				acptOfcCd = "000000";
			}
			
			ClocbldghistCmdDto clocbldghistCmdDto = ClocbldghistCmdDto.builder()
					.bldgId(clocbldghistQryDto.getBldgId())
					.bldgIdNo("01")			
					.masterIdFlag("1")
					.acptOfcCd(acptOfcCd)
					.cudType(HistType.INTGRT.getType())
					.bDongCd(clocbldghistQryDto.getBDongCd())
					.beDongCd(clocbldghistQryDto.getBeDongCd())
					.beAddrNoType(clocbldghistQryDto.getBeAddrNoType())
					.beAddrNo(clocbldghistQryDto.getBeAddrNo())
					.beAddrHo(clocbldghistQryDto.getBeAddrHo())
					.beBldgTypeCd(clocbldghistQryDto.getBeBldgTypeCd())
					.beBldgName(clocbldghistQryDto.getBeBldgName())
					.afAddrNoType(clocbldghistQryDto.getAfAddrNoType())
					.afAddrNo(clocbldghistQryDto.getAfAddrNo())
					.afAddrHo(clocbldghistQryDto.getAfAddrHo())
					.afBldgTypeCd(clocbldghistQryDto.getAfBldgTypeCd())
					.afBldgName(clocbldghistQryDto.getAfBldgName())					
					.regOfcCd(icisBldgDao.getRegOfcCd())
					.regerEmpNo(icisBldgDao.getRegerEmpNo())
					.regerEmpName(icisBldgDao.getRegerEmpName())
					.afBldgId(clocbldghistQryDto.getAfBldgId())
					.afBldgIdNo("01")
					.infoCd(clocbldghistQryDto.getInfoCd())
					.uniteNetFlag(clocbldghistQryDto.getUniteNetFlag())
					.bldgCsType(clocbldghistQryDto.getBldgCsType())					
					.beArnoId(clocbldghistQryDto.getBeArnoId())
					.beRbldgId(clocbldghistQryDto.getBeRbldgId())
					.beRbldgNmId(clocbldghistQryDto.getBeRbldgNmId())
					.beRoadNmId(clocbldghistQryDto.getBeRoadNmId())
					.beBasementFlag(clocbldghistQryDto.getBeBasementFlag())
					.beRbldgNo(clocbldghistQryDto.getBeRbldgNo())
					.beRbldgHo(clocbldghistQryDto.getBeRbldgHo())
					.afArnoId(clocbldghistQryDto.getAfArnoId())
					.afRbldgId(clocbldghistQryDto.getAfRbldgId())
					.afRbldgNmId(clocbldghistQryDto.getAfRbldgNmId())
					.afRoadNmId(clocbldghistQryDto.getAfRoadNmId())
					.afBasementFlag(clocbldghistQryDto.getAfBasementFlag())
					.afRbldgNo(clocbldghistQryDto.getAfRbldgNo())
					.afRbldgHo(clocbldghistQryDto.getAfRbldgHo())
					.unityBldgId(clocbldghistQryDto.getUnityBldgId())
					.build();
			
			clocbldghistCmdSvc.insertClocbldghist(clocbldghistCmdDto);
		}catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("INSERT 건물정보HIST ERROR"));
		}
	}
	
	/**
	 * 하위 건물 여부
	 * @param icisBldgDao
	 * @param sdmBldgDao
	 * @return
	 */
	public boolean isChildBldgUpdate(BiClocbldgScopeQryDto icisBldgDao, BiCwkosdmbldgchgIfQryDto sdmBldgDao) {
		
		/* IF 최상위건물이 아님 THEN 미처리 */
		if(!sdmBldgDao.getTopBldgId().equals(sdmBldgDao.getBldgId())) {
			return false;
		}
		/* IF 처리유형이 삭제 THEN 미처리 */
		if(ReqType.DELETE.getType().equals(sdmBldgDao.getReqTypeFlag()) || ReqType.INTGRT.getType().equals(sdmBldgDao.getReqTypeFlag())) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 하위건물 UPDATE
	 * @tuxedo CSNH040D 
	 * @param icisBldgDao
	 */
	public void updateChildBldg(BiClocbldgScopeQryDto icisBldgDao) {
		try {								
			clocbldgCmdSvc.updateClocbldgRegerEmpNoListYn(icisBldgDao.getTopBldgId(), icisBldgDao.getRegerEmpNo(), icisBldgDao.getBDongCd(), icisBldgDao.getAddrNoType(), icisBldgDao.getAddrNo(), icisBldgDao.getAddrHo());
		}catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("UPDATE 건물정보(CHILD)"));	
		}
	}
	
	/**
	 * 최상위 건물 여부
	 * @tuxedo CSNH040D
	 * @param sdmBldgDao
	 * @return
	 */
	public boolean isTopBldgUpdate(BiCwkosdmbldgchgIfQryDto sdmBldgDao) {
		if(sdmBldgDao.getTopBldgId().equals(sdmBldgDao.getBldgId())) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 이전 최상위 건물 여부
	 * @param icisBldgDao
	 * @param sdmBldgDao
	 * @return
	 */
	public boolean isBefTopBldgUpdate(BiClocbldgScopeQryDto icisBldgDao, BiCwkosdmbldgchgIfQryDto sdmBldgDao) {
				
		/* IF 처리유형이 변경이 아님 THEN 미처리 */
		if(!ReqType.UPDATE.getType().equals(sdmBldgDao.getReqTypeFlag())) {
			return false;
		}		
		/* IF 처리대상이 최상위 건물 THEN 미처리 */		
	    if (icisBldgDao.getBldgId().equals(icisBldgDao.getBefTopBldgId())){
	    	return false;
	    }
	    /* IF 최상위건물 변경이 아님 THEN 미처리 */
	    if (icisBldgDao.getTopBldgId().equals(icisBldgDao.getBefTopBldgId())) {
	    	return false;
	    }
		
		return true;
	}
	
	public void updateTopBldg(BiClocbldgScopeQryDto icisBldgDao, BiCwkosdmbldgchgIfQryDto sdmBldgDao) {
		
		String startBldgNo = icisBldgDao.getFstBldngName();
		String endBldgNo   = icisBldgDao.getLstBldngName();
		
		try {
			if(!ReqType.DELETE.getType().equals(sdmBldgDao.getReqTypeFlag()) && !ReqType.INTGRT.getType().equals(sdmBldgDao.getReqTypeFlag())) {
				startBldgNo = icisBldgDao.getMinBldngName();
				endBldgNo   = icisBldgDao.getMaxBldngName();
			}
					
			clocbldgCmdSvc.updateClocbldgStartEndBldgNo(icisBldgDao.getTopBldgId(), startBldgNo, endBldgNo, icisBldgDao.getRegerEmpNo(), "Y");
		}catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("UPDATE 건물정보(TOP)"));	
		}
	}
	
	/**
	 * 이전 최상위 건물 UPDATE
	 * @tuxedo CSNH040D 
	 * @param icisBldgDao
	 */
	public void updateBefTopBldg(BiClocbldgScopeQryDto icisBldgDao) {
		
		try {
			BiClocbldgStEdBldgNoQryDto clocbldgStEdBldgNoQryDao = this.getStEdBldgNo(icisBldgDao);	
			
			if(clocbldgStEdBldgNoQryDao == null) {
				throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("UPDATE 건물정보(BEF_TOP)"));
			}
			
			clocbldgCmdSvc.updateClocbldgStartEndBldgNo(icisBldgDao.getBefTopBldgId(), clocbldgStEdBldgNoQryDao.getStartBldgNo(), clocbldgStEdBldgNoQryDao.getEndBldgNo(), icisBldgDao.getRegerEmpNo());
		}catch(OrderException oe) {
			throw oe;
		}catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("UPDATE 건물정보(BEF_TOP)"));
		}
	}
	
	/**
	 * 최상위 건물 및 하위 건물 조건 조회
	 * @tuxedo CSNH040D
	 * @param icisBldgDao
	 * @return
	 */
	public BiClocbldgStEdBldgNoQryDto getStEdBldgNo(BiClocbldgScopeQryDto icisBldgDao) {
		BiClocbldgStEdBldgNoQryDto clocbldgStEdBldgNoQryDto = null;
		
		try {
			clocbldgStEdBldgNoQryDto = this.biClocbldgQrySvc.selectClocbldgStEdBldg(icisBldgDao.getBefTopBldgId(), icisBldgDao.getTopBldgId(), icisBldgDao.getBldgId());
		}catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("UPDATE 건물정보(BEF_TOP)"));
		}
		
		return clocbldgStEdBldgNoQryDto;
	}
	
	/**
	 * 유사명칭 등록, 수정, 삭제
	 * @tuxedo CSNH040D 
	 * @param sdmBldgDao
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void processBldgName(BiCwkosdmbldgchgIfQryDto sdmBldgDao) {
		if(ReqType.INSERT.getType().equals(sdmBldgDao.getReqTypeFlag())) {
			this.createClocsdmbldgsimilnm(sdmBldgDao);
		}else if(ReqType.UPDATE.getType().equals(sdmBldgDao.getReqTypeFlag())) {
			this.updateClocsdmbldgsimilnm(sdmBldgDao);
		}else if(ReqType.DELETE.getType().equals(sdmBldgDao.getReqTypeFlag())) {
			this.deleteClocsdmbldgsimilnm(sdmBldgDao);
		}else{
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("유효하지 않은 요청: REQ_TYPE_FLAG(%s)".formatted(sdmBldgDao.getReqTypeFlag())));
		}
	}
	
	/**
	 * 유사명칭 건물 생성
	 * @tuxedo CSNH040D
	 * @param sdmBldgDao
	 */
	public void createClocsdmbldgsimilnm(BiCwkosdmbldgchgIfQryDto sdmBldgDao) {		
		
		try {
			ClocsdmbldgsimilnmCmdDto clocsdmbldgsimilnmCmdDto = ClocsdmbldgsimilnmCmdDto.builder()
					.bldgId(sdmBldgDao.getBldgId())
					.bldgNameSimilNo(sdmBldgDao.getBldgNameSimilNo())
					.bldgSimilName(sdmBldgDao.getBldgName())
					.startDate(sdmBldgDao.getReqDate())
					.regEmpNo(this.programId)
					.eaiReqNo(sdmBldgDao.getReqNo())
					.build();
			
			clocsdmbldgsimilnmCmdSvc.insertClocsdmbldgsimilnm(clocsdmbldgsimilnmCmdDto);
		}catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("INSERT 건물정보테이블 ERROR"));
		}
	}
	
	/**
	 * 유사건물 명칭 UPDATE
	 * @tuxedo CSNH040D
	 * @param sdmBldgDao
	 */
	public void updateClocsdmbldgsimilnm(BiCwkosdmbldgchgIfQryDto sdmBldgDao) {
		int updateCount;
		try {
			updateCount = clocsdmbldgsimilnmCmdSvc.updateClocsdmbldgsimilnmBldgSimilName(
								sdmBldgDao.getBldgId(), 
								sdmBldgDao.getBldgNameSimilNo(),
								sdmBldgDao.getReqNo(),
								this.programId,
								sdmBldgDao.getBldgName());
			
			if(updateCount < 1) {
				throw new OrderException(Rslt.NOT_FOUND.getFlag(), Arrays.asList("UPDATE 건물정보테이블 ERROR"));
			}			
		}catch(OrderException oe) {
			throw oe;
		}
		catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("UPDATE 건물정보테이블 ERROR"));
		}
	}
	
	/**
	 * 유사건물 명칭 삭제
	 * @tuxedo CSNH040D 
	 * @param sdmBldgDao
	 */
	public void deleteClocsdmbldgsimilnm(BiCwkosdmbldgchgIfQryDto sdmBldgDao) {
		try {
			clocsdmbldgsimilnmCmdSvc.updateClocsdmbldgsimilnmEndDate(
					sdmBldgDao.getBldgId(),
					sdmBldgDao.getBldgNameSimilNo(),
					sdmBldgDao.getReqNo(),
					this.programId,
					sdmBldgDao.getReqDate());
		}catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("UPDATE 건물정보테이블 ERROR"));
		}
	}
	
	/**
	 * 유사지번 등록, 수정, 삭제
	 * @tuxedo CSNH040D 
	 * @param sdmBldgDao
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void processBldgJibun(BiCwkosdmbldgchgIfQryDto sdmBldgDao) {
		if(ReqType.INSERT.getType().equals(sdmBldgDao.getReqTypeFlag())) {
			this.createClocsdmbldgsimilJibun(sdmBldgDao);
		}else if(ReqType.UPDATE.getType().equals(sdmBldgDao.getReqTypeFlag())) {
			this.updateClocsdmbldgsimilJibun(sdmBldgDao);
		}else if(ReqType.DELETE.getType().equals(sdmBldgDao.getReqTypeFlag())) {
			this.deleteClocsdmbldgsimilJibun(sdmBldgDao);
		}else{
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("유효하지 않은 요청: REQ_TYPE_FLAG(%s)".formatted(sdmBldgDao.getReqTypeFlag())));
		}
	}
	
	/**
	 * 유사지번 등록
	 * @tuxedo CSNH040D 
	 * @param sdmBldgDao
	 */
	public void createClocsdmbldgsimilJibun(BiCwkosdmbldgchgIfQryDto sdmBldgDao) {
		try {
			ClocsdmbldgsimiljibunCmdDto clocsdmbldgsimiljibunCmdDto =  ClocsdmbldgsimiljibunCmdDto.builder()
					.bldgId(sdmBldgDao.getBldgId())
					.bldgSimilArnoNo(sdmBldgDao.getBldgSimilArnoNo())
					.addrNoType(sdmBldgDao.getAddrNoType())
					.addrNo(sdmBldgDao.getAddrNo())
					.addrHo(sdmBldgDao.getAddrHo())
					.arnoId(sdmBldgDao.getArnoId())
					.rbldgId(sdmBldgDao.getRbldgId())
					.rbldgNmId(sdmBldgDao.getRbldgNmId())
					.roadNmId(sdmBldgDao.getRoadNmId())
					.basementFlag(sdmBldgDao.getBasementFlag())
					.rbldgNo(sdmBldgDao.getRbldgNo())
					.rbldgHo(sdmBldgDao.getRbldgHo())
					.startDate(sdmBldgDao.getReqDate())
					.regEmpNo(this.programId)					
					.eaiReqNo(sdmBldgDao.getReqNo())
					.build();
			
			clocsdmbldgsimiljibunCmdSvc.insertClocsdmbldgsimiljibun(clocsdmbldgsimiljibunCmdDto);			
		}catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("INSERT 건물유사지번정보 ERROR"));
		}
	}
	
	/**
	 * 유사지번 수정
	 * @tuxedo CSNH040D 
	 * @param sdmBldgDao
	 */
	public void updateClocsdmbldgsimilJibun(BiCwkosdmbldgchgIfQryDto sdmBldgDao) {
		int updateCount;
		
		try {
			ClocsdmbldgsimiljibunCmdDto clocsdmbldgsimiljibunCmdDto =  ClocsdmbldgsimiljibunCmdDto.builder()
					.addrNoType(sdmBldgDao.getAddrNoType())
					.addrNo(sdmBldgDao.getAddrNo())
					.addrHo(sdmBldgDao.getAddrHo())
					.arnoId(sdmBldgDao.getArnoId())
					.rbldgId(sdmBldgDao.getRbldgId())
					.rbldgNmId(sdmBldgDao.getRbldgNmId())
					.roadNmId(sdmBldgDao.getRoadNmId())
					.basementFlag(sdmBldgDao.getBasementFlag())
					.rbldgNo(sdmBldgDao.getRbldgNo())
					.rbldgHo(sdmBldgDao.getRbldgHo())
					.regEmpNo(this.programId)					
					.eaiReqNo(sdmBldgDao.getReqNo())
					.bldgId(sdmBldgDao.getBldgId())
					.bldgSimilArnoNo(sdmBldgDao.getBldgSimilArnoNo())
					.build();
			
			updateCount = clocsdmbldgsimiljibunCmdSvc.updateClocsdmbldgsimiljibun(clocsdmbldgsimiljibunCmdDto);
			
			if(updateCount < 1) {
				throw new OrderException(Rslt.NOT_FOUND.getFlag(), Arrays.asList("UPDATE 건물유사지번정보 ERROR"));
			}
		}catch(OrderException oe) {
			throw oe;
		}catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("UPDATE 건물유사지번정보 ERROR"));
		}
	}
	
	/**
	 * 유사지번 삭제
	 * @tuxedo CSNH040D 
	 * @param sdmBldgDao
	 */
	public void deleteClocsdmbldgsimilJibun(BiCwkosdmbldgchgIfQryDto sdmBldgDao) {
		int updateCount;		
		try {					
			updateCount = clocsdmbldgsimiljibunCmdSvc.updateClocsdmbldgsimiljibunEndDate(
								sdmBldgDao.getBldgId(),
								sdmBldgDao.getBldgSimilArnoNo(),
								sdmBldgDao.getReqDate(),
								this.programId,
								sdmBldgDao.getReqNo());
			
			if(updateCount < 1) {
				throw new OrderException(Rslt.NOT_FOUND.getFlag(), Arrays.asList("UPDATE 건물유사지번정보 ERROR"));
			}
		}catch(OrderException oe) {
			throw oe;
		}catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("UPDATE 건물유사지번정보 ERROR"));
		}
	}
	
	/**
	 * 건물 연동 결과 등록
	 * @tuxedo CSNH040D 
	 * @param cwkosdmbldgchgIfQryDto
	 * @param rsltFlag
	 * @param errMessage
	 */
	public void updateCwkoSdmbldgchgRslt(BiCwkosdmbldgchgIfQryDto cwkosdmbldgchgIfQryDto, Rslt rsltFlag, String errMessage) {
		String rsltFlagCd = rsltFlag.getFlag();
		
		try {
			if(rsltFlag == Rslt.NOT_FOUND && !ReqType.INSERT.getType().equals(cwkosdmbldgchgIfQryDto.getReqTypeFlag())) {
				String prevReqNo = this.getCwkoSdmbldgchgReqNo(cwkosdmbldgchgIfQryDto.getBldgId(), cwkosdmbldgchgIfQryDto.getReqNo(), Rslt.SUCC.getFlag(), ReqType.INSERT.getType());
				
				if(StringUtils.isEmpty(prevReqNo)) {
					rsltFlagCd = Rslt.NOFO.getFlag(); //미존재 건물
				}else {
					rsltFlagCd = Rslt.PREV.getFlag(); //선행요청오류
				}
			}
			cwkosdmbldgchgCmdSvc.updateCwkosdmbldgchgRslt(cwkosdmbldgchgIfQryDto.getReqNo(), rsltFlagCd, errMessage, cwkosdmbldgchgIfQryDto.getTranDate(), cwkosdmbldgchgIfQryDto.getBldngName());
		}catch(OrderException oe) {
			throw oe;
		}catch(Exception e) {
			throw new OrderException("MZDBE0001",Arrays.asList("건물 연동 결과 등록"));
		}
	}
	
	/**
	 * 건물연동 요청 번호 조회
	 * @tuxedo CSNH040D 
	 * @param bldgId
	 * @param reqNo
	 * @param rsltFlag
	 * @param reqTypeFlag
	 * @return
	 */
	public String getCwkoSdmbldgchgReqNo(String bldgId, String reqNo, String rsltFlag, String reqTypeFlag) {
		String prevReqNo = "";		
		try {
			prevReqNo = cwkosdmbldgchgQrySvc.selectCwkosdmbldgchgReqNoRownum1(bldgId, reqNo, rsltFlag, reqTypeFlag);
		}catch(Exception e) {
			throw new OrderException("MZDBE0001",Arrays.asList("건물연동 요청 번호 조회"));
		}
		
		return prevReqNo;
	}
	
	public String getErrorMessage(OrderException oe) {
		String errMessage = "";
		if(oe.getErrMsgArr() != null) {
			errMessage = StringUtils.defaultString(oe.getErrMsgArr().get(0));
		}
		return StringUtils.defaultString(oe.getCode()) + ":" + errMessage;
	}
	
	/**
	 * 연동요청 건물 조회
	 * @tuxedo CSNH040D
	 * @param cwkosdmbldgchgIfQryDto
	 * @return
	 */
	public BiClocbldgScopeQryDto getClocbldg(BiCwkosdmbldgchgIfQryDto cwkosdmbldgchgIfQryDto) {
		
		BiClocbldgScopeQryDto biClocbldgScopeQryDto;
		try {
			biClocbldgScopeQryDto = this.biClocbldgQrySvc.selectClocbldgScope(cwkosdmbldgchgIfQryDto.getBldgId(), cwkosdmbldgchgIfQryDto.getTopBldgId(), cwkosdmbldgchgIfQryDto.getBldngName(), cwkosdmbldgchgIfQryDto.getReqTypeFlag(), ReqType.UPDATE.getType(), cwkosdmbldgchgIfQryDto.getZipcdId(), cwkosdmbldgchgIfQryDto.getArnoId());
			biClocbldgScopeQryDto = this.getReprClocbldg(biClocbldgScopeQryDto, cwkosdmbldgchgIfQryDto);

		}catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("SELECT 건물정보 ERROR)"));
		}
		return biClocbldgScopeQryDto;
	}
	
	/**
	 * 연동요청 정보 MAPPING
	 * @tuxedo CSNH040D 
	 * @param biClocbldgScopeQryDto
	 * @param cwkosdmbldgchgIfQryDto
	 * @return
	 */
	public BiClocbldgScopeQryDto getReprClocbldg(BiClocbldgScopeQryDto biClocbldgScopeQryDto, BiCwkosdmbldgchgIfQryDto cwkosdmbldgchgIfQryDto) {
		BiClocbldgScopeQryDto reprClocbldgQryDto = new BiClocbldgScopeQryDto();	
		BeanUtils.copyProperties(biClocbldgScopeQryDto, reprClocbldgQryDto);
		
		if (biClocbldgScopeQryDto.getMinBldngName() == null) {
			reprClocbldgQryDto.setMinBldngName(biClocbldgScopeQryDto.getMaxBldngName());
		}		
		if (biClocbldgScopeQryDto.getMaxBldngName() == null) {
			reprClocbldgQryDto.setMaxBldngName(biClocbldgScopeQryDto.getMinBldngName());
		}
		if (biClocbldgScopeQryDto.getFstBldngName() == null) {
			reprClocbldgQryDto.setFstBldngName(biClocbldgScopeQryDto.getLstBldngName());
		}
		if (biClocbldgScopeQryDto.getLstBldngName() == null) {
			reprClocbldgQryDto.setLstBldngName(biClocbldgScopeQryDto.getFstBldngName());
		}
		
		reprClocbldgQryDto.setBldgId(cwkosdmbldgchgIfQryDto.getBldgId());
		reprClocbldgQryDto.setBDongCd(cwkosdmbldgchgIfQryDto.getDongCd());
		reprClocbldgQryDto.setAddrNoType(cwkosdmbldgchgIfQryDto.getAddrNoType());
		reprClocbldgQryDto.setAddrNo(cwkosdmbldgchgIfQryDto.getAddrNo());
		reprClocbldgQryDto.setBldgTypeCd(cwkosdmbldgchgIfQryDto.getBldgTypeCd());
		reprClocbldgQryDto.setBldgName(cwkosdmbldgchgIfQryDto.getBldgName());
		reprClocbldgQryDto.setArnoId(cwkosdmbldgchgIfQryDto.getArnoId());
		reprClocbldgQryDto.setRbldgId(cwkosdmbldgchgIfQryDto.getRbldgId());
		reprClocbldgQryDto.setRbldgNmId(cwkosdmbldgchgIfQryDto.getRbldgNmId());
		reprClocbldgQryDto.setRoadNmId(cwkosdmbldgchgIfQryDto.getRoadNmId());
		reprClocbldgQryDto.setBasementFlag(cwkosdmbldgchgIfQryDto.getBasementFlag());
		reprClocbldgQryDto.setRbldgNo(cwkosdmbldgchgIfQryDto.getRbldgNo());
		reprClocbldgQryDto.setRbldgHo(cwkosdmbldgchgIfQryDto.getRbldgHo());
	    
		if(cwkosdmbldgchgIfQryDto.getTopBldgId().equals(cwkosdmbldgchgIfQryDto.getBldgId())) {
			reprClocbldgQryDto.setStartBldgNo(reprClocbldgQryDto.getMinBldngName());
			reprClocbldgQryDto.setEndBldgNo(reprClocbldgQryDto.getMaxBldngName());
		}else{
			reprClocbldgQryDto.setStartBldgNo(cwkosdmbldgchgIfQryDto.getBldngName());
			reprClocbldgQryDto.setEndBldgNo(cwkosdmbldgchgIfQryDto.getBldngName());
		}
		
		reprClocbldgQryDto.setPBldgId(cwkosdmbldgchgIfQryDto.getUpBldgId());
		reprClocbldgQryDto.setStartDate(cwkosdmbldgchgIfQryDto.getReqDate());
		reprClocbldgQryDto.setRegerEmpNo(this.programId);
		
		if(ReqType.DELETE.getType().equals(cwkosdmbldgchgIfQryDto.getReqTypeFlag()) ||
		   ReqType.INTGRT.getType().equals(cwkosdmbldgchgIfQryDto.getReqTypeFlag())) {
			reprClocbldgQryDto.setEndDate(cwkosdmbldgchgIfQryDto.getReqDate());
			reprClocbldgQryDto.setLifeCycle("D");
		}
		
		reprClocbldgQryDto.setTopBldgId(cwkosdmbldgchgIfQryDto.getTopBldgId());
		reprClocbldgQryDto.setLstBldgYn(cwkosdmbldgchgIfQryDto.getLstBldgYn());
		
		reprClocbldgQryDto.setAcptOfcCd(cwkosdmbldgchgIfQryDto.getAcptOfcCd());
		reprClocbldgQryDto.setProdList(cwkosdmbldgchgIfQryDto.getInstlEngnTypeCd());
		reprClocbldgQryDto.setIntgBldgId(cwkosdmbldgchgIfQryDto.getIntgBldgId());
		reprClocbldgQryDto.setCmplnBldgYn(cwkosdmbldgchgIfQryDto.getCmplnBldgYn());
		reprClocbldgQryDto.setPnlDay(cwkosdmbldgchgIfQryDto.getPnlDay());
		reprClocbldgQryDto.setUniteNetFlag(cwkosdmbldgchgIfQryDto.getUniteNetFlag());
		reprClocbldgQryDto.setBldgCsType(cwkosdmbldgchgIfQryDto.getBldgCsType());
		
		reprClocbldgQryDto.setGigaFlag(cwkosdmbldgchgIfQryDto.getGigaFlag());
		reprClocbldgQryDto.setSaleStartDate(cwkosdmbldgchgIfQryDto.getSaleStartDate());
		reprClocbldgQryDto.setLocType(cwkosdmbldgchgIfQryDto.getLocType());
		reprClocbldgQryDto.setWillYn(cwkosdmbldgchgIfQryDto.getWillYn());
		reprClocbldgQryDto.setUnityBldgId(cwkosdmbldgchgIfQryDto.getUnityBldgId());

		if(this.isList(biClocbldgScopeQryDto, cwkosdmbldgchgIfQryDto)) {
			reprClocbldgQryDto.setListYn("Y");
		}
		
		return reprClocbldgQryDto;
	}
	
	/**
	 * 하위건물 출력 여부
	 * @tuxedo CSNH040D
	 * @param biClocbldgScopeQryDto
	 * @param cwkosdmbldgchgIfQryDto
	 * @return
	 */
	public boolean isList(BiClocbldgScopeQryDto biClocbldgScopeQryDto, BiCwkosdmbldgchgIfQryDto cwkosdmbldgchgIfQryDto) {
		if( !"2".equals(cwkosdmbldgchgIfQryDto.getBldgTypeCd()) && !"L".equals(cwkosdmbldgchgIfQryDto.getBldgTypeCd())) {
			return true;
		}
		
		if(cwkosdmbldgchgIfQryDto.getTopBldgId().equals(cwkosdmbldgchgIfQryDto.getBldgId())){
			if(biClocbldgScopeQryDto.getChldBldgCnt() > 0) {
				return true;
			}
			if("Y".equals(cwkosdmbldgchgIfQryDto.getLstBldgYn())) {
				return true;
			}
		} else {
			if(cwkosdmbldgchgIfQryDto.getBldgName() == null) {
				return true;
			}			
			if("C".equals(cwkosdmbldgchgIfQryDto.getBldgTypeCd()) || "N".equals(cwkosdmbldgchgIfQryDto.getBldgTypeCd())) {
				return true;
			}
			if(biClocbldgScopeQryDto.getTBldgId() != null &&
			   (biClocbldgScopeQryDto.getTBDongCd().equals(biClocbldgScopeQryDto.getBDongCd())       ||
				biClocbldgScopeQryDto.getTAddrNoType().equals(biClocbldgScopeQryDto.getAddrNoType()) ||
				biClocbldgScopeQryDto.getTAddrNo().equals(biClocbldgScopeQryDto.getAddrNo())         ||
				biClocbldgScopeQryDto.getTAddrHo().equals(biClocbldgScopeQryDto.getAddrHo()))) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 에러 UPDATE
	 * @tuxedo CSNH040D
	 * @param cwkosdmbldgchgIfQryDto
	 */
	public void updateCwkosdmbldgchgErr(BiCwkosdmbldgchgIfQryDto cwkosdmbldgchgIfQryDto) {		
		try {
			String errMsg = "계층화오류, BLDG_TYPE_CD: [%s], LST_BLDG_YN: [%s]".formatted(cwkosdmbldgchgIfQryDto.getBldgTypeCd(), cwkosdmbldgchgIfQryDto.getReqTypeFlag());
			cwkosdmbldgchgCmdSvc.updateCwkosdmbldgchgErrMsg(cwkosdmbldgchgIfQryDto.getReqNo(), errMsg);
		}catch(Exception e) {
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("건물연동 정보 업데이트"));
		}
	}
	
	/**
	 * 대표 건물 여부
	 * @tuxedo CSNH040D
	 * @param cwkosdmbldgchgIfQryDto
	 * @return
	 */
	public boolean checkReprBldgFlag(BiCwkosdmbldgchgIfQryDto cwkosdmbldgchgIfQryDto) {
		if(cwkosdmbldgchgIfQryDto.getDongCd() == null      ||
		   cwkosdmbldgchgIfQryDto.getAddrNoType() == null  ||
		   cwkosdmbldgchgIfQryDto.getAddrNo() == null      ||
		   cwkosdmbldgchgIfQryDto.getBldgTypeCd() == null  ||
		   cwkosdmbldgchgIfQryDto.getBldgName() == null) {
			return false;
		}		
		return true;
	}
	
	/**
	 * 유사명칭 등록 체크
	 * @tuxedo CSNH040D 
	 * @param cwkosdmbldgchgIfQryDto
	 * @return
	 */
	public boolean checkNameBldgFlag(BiCwkosdmbldgchgIfQryDto cwkosdmbldgchgIfQryDto) {		
		if(cwkosdmbldgchgIfQryDto.getBldgName() == null) {
			return false;
		}		
		return true;
	}
	
    /**
     * 유사지번 등록 체크
     * @tuxedo CSNH040D
     * @param cwkosdmbldgchgIfQryDto
     * @return
     */
	public boolean checkJibnBldgFlag(BiCwkosdmbldgchgIfQryDto cwkosdmbldgchgIfQryDto) {
		if(cwkosdmbldgchgIfQryDto.getAddrNoType() == null ||  cwkosdmbldgchgIfQryDto.getAddrNo() == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * 연동요청에 대한 적합성 체크
	 * @tuxedo CSNH040D 
	 * @param cwkosdmbldgchgIfQryDto
	 */
	public void checkRequest(BiCwkosdmbldgchgIfQryDto cwkosdmbldgchgIfQryDto) {		
		
		if("N".equals(cwkosdmbldgchgIfQryDto.getLstBldgYn())   &&
		   !"2".equals(cwkosdmbldgchgIfQryDto.getBldgTypeCd()) &&
		   !"L".equals(cwkosdmbldgchgIfQryDto.getBldgTypeCd()) &&
		   !ReqType.DELETE.getType().equals(cwkosdmbldgchgIfQryDto.getReqTypeFlag()) &&
		   !ReqType.INTGRT.getType().equals(cwkosdmbldgchgIfQryDto.getReqTypeFlag())) {
			this.updateCwkosdmbldgchgErr(cwkosdmbldgchgIfQryDto);
			return;
		}
		
		if(!ReqType.DELETE.getType().equals(cwkosdmbldgchgIfQryDto.getReqTypeFlag()) && 
		   !ReqType.INTGRT.getType().equals(cwkosdmbldgchgIfQryDto.getReqTypeFlag())) {
			
			if( BldgFlag.REPR.getFlag().equals(cwkosdmbldgchgIfQryDto.getReprBldgFlag()) && !this.checkReprBldgFlag(cwkosdmbldgchgIfQryDto)){
				throw new OrderException(Rslt.ADDR.getFlag(), Arrays.asList("B_DONG_CD: [%s], ADDR_NO_TYPE: [%s], ADDR_NO: [%s], BLDG_TYPE_CD: [%s], BLDG_NAME: [%s]"
						.formatted(cwkosdmbldgchgIfQryDto.getDongCd(),
								   cwkosdmbldgchgIfQryDto.getAddrNoType(),
								   cwkosdmbldgchgIfQryDto.getAddrNo(),
								   cwkosdmbldgchgIfQryDto.getBldgTypeCd(),
								   cwkosdmbldgchgIfQryDto.getBldgName())));
			}
			
			if( BldgFlag.NAME.getFlag().equals(cwkosdmbldgchgIfQryDto.getReprBldgFlag()) && !this.checkNameBldgFlag(cwkosdmbldgchgIfQryDto)){
				throw new OrderException(Rslt.ADDR.getFlag(), Arrays.asList("BLDG_NAME: [%s]".formatted(cwkosdmbldgchgIfQryDto.getBldgName())));
			}
			
			if( BldgFlag.JIBN.getFlag().equals(cwkosdmbldgchgIfQryDto.getReprBldgFlag()) && !this.checkJibnBldgFlag(cwkosdmbldgchgIfQryDto)){
				throw new OrderException(Rslt.ADDR.getFlag(), Arrays.asList("ADDR_NO_TYPE: [%s], ADDR_NO: [%s]".formatted(cwkosdmbldgchgIfQryDto.getAddrNoType(), cwkosdmbldgchgIfQryDto.getAddrNo())));
			}
			
			if(!BldgFlag.REPR.getFlag().equals(cwkosdmbldgchgIfQryDto.getReprBldgFlag()) &&
			   !BldgFlag.NAME.getFlag().equals(cwkosdmbldgchgIfQryDto.getReprBldgFlag()) &&
			   !BldgFlag.JIBN.getFlag().equals(cwkosdmbldgchgIfQryDto.getReprBldgFlag())) {
				throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("유효하지 않은 요청: REPR_BLDG_FLAG(%s)".formatted(cwkosdmbldgchgIfQryDto.getReprBldgFlag())));
			}
		}
		
		if(ReqType.INTGRT.getType().equals(cwkosdmbldgchgIfQryDto.getReqTypeFlag())) {
			if(cwkosdmbldgchgIfQryDto.getBldgNameSimilNo() != null ||
			   cwkosdmbldgchgIfQryDto.getBldgSimilArnoNo() != null ||
			   !BldgFlag.REPR.getFlag().equals(cwkosdmbldgchgIfQryDto.getReprBldgFlag())){
				
				throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("유효하지 않은 요청: REPR_BLDG_FLAG(%s)".formatted(cwkosdmbldgchgIfQryDto.getReprBldgFlag())));
			}
			
			if(cwkosdmbldgchgIfQryDto.getBldgId() == null) {
				throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("변경전 건물 미존재(%s)".formatted(cwkosdmbldgchgIfQryDto.getBldgId())));
			}
		}		  
	}
}
