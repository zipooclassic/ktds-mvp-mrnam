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

public class BtCwkoCiaSaTargInfoSql {
    public final static String SELECT_COUNT_CWKOCIASATARGINFO = """
                    SELECT
                        COUNT(*)
                  FROM  WC_CWKO_CIA_SA_TARG_INFO A
                 WHERE  CUST_PROC_TYPE  = :custProcType
                   AND  CUST_ID         = :custId
                   AND  OCCUR_DATE      = :occurDate
                   AND  STATUS_CD       IN ('0','E','P','R')
            ;
                            """;

    public final static String SELECT_GET_SAID_SACD = """
                    SELECT
                        A.SA_ID
                      , A.SA_CD
                  FROM  WC_CWKO_CIA_SA_TARG_INFO A
                 /*    ,BI_MPRDCD B */            /* 채완일_20170604_성장형 상품 제외처리 추가 20170704 원복처리  */
                 WHERE  A.CUST_PROC_TYPE  = :custProcType
                   AND  A.CUST_ID         = :custId
                   AND  A.OCCUR_DATE      = :occurDate
                   AND  A.PROC_RESV_DATE <= SYSDATE
                   AND  A.STATUS_CD       IN ( '0', 'E' )
                /* AND NVL(B.KOS_PROD_FLAG, '0' ) <> '1' */ /* 채완일_20170604_성장형 상품 제외처리 추가  20170704 원복처리 */
                /* AND A.SA_CD = B.SA_CD                 */ /* 채완일_20170604_성장형 상품 제외처리 추가  20170704 원복처리 */
            ;
                    """;

    public static final String SELECT_SERVIE_CONSTRACT_INFOMATION = """
            SELECT
                          'I'                    -- CUST_PROC_TYPE
                        , :#{#dto.custId?:''}    -- CUST_ID
                        , :#{#dto.occurDate?:''} -- OCCUR_DATE
                        , H.TOP_SA_ID            -- SA_ID
                        , H.SA_CD                -- SA_CD
                        , SYSDATE                -- REG_DATE
                        , 'csnh625d'             -- REG_MODULE_ID
                        , '0'                    -- STATUS_CD
                        , '0'                    -- RETRY_CNT
                        , SYSDATE                -- PROC_RESV_DATE
                        , '0'                    -- COMB_RESULT_CD
                  FROM    (SELECT TOP_SA_ID ,SA_CD FROM PP_CCPIPRODHIER
                           WHERE P_NODE_TYPE = 'C'
                           AND P_NODE_ID = :#{#dto.custId?:''}
                           AND SA_ID = TOP_SA_ID
                           AND END_DATE IS NULL
                           UNION
                           SELECT TOP_SA_ID ,SA_CD FROM IC_CCPIPRODHIER
                           WHERE P_NODE_TYPE = 'C'
                           AND P_NODE_ID = :#{#dto.custId?:''}
                           AND SA_ID = TOP_SA_ID
                           AND END_DATE IS NULL
                           UNION
                           SELECT TOP_SA_ID ,SA_CD FROM EI_CCPIPRODHIER
                           WHERE P_NODE_TYPE = 'C'
                           AND P_NODE_ID = :#{#dto.custId?:''}
                           AND SA_ID = TOP_SA_ID
                           AND END_DATE IS NULL
                           UNION
                           SELECT TOP_SA_ID ,SA_CD FROM IA_CCPIPRODHIER
                           WHERE P_NODE_TYPE = 'C'
                           AND P_NODE_ID = :#{#dto.custId?:''}
                           AND SA_ID = TOP_SA_ID
                           AND END_DATE IS NULL
                           UNION
                           SELECT TOP_SA_ID ,SA_CD FROM IN_CCPIPRODHIER
                           WHERE P_NODE_TYPE = 'C'
                           AND P_NODE_ID = :#{#dto.custId?:''}
                           AND SA_ID = TOP_SA_ID
                           AND END_DATE IS NULL
                           UNION
                           SELECT TOP_SA_ID ,SA_CD FROM ET_CCPIPRODHIER
                           WHERE P_NODE_TYPE = 'C'
                           AND P_NODE_ID = :#{#dto.custId?:''}
                           AND SA_ID = TOP_SA_ID
                           AND END_DATE IS NULL
                           ) H LEFT OUTER JOIN BI_CSYSCIASYNCMODULE S
                                ON H.SA_CD = S.SA_CD
                           WHERE S.CUST_PROC_TYPE = 'I'
                           AND S.PROC_PRIORITY_TYPE = 'MAIN'
                           AND S.START_DATE IS NOT NULL
                           AND TO_CHAR(SYSDATE, 'YYYYMMDD') BETWEEN S.START_DATE AND NVL(S.END_DATE, '99999999');
            """;

    public static final String SELECT_SERVIE_CONSTRACT_SA_TRAG_INFOMATION = """
            SELECT
                          'C'                    -- CUST_PROC_TYPE
                        , :#{#dto.custId?:''}    -- CUST_ID
                        , :#{#dto.occurDate?:''} -- OCCUR_DATE
                        , H.TOP_SA_ID            -- SA_ID
                        , H.SA_CD                -- SA_CD
                        , SYSDATE                -- REG_DATE
                        , 'csnh625d'             -- REG_MODULE_ID
                        , '0'                    -- STATUS_CD
                        , '0'                    -- RETRY_CNT
                        , SYSDATE                -- PROC_RESV_DATE
                        , '0'                    -- COMB_RESULT_CD
                  FROM    (SELECT TOP_SA_ID ,SA_CD FROM PP_CCPIPRODHIER
                           WHERE P_NODE_TYPE = 'C'
                           AND P_NODE_ID = :#{#dto.custId?:''}
                           AND SA_ID = TOP_SA_ID
                           AND END_DATE IS NULL
                           UNION
                           SELECT TOP_SA_ID ,SA_CD FROM IC_CCPIPRODHIER
                           WHERE P_NODE_TYPE = 'C'
                           AND P_NODE_ID = :#{#dto.custId?:''}
                           AND SA_ID = TOP_SA_ID
                           AND END_DATE IS NULL
                           UNION
                           SELECT TOP_SA_ID ,SA_CD FROM EI_CCPIPRODHIER
                           WHERE P_NODE_TYPE = 'C'
                           AND P_NODE_ID = :#{#dto.custId?:''}
                           AND SA_ID = TOP_SA_ID
                           AND END_DATE IS NULL
                           UNION
                           SELECT TOP_SA_ID ,SA_CD FROM IA_CCPIPRODHIER
                           WHERE P_NODE_TYPE = 'C'
                           AND P_NODE_ID = :#{#dto.custId?:''}
                           AND SA_ID = TOP_SA_ID
                           AND END_DATE IS NULL
                           UNION
                           SELECT TOP_SA_ID ,SA_CD FROM IN_CCPIPRODHIER
                           WHERE P_NODE_TYPE = 'C'
                           AND P_NODE_ID = :#{#dto.custId?:''}
                           AND SA_ID = TOP_SA_ID
                           AND END_DATE IS NULL
                           UNION
                           SELECT TOP_SA_ID ,SA_CD FROM ET_CCPIPRODHIER
                           WHERE P_NODE_TYPE = 'C'
                           AND P_NODE_ID = :#{#dto.custId?:''}
                           AND SA_ID = TOP_SA_ID
                           AND END_DATE IS NULL
                           ) H LEFT OUTER JOIN BI_CSYSCIASYNCMODULE S
                                ON H.SA_CD = S.SA_CD
                           WHERE S.CUST_PROC_TYPE = 'C'
                           AND S.PROC_PRIORITY_TYPE = 'MAIN'
                           AND S.START_DATE IS NOT NULL
                           AND TO_CHAR(SYSDATE, 'YYYYMMDD') BETWEEN S.START_DATE AND NVL(S.END_DATE, '99999999');
            """;
    
    public static final String SELECT_STATUSCD_AND_PROCDATE_COUNT = """
            SELECT COUNT(1) AS CNT
            FROM WC_CWKO_CIA_SA_TARG_INFO
            WHERE STATUS_CD = 'P'
              AND PROC_DATE < SYSDATE - (1/24/6)
       ;
               """;
       
}
