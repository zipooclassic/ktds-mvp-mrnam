/**************************************************************************************
 * ICIS version 1.0
 *
 *  Copyright ⓒ 2023 kt/ktds corp. All rights reserved.
 *
 *  This is a proprietary software of kt corp, and you may not use this file except in
 *  compliance with license agreement with kt corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of kt corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 *************************************************************************************/

package com.kt.icis.oder.baseinfo.csnl.command.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.kt.icis.oder.baseinfo.common.query.repository.dto.ccst.BiCcstcustagentQryDto;
import com.kt.icis.oder.baseinfo.csnl.command.repository.entity.BiCcstcustagentCmdEntt;
import com.kt.icis.oder.baseinfo.csnl.command.repository.sql.Csnl400dCmdSql;

@Repository
public interface Csnl400dCmdRepo extends PagingAndSortingRepository<BiCcstcustagentCmdEntt, String>, CrudRepository<BiCcstcustagentCmdEntt, String>  {

    @Query(Csnl400dCmdSql.SELECT_CCSTCUSTAGENT_BY_WOFLAG_ORDERBY_EAISEQ)
    public List<BiCcstcustagentQryDto> reader();

    @Modifying
    @Query(Csnl400dCmdSql.UPDATE_CCSTCUSTAGENT_WOFLAG_WODATE)
    public int writer(BiCcstcustagentQryDto dto);

}
