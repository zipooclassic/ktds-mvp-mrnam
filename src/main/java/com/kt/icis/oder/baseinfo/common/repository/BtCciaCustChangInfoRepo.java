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

import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciaCustChangInfoCsnh624dDto;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciacustintegCsnh624dDto;
import com.kt.icis.oder.baseinfo.common.repository.entity.CciaCustChangInfoQryEntt;
import com.kt.icis.oder.baseinfo.common.repository.sql.BtCciaCustChangInfoSql;

/**
 * BtCciaCustChangInfoRepo:
 * <ul>
 * <li></li>
 * </ul>
 *
 * created by eunsub on 2023.09.05.
 *
 * @author eunsub
 */
@Repository
public interface BtCciaCustChangInfoRepo extends CrudRepository<CciaCustChangInfoQryEntt,String>, PagingAndSortingRepository<CciaCustChangInfoQryEntt,String>{

    /**
     * ICIS(_CCIA_CUST_INTEG)에 처리 결과 값을 갱신
     * @param dto
     * @return
     */
    @Modifying
    @Query(BtCciaCustChangInfoSql.UPDATE_CCIACUSTCHANGINFO_CIA_FLAG)
    public int infCUpdateCiaWork(BtCciaCustChangInfoCsnh624dDto dto);


    /**
     * CIA 변동 데이터 조회
     */
    @Query(BtCciaCustChangInfoSql.SELECT_BI_CCIA_CUST_CHANG_INFO_AND_VWBICCSTBASICINFO)
    public List<BtCciacustintegCsnh624dDto> getInfCDeclareCursorCiaWork();



}