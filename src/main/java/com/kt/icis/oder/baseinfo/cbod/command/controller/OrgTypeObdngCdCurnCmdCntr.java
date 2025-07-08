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
package com.kt.icis.oder.baseinfo.cbod.command.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.icis.cmmnfrwk.utils.LogUtil;
import com.kt.icis.oder.baseinfo.common.command.repository.dto.csys.CsysorgtypeCmdDto;
import com.kt.icis.oder.baseinfo.common.command.repository.dto.msys.MsysofficecodeCmdDto;
import com.kt.icis.oder.baseinfo.common.command.service.csys.CsysorgtypeCmdSvc;
import com.kt.icis.oder.baseinfo.common.command.service.cwko.CwkoofficecodeCmdSvc;
import com.kt.icis.oder.baseinfo.common.command.service.cwko.CwkoorgtypeCmdSvc;
import com.kt.icis.oder.baseinfo.common.command.service.msys.MsysofficecodeCmdSvc;
import com.kt.icis.oder.baseinfo.common.query.repository.dto.cwko.BiCwkoofficecodeQryDto;
import com.kt.icis.oder.baseinfo.common.query.repository.dto.cwko.BiCwkoorgtypeQryDto;
import com.kt.icis.oder.baseinfo.common.query.service.cwko.BiCwkoofficecodeQrySvc;
import com.kt.icis.oder.baseinfo.common.query.service.cwko.BiCwkoorgtypeQrySvc;
import com.kt.icis.oder.common.exception.OrderException;
import com.kt.icis.oder.common.utils.StringUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrgTypeObdngCdCurnCmdCntr {
	
	private final BiCwkoorgtypeQrySvc cwkoorgtypeQrySvc;
	private final BiCwkoofficecodeQrySvc cwkoofficecodeQrySvc;	
	private final CwkoorgtypeCmdSvc cwkoorgtypeCmdSvc;
	private final CwkoofficecodeCmdSvc cwkoofficecodeCmdSvc;
	private final CsysorgtypeCmdSvc csysorgtypeCmdSvc;
	private final MsysofficecodeCmdSvc msysofficecodeCmdSvc;

	private enum Rslt{
		DEFAULT("0"),  /* 초기화(Default:N) */
		SUCC("Y"),     /* 처리완료(Y)       */		
		ERR("N");      /* ERROR(N)        */		
		
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
     * 조직유형, 국사코드 현행화 데몬
     * @tuxedo CBOD905D
     */    
    //@Scheduled(fixedDelayString = "${oder.batch.cbod905d.delay-time}")
    public void cbod905d() {    	

    	// 1. 조직유형 변동분 반영
        executeUCsysOrgType();

        // 2. 국사코드 변동분 반영
        executeUCsysOfcCode();
    }
    
    public void executeUCsysOrgType() {    	
    	try{
	    	List<BiCwkoorgtypeQryDto> orgTypeDtoList = this.getCwkorgtype();
	
	    	for(BiCwkoorgtypeQryDto cwkorgTypeDto: orgTypeDtoList) {
	
	    		if(StringUtils.isEmpty(cwkorgTypeDto.getRowId())) {
	    			break;
	    		}
	    		
	    		this.executeUCsysOrgType(cwkorgTypeDto);
	    	}
    	}catch(OrderException oe) {
			LogUtil.error(this.getErrorMessage(oe), oe);
		}	    
    }
    
    public List<BiCwkoorgtypeQryDto> getCwkorgtype(){
    	List<BiCwkoorgtypeQryDto> orgTypeDtoList = Collections.emptyList();    	
    	try {
    		orgTypeDtoList = this.cwkoorgtypeQrySvc.selectCwkOrgtypeByWoFlagZero();
    	}catch(Exception e) {
    		throw new OrderException("SELECT 조직특성구분코드 변동정보수신테이블 ERROR");
    	}
    	return orgTypeDtoList;
    }
    
    public void executeUCsysOrgType(BiCwkoorgtypeQryDto cwkorgTypeDto) {
    	Rslt rsltStatus = Rslt.DEFAULT;
    	String rsltMsg  = "";

    	try {
    		CsysorgtypeCmdDto csysOrgTypeDto = this.getMappedBy(cwkorgTypeDto);

			if("I".equals(cwkorgTypeDto.getEaiOp())) {
				this.insertCsysOrgtype(csysOrgTypeDto);
				rsltStatus = Rslt.SUCC;
			}else if("D".equals(cwkorgTypeDto.getEaiOp())) {
				this.deleteCsysOrgtype(csysOrgTypeDto.getOrgtype());
				rsltStatus = Rslt.SUCC;
			}else if("U".equals(cwkorgTypeDto.getEaiOp())) {
				this.updateCsysOrgtype(csysOrgTypeDto);
				rsltStatus = Rslt.SUCC;
			}else {
				throw new OrderException(Rslt.ERR.getFlag(), Arrays.asList("EAI_OP 값 오류[%s]".formatted(cwkorgTypeDto.getEaiOp())));
			}
    	}catch(OrderException oe) {
    		rsltStatus = Rslt.getRslt(oe.getCode());
    		rsltMsg = this.getErrorMessage(oe);
			LogUtil.error(this.getErrorMessage(oe), oe);			
    	}finally {
    		try {
    			if(rsltStatus != Rslt.DEFAULT) {
    				this.updateCwkOrgtype(cwkorgTypeDto.getRowId(), rsltStatus.getFlag(), rsltMsg);
    			}
    		}catch(OrderException oe) {
    			LogUtil.error(this.getErrorMessage(oe), oe);
    		}
    	}
    }
    
    public CsysorgtypeCmdDto getMappedBy(BiCwkoorgtypeQryDto inDto) {
    	return CsysorgtypeCmdDto.builder()
		        .orgtype(inDto.getOrgtype())
		        .businessgb(inDto.getBusinessgb())
		        .orgtypename(inDto.getOrgtypename())
		        .disseq(inDto.getDisseq())
		        .active(inDto.getActive())
		        .creationdtime(inDto.getCreationdtime())
		        .updatedtime(inDto.getUpdatedtime())
		        .eaiSeq(inDto.getEaiSeq())
		        .build();
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertCsysOrgtype(CsysorgtypeCmdDto csysOrgTypeDto) {
    	try {
    		this.csysorgtypeCmdSvc.insertCsysOrgtype(csysOrgTypeDto);
    	}catch(Exception e) {
    		throw new OrderException(Rslt.ERR.getFlag(), Arrays.asList("INSERT 조직특성구분코드 테이블 오류발생[%s]".formatted(this.getErrorMsgInException(e))));
    	}
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateCsysOrgtype(CsysorgtypeCmdDto csysOrgTypeDto) {
    	try {
    		this.csysorgtypeCmdSvc.updateCsysOrgtype(csysOrgTypeDto);
    	}catch(Exception e) {
    		throw new OrderException(Rslt.ERR.getFlag(), Arrays.asList("UPDATE 조직특성구분코드 테이블 오류발생[%s]".formatted(this.getErrorMsgInException(e))));
    	}
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteCsysOrgtype(String orgType) {
    	try {
    		this.csysorgtypeCmdSvc.deleteCsysOrgtype(orgType);
    	}catch(Exception e) {
    		throw new OrderException(Rslt.ERR.getFlag(), Arrays.asList("DELETE 조직특성구분코드 테이블 오류발생[%s]".formatted(this.getErrorMsgInException(e))));
    	}
    }
    
    public int updateCwkOrgtype(String rowId, String woFlag, String errMsg) {
    	try {
    		return this.cwkoorgtypeCmdSvc.updateCwkOrgtype(rowId, woFlag, errMsg);
    	}catch(Exception e) {
    		throw new OrderException(Rslt.ERR.getFlag(), Arrays.asList("UPDATE 조직특성구분코드 변동정보수신테이블 오류발생[%s]".formatted(this.getErrorMsgInException(e))));
    	}
    }

    public void executeUCsysOfcCode() {
    	try {
	    	List<BiCwkoofficecodeQryDto> officecodeDtoList = this.getCwkoofficecode();
	    	
	    	for(BiCwkoofficecodeQryDto officecodeDto: officecodeDtoList) {
	    		
	    		if(StringUtils.isEmpty(officecodeDto.getRowId())) {
	    			break;
	    		}
	    		
	   			this.executeUCsysOfcCode(officecodeDto);
	    	}
    	}catch(OrderException oe) {
			LogUtil.error(this.getErrorMessage(oe), oe);
		}
    }
    
    public List<BiCwkoofficecodeQryDto> getCwkoofficecode(){    	
    	List<BiCwkoofficecodeQryDto> officecodeDtoList = Collections.emptyList();
    	try {
    		officecodeDtoList = this.cwkoofficecodeQrySvc.selectCwkoOfficeCodeByWoFlagZero();
    	}catch(Exception e) {
    		throw new OrderException("국사정보변동정보수신테이블" );
    	}
    	return officecodeDtoList;
    }
    
    public void executeUCsysOfcCode(BiCwkoofficecodeQryDto cwkoofficecodeDto) {
    	Rslt rsltStatus = Rslt.DEFAULT;
    	String rsltMsg  = "";
    	
    	try {
			MsysofficecodeCmdDto msysoffcecodeDto = this.getMappedBy(cwkoofficecodeDto);
			
			if("I".equals(cwkoofficecodeDto.getEaiOp())) {
				this.insertMsysofficecode(msysoffcecodeDto);
				rsltStatus = Rslt.SUCC;
			}else if("D".equals(cwkoofficecodeDto.getEaiOp())) {
				this.deleteMsysofficecode(msysoffcecodeDto.getOfficecode());
				rsltStatus = Rslt.SUCC;
			}else if("U".equals(cwkoofficecodeDto.getEaiOp())) {
				this.updateMsysofficecode(msysoffcecodeDto);
				rsltStatus = Rslt.SUCC;
			}else {
				throw new OrderException(Rslt.ERR.getFlag(), Arrays.asList("EAI_OP 값 오류[%s]".formatted(cwkoofficecodeDto.getEaiOp())));				
			}
    	}catch(OrderException oe) {
    		
    		rsltStatus = Rslt.getRslt(oe.getCode());
    		rsltMsg = this.getErrorMessage(oe);
			LogUtil.error(this.getErrorMessage(oe), oe);
    	}finally {
    		try {
    			if(rsltStatus != Rslt.DEFAULT) {
    				this.updateCwkoofficecode(cwkoofficecodeDto.getRowId(), rsltStatus.getFlag(), rsltMsg);
    			}
    		}catch(OrderException oe) {
    			LogUtil.error(this.getErrorMessage(oe), oe);
    		}
    	}
    }
    
    public MsysofficecodeCmdDto getMappedBy(BiCwkoofficecodeQryDto inDto) {
    	return MsysofficecodeCmdDto.builder()
                .officecode(inDto.getOfficecode())
                .officename(inDto.getOfficename())
                .parentcodeid(inDto.getParentcodeid())
                .orgcodebu(inDto.getOrgcodebu())
                .orgcodebuName(inDto.getOrgcodebuName())
                .orgcodebr(inDto.getOrgcodebr())
                .orgcodebrName(inDto.getOrgcodebrName())
                .orgcodenw(inDto.getOrgcodenw())
                .orgcodenwName(inDto.getOrgcodenwName())
                .orgcodeetc(inDto.getOrgcodeetc())
                .orgcodeetcName(inDto.getOrgcodeetcName())
                .loccode(inDto.getLoccode())
                .officetype(inDto.getOfficetype())
                .officetypeName(inDto.getOfficetypeName())
                .dongcode(inDto.getDongcode())
                .dongcodeName(inDto.getDongcodeName())
                .addrtype(inDto.getAddrtype())
                .addrtypename(inDto.getAddrtypename())
                .bunji(inDto.getBunji())
                .ho(inDto.getHo())
                .note(inDto.getNote())
                .officenick(inDto.getOfficenick())
                .detailaddr(inDto.getDetailaddr())
                .creationdtime(inDto.getCreationdtime())
                .lastmodificationdtime(inDto.getLastmodificationdtime())
                .lastmodificationusrid(inDto.getLastmodificationusrid())
                .status(inDto.getStatus())
                .active(inDto.getActive())
                .eaiSeq(inDto.getEaiSeq())
                .build();
    }

    public void insertMsysofficecode(MsysofficecodeCmdDto msysoffcecodeDto) {
    	try {
    		this.msysofficecodeCmdSvc.insertMsysofficecode(msysoffcecodeDto);
    	}catch(Exception e) {
    		throw new OrderException(Rslt.ERR.getFlag(), Arrays.asList("INSERT 국사정보테이블(MDM) 오류발생[%s]".formatted(this.getErrorMsgInException(e))));
    	}
    }
    
    public void updateMsysofficecode(MsysofficecodeCmdDto msysoffcecodeDto) {
    	try {
    		this.msysofficecodeCmdSvc.updateMsysofficecode(msysoffcecodeDto);
    	}catch(Exception e) {
    		throw new OrderException(Rslt.ERR.getFlag(), Arrays.asList("UPDATE 국사정보테이블(MDM) 오류발생[%s]".formatted(this.getErrorMsgInException(e))));
    	}
    }
    
    public void deleteMsysofficecode(String officecode) {
    	try {
    		this.msysofficecodeCmdSvc.deleteMsysofficecode(officecode);
    	}catch(Exception e) {
    		throw new OrderException(Rslt.ERR.getFlag(), Arrays.asList("DELETE 국사정보테이블(MDM) 오류발생[%s]".formatted(this.getErrorMsgInException(e))));
    	}
    }

    public void updateCwkoofficecode(String rowId, String woFlag, String errMsg) {
    	try {
    		this.cwkoofficecodeCmdSvc.updateCwkoOfficeCode(rowId, woFlag, errMsg);
    	}catch(Exception e) {
    		throw new OrderException(Rslt.ERR.getFlag(), Arrays.asList("UPDATE 국사정보변동정보수신테이블 오류발생[%s]".formatted(this.getErrorMsgInException(e))));
    	}
    }
    
    public String getErrorMsgInException(Exception e) {    	
    	int MSG_MAX_LEN = 100;    	
    	int len = 0;
    	String msg = "";
    	
    	if(!StringUtils.isEmpty(e.getMessage()))
    	{
    		len = e.getMessage().length();
    		
    		if(len > MSG_MAX_LEN)
        	{
        		len = MSG_MAX_LEN;
        	}
    	}
    	
    	if(len > 0) {
    		msg = e.getMessage().substring(0, len);
    	}
    	
    	return msg;
    }
    
    public String getErrorMessage(OrderException oe) {
		String errMessage = "";
		if(oe.getErrMsgArr() != null) {
			errMessage = StringUtils.defaultString(oe.getErrMsgArr().get(0));
		}
		return StringUtils.defaultString(oe.getCode()) + ":" + errMessage;
	}
}
