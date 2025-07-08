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

public class BtCordordinfoSql {

    public final static String SELECT_CORDORDINFO_WORKING_SVCNAME = """
       SELECT   
    		ORD_NO
	   FROM  (   
			SELECT ORD_NO FROM IC_CORDORDINFO
			WHERE     COMPLETE_FLAG   =   '0'
			AND     SUBSTR(ORD_TYPE_CD, 2, 1) <> 'O'
			AND SA_ID	= :saId
			UNION ALL
			SELECT ORD_NO FROM EI_CORDORDINFO
			WHERE     COMPLETE_FLAG   =   '0'
			AND     SUBSTR(ORD_TYPE_CD, 2, 1) <> 'O'
			AND SA_ID	=  :saId
			UNION ALL
			SELECT ORD_NO FROM IA_CORDORDINFO
			WHERE     COMPLETE_FLAG   =   '0'
			AND     SUBSTR(ORD_TYPE_CD, 2, 1) <> 'O'
			AND SA_ID	=  :saId
			UNION ALL
			SELECT ORD_NO FROM IN_CORDORDINFO
			WHERE     COMPLETE_FLAG   =   '0'
			AND     SUBSTR(ORD_TYPE_CD, 2, 1) <> 'O'
			AND SA_ID	=  :saId
			UNION ALL
			SELECT ORD_NO FROM ET_CORDORDINFO
			WHERE     COMPLETE_FLAG   =   '0'
			AND     SUBSTR(ORD_TYPE_CD, 2, 1) <> 'O'
			AND SA_ID	= :saId
			UNION ALL
			SELECT ORD_NO FROM PP_CORDORDINFO
			WHERE	COMPLETE_FLAG   =   '0'
			AND	SUBSTR(ORD_TYPE_CD, 2, 1) <> 'O'
			AND SA_ID	= :saId
			) CORDORDINFO
       WHERE	ROWNUM = 1
            ;
            """;

   
}
