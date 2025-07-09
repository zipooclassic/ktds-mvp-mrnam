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

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.kt.icis.cmmnfrwk.utils.LogUtil;
import com.kt.icis.oder.baseinfo.common.command.service.cloc.ClocbldgCmdSvc;
import com.kt.icis.oder.baseinfo.common.command.service.cloc.ClocbldghistCmdSvc;
import com.kt.icis.oder.baseinfo.common.command.service.cloc.ClocsdmbldgCmdSvc;
import com.kt.icis.oder.baseinfo.common.command.service.cloc.ClocsdmbldgsimiljibunCmdSvc;
import com.kt.icis.oder.baseinfo.common.command.service.cloc.ClocsdmbldgsimilnmCmdSvc;
import com.kt.icis.oder.baseinfo.common.command.service.cwko.CwkosdmbldgchgCmdSvc;
import com.kt.icis.oder.baseinfo.common.query.repository.dto.cloc.BiClocbldgScopeQryDto;
import com.kt.icis.oder.baseinfo.common.query.repository.dto.cwko.BiCwkosdmbldgchgIfQryDto;
import com.kt.icis.oder.baseinfo.common.query.service.cloc.BiClocbldgQrySvc;
import com.kt.icis.oder.baseinfo.common.query.service.cwco.BiCwkosdmbldgchgQrySvc;
import com.kt.icis.oder.common.exception.OrderException;
import com.kt.icis.oder.common.utils.StringUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SdmBldgInfoCmdCntr {
	private final BiCwkosdmbldgchgQrySvc cwkosdmbldgchgQrySvc;
	private final CwkosdmbldgchgCmdSvc cwkosdmbldgchgCmdSvc;
	private final BiClocbldgQrySvc biClocbldgQrySvc;
	private final ClocbldgCmdSvc clocbldgCmdSvc;
	private final ClocsdmbldgCmdSvc clocsdmbldgCmdSvc;
	private final ClocbldghistCmdSvc clocbldghistCmdSvc;
	private final ClocsdmbldgsimilnmCmdSvc clocsdmbldgsimilnmCmdSvc;
	private final ClocsdmbldgsimiljibunCmdSvc clocsdmbldgsimiljibunCmdSvc;
	private final SdmBldgInfoTrCmdCntr bldgInfoTrCmdCntr;
	
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
	 * SDM 건물정보 연동
	 * @tuxedo CSNH040D
	 */
	//@Scheduled(fixedDelayString = "${oder.batch.ccui885d.delay-time}")
	public void csnh040dJob() {
		// for testing
		fetchLimit = 100;
		programId  = "CSNH040D";
		try {
			List<BiCwkosdmbldgchgIfQryDto>  cwkosdmbldgchgIfQryDtoList = this.getCwkosdmbldgchg();
			
			for(BiCwkosdmbldgchgIfQryDto biCwkosdmbldgchgIfQryDto: cwkosdmbldgchgIfQryDtoList)	{
				this.cshnh0404dJob(biCwkosdmbldgchgIfQryDto);
			}
		}catch(OrderException oe) {
			LogUtil.error(this.getErrorMessage(oe), oe);
		}
	}
	
	public List<BiCwkosdmbldgchgIfQryDto> getCwkosdmbldgchg(){
		List<BiCwkosdmbldgchgIfQryDto>  cwkosdmbldgchgIfQryDtoList = null;
		
		try {
			cwkosdmbldgchgIfQryDtoList = cwkosdmbldgchgQrySvc.selectCwkosdmbldgchgByRsltFlag0(ReqType.INTGRT.getType(), fetchLimit);
		}catch(Exception e) {
			throw new OrderException("SELECT 건물연동 ERROR");
		}
		
		return cwkosdmbldgchgIfQryDtoList;
	}
	
	/**
	 * SDM 건물정보 연동
	 * @tuxedo CSNH040D
	 * @param transactionStatus
	 * @param cwkosdmbldgchgIfQryDto
	 */
	public void cshnh0404dJob(BiCwkosdmbldgchgIfQryDto cwkosdmbldgchgIfQryDto) {
		Rslt rsltStatus = Rslt.DEFAULT;
		String rsltMsg  = "";
		try {
			this.checkRequest(cwkosdmbldgchgIfQryDto);
		
			if(BldgFlag.REPR.getFlag().equals(cwkosdmbldgchgIfQryDto.getReprBldgFlag())) {
				BiClocbldgScopeQryDto biClocbldgScopeQryDto = this.getClocbldg(cwkosdmbldgchgIfQryDto);
				bldgInfoTrCmdCntr.processBldgRepr(biClocbldgScopeQryDto, cwkosdmbldgchgIfQryDto);
				rsltStatus = Rslt.SUCC;				
			} else if(BldgFlag.NAME.getFlag().equals(cwkosdmbldgchgIfQryDto.getReprBldgFlag())) {
				bldgInfoTrCmdCntr.processBldgName(cwkosdmbldgchgIfQryDto);
				rsltStatus = Rslt.SUCC;
			} else if(BldgFlag.JIBN.getFlag().equals(cwkosdmbldgchgIfQryDto.getReprBldgFlag())) {
				bldgInfoTrCmdCntr.processBldgJibun(cwkosdmbldgchgIfQryDto);
				rsltStatus = Rslt.SUCC;
			} else {
				throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("유효하지 않은 요청: REPR_BLDG_FLAG(%s)".formatted(cwkosdmbldgchgIfQryDto.getReprBldgFlag())));
			}
		}catch(OrderException oe) {
			
			rsltStatus = Rslt.getRslt(oe.getCode());
			rsltMsg = this.getErrorMessage(oe);
			LogUtil.error(this.getErrorMessage(oe), oe);
		}finally {
			try {				
				if(rsltStatus != Rslt.DEFAULT) {				
					this.updateCwkoSdmbldgchgRslt(cwkosdmbldgchgIfQryDto, rsltStatus, rsltMsg);
				}
			}catch(OrderException oe) {
				LogUtil.error(this.getErrorMessage(oe), oe);
			}
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
			throw new OrderException("UPDATE 건물연동(CheckRequest)");			
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
			throw new OrderException("SELECT 건물연동(P)");
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
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("건물정보 조회 ERROR)"));
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
		reprClocbldgQryDto.setAddrHo(cwkosdmbldgchgIfQryDto.getAddrHo());
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
			throw new OrderException(Rslt.FAIL.getFlag(), Arrays.asList("UPDATE 건물연동(CheckRequest)"));
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
