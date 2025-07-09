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

package com.kt.icis.oder.baseinfo.csnh.command.repository.sql;

public class CustInfoCurnCmdSql {

    public static final String SELECT_LOB_CD_BY_SA_ID = """
            SELECT LOB_CD FROM (
                SELECT 'PP' AS LOB_CD FROM PP_CCPIPRODHIER WHERE SA_ID = :saId UNION ALL
                SELECT 'IC' AS LOB_CD FROM IC_CCPIPRODHIER WHERE SA_ID = :saId UNION ALL
                SELECT 'EI' AS LOB_CD FROM EI_CCPIPRODHIER WHERE SA_ID = :saId UNION ALL
                SELECT 'IA' AS LOB_CD FROM IA_CCPIPRODHIER WHERE SA_ID = :saId UNION ALL
                SELECT 'IN' AS LOB_CD FROM IN_CCPIPRODHIER WHERE SA_ID = :saId UNION ALL
                SELECT 'ET' AS LOB_CD FROM ET_CCPIPRODHIER WHERE SA_ID = :saId
            ) WHERE ROWNUM = 1
            """;

}
