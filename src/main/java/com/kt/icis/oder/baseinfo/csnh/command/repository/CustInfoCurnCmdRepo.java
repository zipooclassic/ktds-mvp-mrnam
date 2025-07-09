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

package com.kt.icis.oder.baseinfo.csnh.command.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kt.icis.oder.baseinfo.csnh.command.repository.entity.CustInfoCurnCmdEntt;
import com.kt.icis.oder.baseinfo.csnh.command.repository.sql.CustInfoCurnCmdSql;

@Repository
public interface CustInfoCurnCmdRepo extends CrudRepository<CustInfoCurnCmdEntt,String> {

    @Query(CustInfoCurnCmdSql.SELECT_LOB_CD_BY_SA_ID)
    public String selectLobCdBySaId(String saId);

}
