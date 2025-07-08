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

import com.kt.icis.oder.baseinfo.common.repository.BtMsysofficecodeMbtsRepo;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtMsysofficecodeDto;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class BtMsysofficecodeSvc {

private final BtMsysofficecodeMbtsRepo msysofficecodeRepo;
	
	public int insertMsysofficecode(BtMsysofficecodeDto msysofficecodeDto){
		return msysofficecodeRepo.insertMsysofficecode(msysofficecodeDto);
	}
	
	public int updateMsysofficecode(BtMsysofficecodeDto msysofficecodeDto){
		return msysofficecodeRepo.updateMsysofficecode(msysofficecodeDto);
	}
	
	public int deleteMsysofficecode(String officecode){
		return msysofficecodeRepo.deleteMsysofficecode(officecode);
	}
}
