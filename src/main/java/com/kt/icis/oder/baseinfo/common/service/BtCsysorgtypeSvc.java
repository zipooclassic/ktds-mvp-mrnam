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

import org.springframework.stereotype.Service;

import com.kt.icis.oder.baseinfo.common.repository.BtCsysorgtypeMbtsRepo;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCsysorgtypeDto;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class BtCsysorgtypeSvc {

private final BtCsysorgtypeMbtsRepo csysOrgtypeRepo;
	
	public int insertCsysOrgtype(BtCsysorgtypeDto csysOrgTypeDto){
		return csysOrgtypeRepo.insertCsysortype(csysOrgTypeDto);
	}
	
	public int updateCsysOrgtype(BtCsysorgtypeDto csysOrgTypeDto){
		return csysOrgtypeRepo.updateCsysortype(csysOrgTypeDto);
	}
	
	public int deleteCsysOrgtype(String orgType){
		return csysOrgtypeRepo.deleteCsysortype(orgType);
	}
}
