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

public class BtCciaCustDivdSql {

    public final static String SELECT_CCIACUSTDIVD_REWORK_SVCNAME = """
            SELECT /*+ USE_NL(TCCD TC VBC)
                       INDEX(TCCD BI_CCIA_CUST_DIVD_IX01)
                       INDEX(TC PP_CCPIPRODHIER_PK)   */
                   TCCD.SA_ID
                 , TCCD.CUST_ID
                 , TCCD.OCCUR_DATE
                 , TCCD.NEW_CUST_ID
                 , TCCD.PROC_RESULT
              FROM BI_CCIA_CUST_DIVD TCCD LEFT OUTER JOIN PP_CCPIPRODHIER TC ON TCCD.SA_ID = TC.SA_ID
                                          LEFT OUTER JOIN VW_BI_CCSTBASICINFO VBC ON TCCD.CUST_ID = VBC.CUST_ID
             WHERE TCCD.PROC_RESULT = 'P'
             AND TCCD.SYNC_HST_NO IS NOT NULL
             UNION ALL
            SELECT /*+ USE_NL(TCCD TC VBC)
                       INDEX(TCCD BI_CCIA_CUST_DIVD_IX01)
                       INDEX(TC IC_CCPIPRODHIER_PK)   */
                   TCCD.SA_ID
                 , TCCD.CUST_ID
                 , TCCD.OCCUR_DATE
                 , TCCD.NEW_CUST_ID
                 , TCCD.PROC_RESULT
              FROM BI_CCIA_CUST_DIVD TCCD LEFT OUTER JOIN IC_CCPIPRODHIER TC ON TCCD.SA_ID = TC.SA_ID
                                          LEFT OUTER JOIN VW_BI_CCSTBASICINFO VBC ON TCCD.CUST_ID = VBC.CUST_ID
             WHERE TCCD.PROC_RESULT = 'P'
             AND TCCD.SYNC_HST_NO IS NOT NULL
             UNION ALL
            SELECT /*+ USE_NL(TCCD TC VBC)
                       INDEX(TCCD BI_CCIA_CUST_DIVD_IX01)
                       INDEX(TC EI_CCPIPRODHIER_PK)   */
                   TCCD.SA_ID
                 , TCCD.CUST_ID
                 , TCCD.OCCUR_DATE
                 , TCCD.NEW_CUST_ID
                 , TCCD.PROC_RESULT
              FROM BI_CCIA_CUST_DIVD TCCD LEFT OUTER JOIN EI_CCPIPRODHIER TC ON TCCD.SA_ID = TC.SA_ID
                                          LEFT OUTER JOIN VW_BI_CCSTBASICINFO VBC ON TCCD.CUST_ID = VBC.CUST_ID
             WHERE TCCD.PROC_RESULT = 'P'
             AND TCCD.SYNC_HST_NO IS NOT NULL
             UNION ALL
            SELECT /*+ USE_NL(TCCD TC VBC)
                       INDEX(TCCD BI_CCIA_CUST_DIVD_IX01)
                       INDEX(TC IA_CCPIPRODHIER_PK)   */
                   TCCD.SA_ID
                 , TCCD.CUST_ID
                 , TCCD.OCCUR_DATE
                 , TCCD.NEW_CUST_ID
                 , TCCD.PROC_RESULT
              FROM BI_CCIA_CUST_DIVD TCCD LEFT OUTER JOIN IA_CCPIPRODHIER TC ON TCCD.SA_ID = TC.SA_ID
                                          LEFT OUTER JOIN VW_BI_CCSTBASICINFO VBC ON TCCD.CUST_ID = VBC.CUST_ID
             WHERE TCCD.PROC_RESULT = 'P'
             AND TCCD.SYNC_HST_NO IS NOT NULL
             UNION ALL
            SELECT /*+ USE_NL(TCCD TC VBC)
                       INDEX(TCCD BI_CCIA_CUST_DIVD_IX01)
                       INDEX(TC IN_CCPIPRODHIER_PK)   */
                   TCCD.SA_ID
                 , TCCD.CUST_ID
                 , TCCD.OCCUR_DATE
                 , TCCD.NEW_CUST_ID
                 , TCCD.PROC_RESULT
              FROM BI_CCIA_CUST_DIVD TCCD LEFT OUTER JOIN IN_CCPIPRODHIER TC ON TCCD.SA_ID = TC.SA_ID
                                          LEFT OUTER JOIN VW_BI_CCSTBASICINFO VBC ON TCCD.CUST_ID = VBC.CUST_ID
             WHERE TCCD.PROC_RESULT = 'P'
             AND TCCD.SYNC_HST_NO IS NOT NULL
             UNION ALL
            SELECT /*+ USE_NL(TCCD TC VBC)
                       INDEX(TCCD BI_CCIA_CUST_DIVD_IX01)
                       INDEX(TC ET_CCPIPRODHIER_PK)   */
                   TCCD.SA_ID
                 , TCCD.CUST_ID
                 , TCCD.OCCUR_DATE
                 , TCCD.NEW_CUST_ID
                 , TCCD.PROC_RESULT
              FROM BI_CCIA_CUST_DIVD TCCD LEFT OUTER JOIN ET_CCPIPRODHIER TC ON TCCD.SA_ID = TC.SA_ID
                                          LEFT OUTER JOIN VW_BI_CCSTBASICINFO VBC ON TCCD.CUST_ID = VBC.CUST_ID
             WHERE TCCD.PROC_RESULT = 'P'
             AND TCCD.SYNC_HST_NO IS NOT NULL
            """;


    public final static String UPDATE_CCIACUSTDIVD_ICIS_WORK_WRITER = """
            UPDATE  BI_CCIA_CUST_DIVD
            	SET
            	 PROC_RESULT = :#{#dto.procResult?:''}
            WHERE SA_ID = :#{#dto.saId?:''}
            AND CUST_ID = :#{#dto.custId?:''}
            AND OCCUR_DATE = :#{#dto.occurDate?:''}
            AND NEW_CUST_ID = :#{#dto.newCustId?:''}
                  ;
                          """;


    public static final String SELECT_CCIACUSTDIVD_CCPICRODHIER_CCSTBASICINFO = """
                SELECT /*+ ORDERED USE_NL(A B) INDEX(A BI_CCIA_CUST_DIVD_IX01) */
				         A.SA_ID
				        ,A.CUST_ID
				        ,A.OCCUR_DATE
				        ,A.NEW_CUST_ID
				        ,A.REQER_NAME
				        ,A.REQER_CUST_NO
				        ,A.REQER_PHN_NO
				        ,A.REQER_CELLPHN_NO
				        ,A.REQER_EMAIL
				        ,A.CUST_REL_CD
				        ,A.REQER_REF
				        ,A.PROC_RESULT
				        ,A.ERR_MSG
				        ,TO_CHAR(A.REG_DATE,'YYYYMMDDHH24MISS') AS REG_DATE
				        ,A.REG_OFC_CD
				        ,A.REGR_ID
				        ,A.REGER_EMP_NAME
				        ,A.PGM_ID
				        ,B.P_NODE_ID AS CHECK_OLD_CUST_ID                        /* 2007.12.17 Modified by n75ein    */
				        ,C.CUST_ID AS CHECK_NEW_CUST_ID                          /* 2007.12.17 Modified by n75ein    */
				FROM     BI_CCIA_CUST_DIVD  A
				        ,(
				                        SELECT P_NODE_ID,SA_ID FROM PP_CCPIPRODHIER
				                        UNION ALL
				                        SELECT P_NODE_ID,SA_ID FROM IC_CCPIPRODHIER
				                        UNION ALL
				                        SELECT P_NODE_ID,SA_ID FROM EI_CCPIPRODHIER
				                        UNION ALL
				                        SELECT P_NODE_ID,SA_ID FROM IA_CCPIPRODHIER
				                        UNION ALL
				                        SELECT P_NODE_ID,SA_ID FROM IN_CCPIPRODHIER
				                        UNION ALL
				                        SELECT P_NODE_ID,SA_ID FROM ET_CCPIPRODHIER
				                        )      B               /* 2007.12.17 Modified by n75ein    */
				        ,VW_BI_CCSTBASICINFO   C               /* 2007.12.17 Modified by n75ein    */
				WHERE   A.PROC_RESULT   =   '0'
				AND 	A.SYNC_HST_NO IS NOT NULL
				AND     B.SA_ID(+)      =   A.SA_ID         /* 2007.12.17 Modified by n75ein    */
				AND     C.CUST_ID(+)    =   A.NEW_CUST_ID   /* 2007.12.17 Modified by n75ein    */
	""";

    public static final String UPDATE_CCIACUSTDIVD_SAID_CUSTID_OCCURDATE_NEWCUSTID = """
            UPDATE  BI_CCIA_CUST_DIVD
            SET      PROC_RESULT    =   :#{#dto.procResult?:''}
                    ,PROC_DATE      =   SYSDATE
                    ,ERR_MSG        =   :#{#dto.errMsg?:''}
                    ,IPS_PROC_RESULT=   DECODE(:#{#dto.procResult?:''}, 'F', 'F', 'N', 'N', 'S', '0') /* 2010.12.28 */
            WHERE   SA_ID           =   :#{#dto.saId?:''}
            AND     CUST_ID         =   :#{#dto.custId?:''}
            AND     OCCUR_DATE      =   :#{#dto.occurDate?:''}
            AND     NEW_CUST_ID     =   :#{#dto.newCustId?:''}
            ;
            """;



}
