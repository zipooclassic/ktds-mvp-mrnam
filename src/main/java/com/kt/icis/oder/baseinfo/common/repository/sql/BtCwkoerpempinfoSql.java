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

public class BtCwkoerpempinfoSql {

    public final static String SELECT_CWKOERPEMPINFO_WOFLAG_AND_EMPNO = """
                SELECT
                   ROWIDTOCHAR(A.ROWID) AS ROW_ID
                  ,A.EMP_NO
                  ,A.EMP_NO_OLD
                  ,A.EMP_NM
                  ,A.STATUS_CD
                  ,A.DEPT_CD
                  ,A.NO_FORM_CD
                  ,A.NOR_TEL_NO
                  ,A.MAILID
                  ,A.HANDPHONE_NO
                  ,A.JOB_CD
                  ,A.CONTENT
                  ,A.REPL_DT
                  ,A.PERSG
                  ,A.ZBIZUNIT_CD
                  ,A.ZMANAGER_NO
                  ,A.EAI_STATE
                  ,A.EAI_OP
                  ,A.EAI_CDATE
                  ,A.EAI_UDATE
                  ,TO_CHAR(A.EAI_SEQ) AS EAI_SEQ
                  ,A.SOCIAL_NO
            FROM   WC_CWKOERPEMPINFO A
            WHERE  A.WO_FLAG = '0'
            --  AND  SUBSTR(A.EMP_NO,-1,1) = SUBSTR(:pgmName,-1,1)  -- 더 이상 사용하지 않음, 테스트 이후 삭제예정임
            ;
                """;

}
