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

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.kt.icis.oder.baseinfo.common.repository.dto.BtCwkociasatarginfoCsnh624dDto;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCwkociasatarginfoCsnh625dDto;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCwkociasatarginfoCsnj626Dto;
import com.kt.icis.oder.baseinfo.common.repository.entity.BtCwkoCiaSaTargInfoEntt;
import com.kt.icis.oder.baseinfo.common.repository.sql.BtCwkoCiaSaTargInfoSql;

@Repository
public interface BtCwkoCiaSaTargInfoRepo extends CrudRepository<BtCwkoCiaSaTargInfoEntt,String>, PagingAndSortingRepository<BtCwkoCiaSaTargInfoEntt,String>{

    @Query(BtCwkoCiaSaTargInfoSql.SELECT_COUNT_CWKOCIASATARGINFO)
    public int getCount(String custProcType, String custId, String occurDate);

    @Query(BtCwkoCiaSaTargInfoSql.SELECT_GET_SAID_SACD)
    public List<BtCwkociasatarginfoCsnj626Dto> getSaIdSaCd(String custProcType, String custId, String occurDate);

    @Query(BtCwkoCiaSaTargInfoSql.SELECT_SERVIE_CONSTRACT_INFOMATION)
    public List<BtCwkoCiaSaTargInfoEntt> selectInfInsertSaWorkForInfo(BtCwkociasatarginfoCsnh625dDto dto);


    @Query(BtCwkoCiaSaTargInfoSql.SELECT_SERVIE_CONSTRACT_SA_TRAG_INFOMATION)
    public List<BtCwkoCiaSaTargInfoEntt> selectInfCInsertSaWorkForInfo(BtCwkociasatarginfoCsnh624dDto dto);
    
    @Query(BtCwkoCiaSaTargInfoSql.SELECT_STATUSCD_AND_PROCDATE_COUNT)
    public int selectStatusCdAndProcDateCount();
}
