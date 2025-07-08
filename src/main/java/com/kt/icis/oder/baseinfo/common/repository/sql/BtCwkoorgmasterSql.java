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
package com.kt.icis.oder.baseinfo.common.repository.sql;

public class BtCwkoorgmasterSql {

    public final static String SELECT_CWKOORGMASTER_WOFLAG_AND_EMPNO = """
                    SELECT
                   ROWIDTOCHAR(A.ROWID) AS ROW_ID
                  ,A.ORGCODE
                  ,A.PARENTORGCODE
                  ,A.ORGNAME
                  ,A.ORGENGNAME
                  ,A.DIVISIONWORK
                  ,A.ZIPID
                  ,A.POSTALCD
                  ,A.ADDR1
                  ,A.ADDR2
                  ,A.BUSINESSGB
                  ,A.ORGTYPE
                  ,A.ISPAYORG
                  ,A.TELNO
                  ,A.FAXNO
                  ,A.ISPROFITCENTER
                  ,A.PROFITCENTER
                  ,A.ISCOSTCENTER
                  ,A.COSTCENTER
                  ,A.ISEVALUATE
                  ,A.HEADCODE
                  ,A.ORGLEVEL
                  ,A.ORGORDER
                  ,A.FORMORDER
                  ,A.ISLEAF
                  ,A.CREATIONDTIME
                  ,A.UPDATEDTIME
                  ,A.ACTIVE
                  ,A.CAPEMPNO
                  ,A.ORGGRADE
                  ,A.LASTUPDATOR
                  ,A.ORGSYMNAME
                  ,TO_CHAR(A.EAI_SEQ) AS EAI_SEQ
                  ,A.EAI_STATE
                  ,A.EAI_OP
                  ,A.EAI_CDATE
                  ,A.EAI_UDATE
                  ,A.EAI_MSG
            FROM   WC_CWKOORGMASTER A
            WHERE  A.WO_FLAG = '0'
            --  AND  SUBSTR(A.ORGCODE,-1,1) = SUBSTR(:pgmName,-1,1) -- 더 이상 사용하지 않음, 테스트 이후 삭제예정임
            ;
                    """;

}
