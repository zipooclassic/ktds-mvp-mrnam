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

public class BtCwkoorgtypeSql {

	public final static String SELECT_CWKOORGTYPE_BY_WO_FLAG_ZERO  = """
    		SELECT /*+ INDEX(A BI_CWKOORGTYPE_IX01) */
	               ROWIDTOCHAR(A.ROWID) AS ROW_ID
	              ,A.ORGTYPE
	              ,A.BUSINESSGB
	              ,A.ORGTYPENAME
	              ,A.DISSEQ
	              ,A.ACTIVE
	              ,A.CREATIONDTIME
	              ,A.UPDATEDTIME
	              ,A.EAI_SEQ
	              ,A.EAI_STATE
	              ,A.EAI_OP
	              ,A.EAI_CDATE
	              ,A.EAI_UDATE
	              ,A.EAI_MSG
	              ,A.WO_FLAG
	              ,A.WO_TIME
	              ,A.WO_ERR_MSG
	        FROM   BI_CWKOORGTYPE A
	        WHERE  A.WO_FLAG = '0'
	        ;
    """;

    public final static String UPDATE_CWKOORGTYPE_WO_FLAG_BY_ROWID = """
            UPDATE BI_CWKOORGTYPE
	        SET    WO_FLAG    = :woFlag
	              ,WO_TIME    = SYSDATE
	              ,WO_ERR_MSG = :woErrMsg
	        WHERE  ROWID      = CHARTOROWID(:rowId)
            ;
    """;
}
