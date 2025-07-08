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
package com.kt.icis.oder.baseinfo.common.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kt.icis.oder.baseinfo.common.repository.BtCwkoofficecodeRepo;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCwkoofficecodeDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BtCwkoofficecodeSvc {

private final BtCwkoofficecodeRepo cwkoofficecodeRepo;
	
	public List<BtCwkoofficecodeDto> selectCwkoOfficeCodeByWoFlagZero(){
		return cwkoofficecodeRepo.selectCwkoOfficeCodeByWoFlagZero();
	}
	
	public int updateCwkoOfficeCode(String rowId, String woFlag, String woErrMsg){
		return cwkoofficecodeRepo.updateCwkoOfficeCode(rowId, woFlag, woErrMsg);
	}
    
}
