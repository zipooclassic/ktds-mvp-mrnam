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

package com.kt.icis.oder.baseinfo.csnl.command.repository.sql;

public class Csnl400dCmdSql {

    public final static String SELECT_CCSTCUSTAGENT_BY_WOFLAG_ORDERBY_EAISEQ = """
            SELECT
                TO_CHAR(AGNT_BTHDAY, 'YYYYMMDD') AS AGNT_BTHDAY
                , AGNT_EMAIL
                , AGNT_MPHON_NO
                , AGNT_SEX_CD
                , TO_CHAR(AGNT_START_DATE, 'YYYYMMDDHH24MISS') AS AGNT_START_DATE
                , GENESIS_CUST_ID
                , GENESIS_CUST_NAME
                , ICIS_CUST_ID
                , COP_REG_NO
                , ACTV_YN
                , AGNT_NAME
                , TO_CHAR(RCVER_BTHDAY, 'YYYYMMDD') AS RCVER_BTHDAY
                , RCVER_EMAIL
                , RCVER_MPHON_NO
                , RCVER_NAME
                , RCVER_SEX_CD
                , EAI_SEQ
                , EAI_TRT_DIV_CD
                , GENESIS_AGENT_ID
                , TO_CHAR(AGNT_END_DATE, 'YYYYMMDDHH24MISS') AS AGNT_END_DATE /* 20170226 */
                , AGNT_SEQ /* 20170226 */
            FROM
                BI_CCSTCUSTAGENT
            WHERE
                WO_FLAG = '0'
            ORDER BY
                EAI_SEQ
            """;

    public final static String UPDATE_CCSTCUSTAGENT_WOFLAG_WODATE = """
            UPDATE
                BI_CCSTCUSTAGENT
            SET
                WO_FLAG = :#{#dto.woFlag?:''}
                , WO_DATE = SYSDATE
            WHERE
                ICIS_CUST_ID = :#{#dto.icisCustId?:''}
                AND EAI_SEQ  = :#{#dto.eaiSeq?:''}
                AND WO_FLAG = '0'
            """;

}
