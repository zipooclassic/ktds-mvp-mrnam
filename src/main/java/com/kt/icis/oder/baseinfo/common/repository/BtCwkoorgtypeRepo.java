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

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.kt.icis.oder.baseinfo.common.repository.dto.BtCwkoorgtypeDto;
import com.kt.icis.oder.baseinfo.common.repository.entity.BtCwkoorgtypeEntt;
import com.kt.icis.oder.baseinfo.common.repository.sql.BtCwkoorgtypeSql;

@Repository
public interface BtCwkoorgtypeRepo extends CrudRepository<BtCwkoorgtypeEntt,String>, PagingAndSortingRepository<BtCwkoorgtypeEntt,String>{
	
	@Query(BtCwkoorgtypeSql.SELECT_CWKOORGTYPE_BY_WO_FLAG_ZERO)
	List<BtCwkoorgtypeDto> selectCwkOrgtypeByWoFlagZero();
	
	@Modifying
    @Query(BtCwkoorgtypeSql.UPDATE_CWKOORGTYPE_WO_FLAG_BY_ROWID)
    public int updateCwkOrgtype(String rowId, String woFlag, String woErrMsg);
}
 
