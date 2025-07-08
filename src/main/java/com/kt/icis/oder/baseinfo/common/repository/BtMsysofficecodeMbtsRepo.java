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

package com.kt.icis.oder.baseinfo.common.repository;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.UpdateProvider;

import com.kt.icis.oder.baseinfo.common.repository.dto.BtMsysofficecodeDto;
import com.kt.icis.oder.baseinfo.common.repository.sql.BtMsysofficecodeSql;

@Mapper
public interface BtMsysofficecodeMbtsRepo {
	
	@InsertProvider(type = BtMsysofficecodeSql.class, method = "insertMsysofficecode")
    public int insertMsysofficecode(BtMsysofficecodeDto inDto);
	
	@DeleteProvider(type = BtMsysofficecodeSql.class, method = "deleteMsysofficecode")
    public int deleteMsysofficecode(String officecode);
	
	@UpdateProvider(type = BtMsysofficecodeSql.class, method = "updateMsysofficecode")
    public int updateMsysofficecode(BtMsysofficecodeDto inDto);
}
 
