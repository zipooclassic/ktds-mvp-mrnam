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

public class BtCcpiprodhierSql {

    public final static String SELECT_CCPIPRODHIER_REWORK_SVCNAME = """
       SELECT  
                 TCCD.SA_ID
                ,TCCD.CUST_ID
                ,TCCD.OCCUR_DATE
                ,TCCD.NEW_CUST_ID
                ,TCCD.PROC_RESULT
        FROM     BI_CCIA_CUST_DIVD  TCCD
        		LEFT OUTER JOIN (
        						SELECT SA_ID FROM PP_CCPIPRODHIER 
								UNION ALL
								SELECT SA_ID FROM IC_CCPIPRODHIER
								UNION ALL
								SELECT SA_ID FROM EI_CCPIPRODHIER
								UNION ALL
								SELECT SA_ID FROM IA_CCPIPRODHIER
								UNION ALL
								SELECT SA_ID FROM IN_CCPIPRODHIER
								UNION ALL 
								SELECT SA_ID FROM ET_CCPIPRODHIER
 								)TC 	
                	ON TCCD.SA_ID = TC.SA_ID                
                LEFT OUTER JOIN VW_BI_CCSTBASICINFO VBC
                	ON TCCD.CUST_ID = VBC.CUST_ID
        WHERE   TCCD.PROC_RESULT   =   'P'
            ;
            """;

    
}
