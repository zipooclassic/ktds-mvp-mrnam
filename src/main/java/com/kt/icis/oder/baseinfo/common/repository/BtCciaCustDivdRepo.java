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

import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciaCustDivdCsnh627dDto;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciacustdivdCsnh622dDto;
import com.kt.icis.oder.baseinfo.common.repository.entity.BtCciaCustDivdQryEntt;
import com.kt.icis.oder.baseinfo.common.repository.sql.BtCciaCustDivdSql;

/**
 * BtCciaCustDivdRepo:
 * <ul>
 * <li></li>
 * </ul>
 * 
 * created by eunsub on 2023.09.05.
 * 
 * @author eunsub
 */
@Repository
public interface BtCciaCustDivdRepo extends CrudRepository<BtCciaCustDivdQryEntt,String>, PagingAndSortingRepository<BtCciaCustDivdQryEntt,String>{

    @Query(BtCciaCustDivdSql.SELECT_CCIACUSTDIVD_REWORK_SVCNAME)
    public List<BtCciaCustDivdCsnh627dDto> infDeclareCursorIcisWork();

    @Modifying
    @Query(BtCciaCustDivdSql.UPDATE_CCIACUSTDIVD_ICIS_WORK_WRITER)
    public int updateIcisWork(BtCciaCustDivdCsnh627dDto dto);

    @Query(BtCciaCustDivdSql.SELECT_CCIACUSTDIVD_CCPICRODHIER_CCSTBASICINFO)
    public List<BtCciacustdivdCsnh622dDto> findByCciacustdivdAndccpiprodhierAndccstbasicinfo();

    @Modifying
    @Query(BtCciaCustDivdSql.UPDATE_CCIACUSTDIVD_SAID_CUSTID_OCCURDATE_NEWCUSTID)
    public void updateBySaidCustidOccurdateNewcustid(BtCciacustdivdCsnh622dDto dto);

}
