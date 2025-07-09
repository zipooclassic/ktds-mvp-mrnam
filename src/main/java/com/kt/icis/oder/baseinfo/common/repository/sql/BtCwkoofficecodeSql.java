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

public class BtCwkoofficecodeSql {

    public final static String SELECT_CWKOOFFICECODE_BY_WO_FLAG_ZERO  = """
    		SELECT /*+ INDEX(A BI_CWKOOFFICECODE_IX01) */
	               ROWIDTOCHAR(A.ROWID) AS ROW_ID
	              ,A.OFFICECODE
	              ,A.OFFICENAME
	              ,A.PARENTCODEID
	              ,A.ORGCODEBU
	              ,A.ORGCODEBU_NAME
	              ,A.ORGCODEBR
	              ,A.ORGCODEBR_NAME
	              ,A.ORGCODENW
	              ,A.ORGCODENW_NAME
	              ,A.ORGCODEETC
	              ,A.ORGCODEETC_NAME
	              ,A.LOCCODE
	              ,A.OFFICETYPE
	              ,A.OFFICETYPE_NAME
	              ,A.DONGCODE
	              ,A.DONGCODE_NAME
	              ,A.ADDRTYPE
	              ,A.ADDRTYPENAME
	              ,A.BUNJI
	              ,A.HO
	              ,A.NOTE
	              ,A.OFFICENICK
	              ,A.DETAILADDR
	              ,A.CREATIONDTIME
	              ,A.LASTMODIFICATIONDTIME
	              ,A.LASTMODIFICATIONUSRID
	              ,A.STATUS
	              ,A.ACTIVE
	              ,A.EAI_SEQ
	              ,A.EAI_STATE
	              ,A.EAI_OP
	              ,A.EAI_CDATE
	              ,A.EAI_UDATE
	              ,A.EAI_MSG
	              ,A.EAI_SYSTEM_CD
	              ,A.WO_FLAG
	              ,A.WO_TIME
	              ,A.WO_ERR_MSG
	        FROM   BI_CWKOOFFICECODE A
	        WHERE  A.WO_FLAG = '0'
	        ;
    """;

    public final static String UPDATE_WKOOFFICECODE_WO_FLAG_BY_ROWID = """
            UPDATE BI_CWKOOFFICECODE
	        SET    WO_FLAG    = :woFlag
	              ,WO_TIME    = SYSDATE
	              ,WO_ERR_MSG = :woErrMsg
	        WHERE  ROWID      = CHARTOROWID(:rowId)
            ;
    """;
}
