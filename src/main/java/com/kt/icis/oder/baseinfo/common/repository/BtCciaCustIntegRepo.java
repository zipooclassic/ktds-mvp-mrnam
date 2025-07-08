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

import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciacustintegAndCciacustchanginfoDto;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciacustintegCsnh625dDto;
import com.kt.icis.oder.baseinfo.common.repository.entity.BtCciaCustIntegEntt;
import com.kt.icis.oder.baseinfo.common.repository.sql.BtCciaCustIntegSql;
/**
 * BtCciaCustIntegRepo:
 * <ul>
 * <li></li>
 * </ul>
 *
 * created by eunsub on 2023.09.05.
 *
 * @author eunsub
 */

@Repository
public interface BtCciaCustIntegRepo extends CrudRepository<BtCciaCustIntegEntt,String>, PagingAndSortingRepository<BtCciaCustIntegEntt,String>{

    @Query(BtCciaCustIntegSql.SELECT_CCIACUSTINTEG_AND_CCIACUSTCHANGINFO)
    List<BtCciacustintegAndCciacustchanginfoDto> getCciacustintegAndCciacustChanginfo();


    /**
     * CIA 변동 데이터 조회
     */
    @Query(BtCciaCustIntegSql.SELECT_CCIACUSTINTEG_AND_VWBICCSTBASICINFO)
    public List<BtCciacustintegCsnh625dDto> getInfDeclareCursorCiaWork();


    /**
     * 고객ID가 다른 변동에서 통합될 고객ID로 사용되는지 확인
     * @param dto
     * @return
     */
    @Query(BtCciaCustIntegSql.SELECT_CCIACUSTINTEG_CUSTMER_COUNT)
    public int getInfIsPendingCust(BtCciacustintegCsnh625dDto dto);

    /**
     * ICIS(CCIA_CUST_INTEG)에 처리 결과 값을 갱신
     * @param dto
     * @return
     */
    @Modifying
    @Query(BtCciaCustIntegSql.UPDATE_CCIACUSTINTEG_CIA_FLAG)
    public int updateCiaWork(BtCciacustintegCsnh625dDto dto);






}