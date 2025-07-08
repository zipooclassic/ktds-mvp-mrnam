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

package com.kt.icis.oder.baseinfo.csab.query.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import com.kt.icis.cmmnfrwk.utils.LogUtil;
import com.kt.icis.oder.baseinfo.common.query.repository.dto.cloc.BiClocroadjibunQryDto;
import com.kt.icis.oder.baseinfo.common.query.service.cloc.BiClocraddrbldgQrySvc;
import com.kt.icis.oder.baseinfo.common.query.service.cloc.BiClocroadinfoQrySvc;
import com.kt.icis.oder.baseinfo.common.query.service.cloc.BiClocroadjibunmapQrySvc;
import com.kt.icis.oder.baseinfo.csab.query.payload.in.dto.BizmekaNadrLnkInQryDto;
import com.kt.icis.oder.baseinfo.csab.query.payload.out.BizmekaNadrLnkOutQryPyld;
import com.kt.icis.oder.baseinfo.csab.query.payload.out.dto.BizmekaNadrLnkOutQryDto;
import com.kt.icis.oder.baseinfo.csab.query.payload.out.dto.BizmekaNadrLnkRlstOutQryDto;
import com.kt.icis.oder.common.exception.OrderException;
import com.kt.icis.oder.common.utils.StringUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BizmekaNadrLnkQryCntr {
	private final BiClocroadjibunmapQrySvc clocroadjibunmapQrySvc;
	private final BiClocroadinfoQrySvc clocroadinfoQrySvc;
	private final BiClocraddrbldgQrySvc clocraddrbldgQrySvc;

	private enum Rslt{
		DEFAULT("0"),                   /* 초기화(Default:0) */
		SUCC("0000"),                   /* 정상             */		
		ERR_WORK_GUBUN("0001"),          /*                 */
		ERR_INPUT_DONG("0002"),
		ERR_INPUT_ADDR_NO_TYPE("0003"),
		ERR_INPUT_ADDR_NO("0004"),
		ERR_INPUT_ADDR_HO("0005"),		
		ERR_INPUT_ROAD_NAME("0007"),
		ERR_INPUT_BDONG("0008"),
		ERR_INPUT_ROAD_ID("0009"),
		ERR_INPUT_RADDR_NO("0010"),
		ERR_INPUT_RADDR_HO("0011"),
		ERR_QRY_JIBUN_TO_ROAD_N("1001"),
		ERR_QRY_JIBUN_TO_ROAD_Y("1002"),		
		ERR_QRY_ROAD_NAME("2001"),
		ERR_QRY_ROAD_TO_JIBUN("3001"),
		ERR_QRY_DATA_GREATHER_THAN_100("4001");

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
	
	/**
	 * 비즈메카 새주소 연동
	 * @tuxedo CSAB801D
	 */
	public BizmekaNadrLnkOutQryPyld csab801dJob(BizmekaNadrLnkInQryDto bizmekaNadrLnkInQryDto) {
		Rslt rsltStatus = Rslt.DEFAULT;
		String rsltMsg  = "정상";
		
		List<BiClocroadjibunQryDto> clocroadjibunQryDtoList = Collections.emptyList();
		
		try {
			clocroadjibunQryDtoList = this.getClocroadjibun(bizmekaNadrLnkInQryDto);
			rsltStatus = Rslt.SUCC;
		}catch(OrderException oe) {
			rsltStatus = Rslt.getRslt(oe.getCode());
			rsltMsg = this.getErrorMessage(oe);
			LogUtil.error(this.getErrorMessage(oe), oe);
		}
		
		return this.getBizmekoNadrLnkPyld(clocroadjibunQryDtoList, bizmekaNadrLnkInQryDto.getWorkGubun(), rsltStatus.getFlag(), rsltMsg);
	}
	
	public BizmekaNadrLnkOutQryPyld getBizmekoNadrLnkPyld(List<BiClocroadjibunQryDto> clocroadjibunQryDtoList, String workGubun, String errCode, String errMsg) {
		List<BizmekaNadrLnkOutQryDto> bizmekaNadrLnkOutQryDtoList = this.getBizmekaNadrLnkList(clocroadjibunQryDtoList);
		BizmekaNadrLnkRlstOutQryDto bizmekaNadrLnkRlstOutQryDto = BizmekaNadrLnkRlstOutQryDto.builder()
			.workGubun(workGubun)
			.rsltDataCnt(String.valueOf(clocroadjibunQryDtoList.size()))
			.errCode(errCode)
			.errMsg(errMsg)
			.build();
		
		return BizmekaNadrLnkOutQryPyld.builder()
				.bizmekaNadrLnkOutQryDtoList(bizmekaNadrLnkOutQryDtoList)
				.bizmekaNadrLnkRlstOutQryDto(bizmekaNadrLnkRlstOutQryDto)
				.build();
	}
	
	public List<BizmekaNadrLnkOutQryDto> getBizmekaNadrLnkList(List<BiClocroadjibunQryDto> clocroadjibunQryDtoList){		
		List<BizmekaNadrLnkOutQryDto> bizmekaNadrLnkOutQryDtoList = new ArrayList<>();
		clocroadjibunQryDtoList.forEach(s -> {
			BizmekaNadrLnkOutQryDto t = BizmekaNadrLnkOutQryDto.builder()
					.dongCd(s.getDongCd())
					.addrNoType(s.getAddrNoType())
					.addrNo(s.getAddrNo())
					.addrHo(s.getAddrHo())
					.addrText(s.getAddrText())
					.legDongCd(s.getBDongCd())
					.arnoId(s.getArnoId())
					.roadId(s.getRoadNmId())
					.roadNm(s.getRoadName())
					.roadFullNm(s.getRoadAddr())
					.raddrNo(s.getRbldgNo())
					.raddrHo(s.getRbldgHo())
					.rbldgId(s.getRbldgId())
					.rbldgNmId(s.getRbldgNmId())
					.rbldgName(s.getRbldgName())
					.rbldgText(s.getRbldgText())
					.build();
			
			bizmekaNadrLnkOutQryDtoList.add(t);
		});
		
		return bizmekaNadrLnkOutQryDtoList;
	}
	
	/**
	 * WORK GUBUN에 따라 송신 데이타 조회
	 * @tuxedo CSAB801D 
	 * @param bizmekaNadrLnkInQryDto
	 */
	public List<BiClocroadjibunQryDto> getClocroadjibun(BizmekaNadrLnkInQryDto bizmekaNadrLnkInQryDto) {
		List<BiClocroadjibunQryDto> clocroadjibunQryDtoList = Collections.emptyList();
		
		if("1".equals(bizmekaNadrLnkInQryDto.getWorkGubun())) {
			clocroadjibunQryDtoList = this.getRaddrByJibunaAddr(bizmekaNadrLnkInQryDto);
		}else if("2".equals(bizmekaNadrLnkInQryDto.getWorkGubun())){
			clocroadjibunQryDtoList = this.getRaddr(bizmekaNadrLnkInQryDto);
		}else if("3".equals(bizmekaNadrLnkInQryDto.getWorkGubun())) {
			clocroadjibunQryDtoList = this.getJibunAddrByRaddr(bizmekaNadrLnkInQryDto);
		}else {
			StringBuffer workgubunstr = new StringBuffer();
			workgubunstr.append(Rslt.ERR_WORK_GUBUN.getFlag());
			workgubunstr.append(":작업구분 오류입니다.");
			LogUtil.info(String.format("비즈메카 새주소 연동 : %s", workgubunstr.toString()));
		}
		
		return clocroadjibunQryDtoList;
	}
	
	/**
	 * 지번주소로 도로명 주소 찾기
	 * @tuxedo CSAB801D 
	 * @param bizmekaNadrLnkInQryDto
	 * @return
	 */
	public List<BiClocroadjibunQryDto> getRaddrByJibunaAddr(BizmekaNadrLnkInQryDto bizmekaNadrLnkInQryDto) {
		List<BiClocroadjibunQryDto> clocroadjibunQryDtoList = Collections.emptyList();		
		this.validateJibun(bizmekaNadrLnkInQryDto);
		
		try {
			clocroadjibunQryDtoList = this.clocroadjibunmapQrySvc.selectClocroadjibunmapRoadByJibun(					
					"N", 
					bizmekaNadrLnkInQryDto.getDongCd(),
					bizmekaNadrLnkInQryDto.getAddrNoType(),
					bizmekaNadrLnkInQryDto.getAddrNo(), 
					bizmekaNadrLnkInQryDto.getAddrHo());
			
			if(clocroadjibunQryDtoList != null && clocroadjibunQryDtoList.size() > 100) {
				throw new OrderException(Rslt.ERR_QRY_DATA_GREATHER_THAN_100.getFlag(), Arrays.asList("100건 이상 자료가 존재하니, 조회조건을 변경해서 조회하시기 바랍니다"));
			}
		}catch(Exception e) {
			throw new OrderException(Rslt.ERR_QRY_JIBUN_TO_ROAD_N.getFlag(), Arrays.asList("지번주소 도로명 주소 매핑시 오류"));
		}
		
		try {
			if(ObjectUtils.isEmpty(clocroadjibunQryDtoList)) {
				clocroadjibunQryDtoList = this.clocroadjibunmapQrySvc.selectClocroadjibunmapRoadByJibun(					
						"Y", 
						bizmekaNadrLnkInQryDto.getDongCd(),
						bizmekaNadrLnkInQryDto.getAddrNoType(),
						bizmekaNadrLnkInQryDto.getAddrNo(), 
						bizmekaNadrLnkInQryDto.getAddrHo());

				if(clocroadjibunQryDtoList != null && clocroadjibunQryDtoList.size() > 100) {
					throw new OrderException(Rslt.ERR_QRY_DATA_GREATHER_THAN_100.getFlag(), Arrays.asList("100건 이상 자료가 존재하니, 조회조건을 변경해서 조회하시기 바랍니다"));
				}
			}
		}catch(Exception e) {
			throw new OrderException(Rslt.ERR_QRY_JIBUN_TO_ROAD_Y.getFlag(), Arrays.asList("지번주소 도로명 주소 매핑시 오류"));
		}
		
		return clocroadjibunQryDtoList;
	}

	/**
	 * 도로명 조회
	 * @tuxedo CSAB801D  
	 * @param bizmekaNadrLnkInQryDto
	 * @return
	 */
	public List<BiClocroadjibunQryDto> getRaddr(BizmekaNadrLnkInQryDto bizmekaNadrLnkInQryDto) {		
		List<BiClocroadjibunQryDto> clocroadjibunQryDtoList = Collections.emptyList();
		this.validateRaddrName(bizmekaNadrLnkInQryDto);
		
		try {
			if(ObjectUtils.isEmpty(bizmekaNadrLnkInQryDto.getLegDongCd())) {
				clocroadjibunQryDtoList = this.clocroadinfoQrySvc.selectClocroadinfoByRoadName(bizmekaNadrLnkInQryDto.getRoadName());
			} else {
				clocroadjibunQryDtoList = this.clocroadinfoQrySvc.selectClocroadinfoByRoadNameAndDongCd(bizmekaNadrLnkInQryDto.getRoadName(), bizmekaNadrLnkInQryDto.getLegDongCd());
			}
			
			if(clocroadjibunQryDtoList != null && clocroadjibunQryDtoList.size() > 100) {
				throw new OrderException(Rslt.ERR_QRY_DATA_GREATHER_THAN_100.getFlag(), Arrays.asList("100건 이상 자료가 존재하니, 조회조건을 변경해서 조회하시기 바랍니다"));
			}
		}catch(Exception e) {
			throw new OrderException(Rslt.ERR_QRY_ROAD_NAME.getFlag(), Arrays.asList("도로명조회 오류 입니다"));
		}
		
		return clocroadjibunQryDtoList;
	}

	/**
	 * 도로명으로 지번 조회
	 * @tuxedo CSAB801D 
	 * @param bizmekaNadrLnkInQryDto
	 * @return
	 */
	public List<BiClocroadjibunQryDto> getJibunAddrByRaddr(BizmekaNadrLnkInQryDto bizmekaNadrLnkInQryDto) {		
		List<BiClocroadjibunQryDto> clocroadjibunQryDtoList = Collections.emptyList();
		this.validateRaddr(bizmekaNadrLnkInQryDto);
		
		try {
			clocroadjibunQryDtoList = this.clocraddrbldgQrySvc.selectClocraddrbldgJibunByRoad(
					bizmekaNadrLnkInQryDto.getRoadId(),
					bizmekaNadrLnkInQryDto.getLegDongCd(),
					bizmekaNadrLnkInQryDto.getRaddrNo(),
					bizmekaNadrLnkInQryDto.getRaddrHo());
			
			if(clocroadjibunQryDtoList != null && clocroadjibunQryDtoList.size() > 100) {
				throw new OrderException(Rslt.ERR_QRY_DATA_GREATHER_THAN_100.getFlag(), Arrays.asList("100건 이상 자료가 존재하니, 조회조건을 변경해서 조회하시기 바랍니다"));
			}			
		}catch(Exception e) {
			throw new OrderException(Rslt.ERR_QRY_ROAD_TO_JIBUN.getFlag(), Arrays.asList("지번주소 매핑 조회 오류 입니다"));
		}
		
		return clocroadjibunQryDtoList;
	}
		
	
	public void validateRaddr(BizmekaNadrLnkInQryDto bizmekaNadrLnkInQryDto) {
		if(StringUtils.defaultString(bizmekaNadrLnkInQryDto.getLegDongCd()).length() != 6) {
			throw new OrderException(Rslt.ERR_INPUT_BDONG.getFlag(), Arrays.asList("법정동 코드 입력 오류 입니다"));
		}
		
		if(StringUtils.defaultString(bizmekaNadrLnkInQryDto.getRoadId()).length() != 9) {
			throw new OrderException(Rslt.ERR_INPUT_ROAD_ID.getFlag(), Arrays.asList("도로명ID 입력 오류 입니다"));
		}
		
		if(StringUtils.defaultString(bizmekaNadrLnkInQryDto.getRaddrNo()).length() != 5) {
			throw new OrderException(Rslt.ERR_INPUT_RADDR_NO.getFlag(), Arrays.asList("도로명 본번 입력 오류 입니다"));
		}
		
		if(StringUtils.defaultString(bizmekaNadrLnkInQryDto.getRaddrHo()).length() != 5) {
			throw new OrderException(Rslt.ERR_INPUT_RADDR_HO.getFlag(), Arrays.asList("도로명 부번 입력 오류 입니다"));
		}
	}
	
	public void validateJibun(BizmekaNadrLnkInQryDto bizmekaNadrLnkInQryDto) {
		if(StringUtils.defaultString(bizmekaNadrLnkInQryDto.getDongCd()).length() != 6) {
			throw new OrderException(Rslt.ERR_INPUT_DONG.getFlag(), Arrays.asList("동코드 입력 오류 입니다"));
		}
		
		if(StringUtils.defaultString(bizmekaNadrLnkInQryDto.getAddrNoType()).length() != 1) {
			throw new OrderException(Rslt.ERR_INPUT_ADDR_NO_TYPE.getFlag(), Arrays.asList("주소유형 입력 오류 입니다"));
		}
		
		if(StringUtils.defaultString(bizmekaNadrLnkInQryDto.getAddrNo()).length() != 4) {
			throw new OrderException(Rslt.ERR_INPUT_ADDR_NO.getFlag(), Arrays.asList("번지 입력 오류 입니다"));
		}
		
		if(StringUtils.defaultString(bizmekaNadrLnkInQryDto.getAddrHo()).length() != 4) {
			throw new OrderException(Rslt.ERR_INPUT_ADDR_HO.getFlag(), Arrays.asList("호 입력 오류 입니다"));
		}
	}
	
	public void validateRaddrName(BizmekaNadrLnkInQryDto bizmekaNadrLnkInQryDto) {
		if(StringUtils.defaultString(bizmekaNadrLnkInQryDto.getRoadName()).length() < 1) {
			throw new OrderException(Rslt.ERR_INPUT_ROAD_NAME.getFlag(), Arrays.asList("도로명 입력 오류 입니다"));
		}
	}
	
	public String getErrorMessage(OrderException oe) {
		String errMessage = "";
		if(oe.getErrMsgArr() != null) {
			errMessage = StringUtils.defaultString(oe.getErrMsgArr().get(0));
		}
		return StringUtils.defaultString(oe.getCode()) + ":" + errMessage;
	}
}