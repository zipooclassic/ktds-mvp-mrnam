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

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.kt.icis.oder.baseinfo.common.repository.entity.BtCordordinfoQryEntt;
import com.kt.icis.oder.baseinfo.common.repository.sql.BtCordordinfoSql;

/**
 * 테스트 repository
 */
@Repository
public interface BtCordordinfoRepo extends CrudRepository<BtCordordinfoQryEntt,String>, PagingAndSortingRepository<BtCordordinfoQryEntt,String>{

    @Query(BtCordordinfoSql.SELECT_CORDORDINFO_WORKING_SVCNAME)
    public String checkOnGoingOrdNo(String saId);
    
}
