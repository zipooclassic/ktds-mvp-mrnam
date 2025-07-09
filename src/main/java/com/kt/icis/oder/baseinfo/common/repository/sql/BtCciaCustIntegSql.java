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

public class BtCciaCustIntegSql {
    public final static String SELECT_CCIACUSTINTEG_AND_CCIACUSTCHANGINFO= """
                    SELECT X.CUST_PROC_TYPE AS CUST_PROC_TYPE
		                    , X.CUST_ID AS CUST_ID
		                    , X.OCCUR_DATE AS OCCUR_DATE
		                    FROM (
		                        SELECT T.CUST_PROC_TYPE
		                            , T.CUST_ID
		                            , T.OCCUR_DATE
		                            , T.PROC_NO
		                        FROM (
		                            SELECT /*+ INDEX(A BI_CCIA_CUST_INTEG_IX01) */
		                                'I' AS CUST_PROC_TYPE
		                                , A.CUST_ID
		                                , A.OCCUR_DATE
		                                , A.PROC_DATE
		                                , A.PROC_RESULT
		                                , A.ERR_MSG
		                                , DECODE(SUBSTR(A.CUST_ID,11,1),'0','0','1','1','2','2','3','3','4','4','5','5','6','6','7','7','8','8','9','9','C','C','N') PROC_NO
		                            FROM BI_CCIA_CUST_INTEG A
		                            WHERE A.PROC_RESULT = 'P'
		                            UNION ALL
		                            SELECT /*+ INDEX(B BI_CCIA_CUST_CHANG_INFO_IX01) */
		                                    'C' AS CUST_PROC_TYPE
		                                , B.CUST_ID
		                                , B.OCCUR_DATE
		                                , B.PROC_DATE
		                                , B.PROC_RESULT
		                                , B.ERR_MSG
		                                , DECODE(SUBSTR(B.CUST_ID,11,1),'0','0','1','1','2','2','3','3','4','4','5','5','6','6','7','7','8','8','9','9','C','C','N') PROC_NO
		                            FROM  BI_CCIA_CUST_CHANG_INFO B
		                            WHERE B.PROC_RESULT = 'P'
		                        ) T
		                        WHERE T.PROC_NO IN ('0','1','2','3','4','5','6','7','8','9','N','C')
		                ) X
		                ORDER BY X.OCCUR_DATE
            ;
                    """;

    //CIA 변동 데이터 조회 
    public final static String SELECT_CCIACUSTINTEG_AND_VWBICCSTBASICINFO= """
            SELECT  CIA.CUST_ID
                   ,CIA.OCCUR_DATE
                   ,NVL2(CIA.ICIS_CUST_ID, 1, 0) EXIST
            FROM (
                    SELECT
                            I.CUST_ID
                          , I.OCCUR_DATE
                          , C.CUST_ID                   ICIS_CUST_ID
                          , NVL(C.CUST_NO_TYPE, '*')    ICIS_CUST_NO_TYPE
                    FROM
                    (
                        SELECT  /*+ FULL(A) PARALLEL(A 4) */
                                RANK() OVER(PARTITION BY CUST_ID, INTEG_CUST_ID ORDER BY OCCUR_DATE) RANK
                              , A.CUST_ID
                              , A.INTEG_CUST_ID
                              , A.PROC_RESULT
                              , A.OCCUR_DATE
                          FROM BI_CCIA_CUST_INTEG A
                         WHERE PROC_RESULT IN ('0','P','F','T')
                    ) I
                    , VW_BI_CCSTBASICINFO C
                    WHERE I.RANK = 1
                      AND I.PROC_RESULT IN ('0','T')
                      AND I.CUST_ID = C.CUST_ID(+)
                    ORDER BY OCCUR_DATE
                  ) CIA
            WHERE CIA.ICIS_CUST_NO_TYPE <> '3'
               OR (CIA.ICIS_CUST_NO_TYPE = '3' AND TO_CHAR(SYSDATE, 'HH24') BETWEEN  (SELECT remark FROM BI_CSYSCD WHERE GRP_ID = '0Z00' AND CD = 'S') AND (SELECT remark FROM BI_CSYSCD WHERE GRP_ID = '0Z00' AND CD = 'E'));
                     """;

    /* 고객ID가 다른 변동에서 통합될 고객ID로 사용되는지 확인 */
    public final static String SELECT_CCIACUSTINTEG_CUSTMER_COUNT= """
            SELECT
            	COUNT(*) AS CNT
            FROM BI_CCIA_CUST_INTEG A
            WHERE A.INTEG_CUST_ID = :#{#dto.custId?:''}
            AND A.PROC_RESULT IN ('0','P','F','T');
                     """;

    /* 34. CIA연동 테이블에 작업중 플래그 처리 */
    public static final String UPDATE_CCIACUSTINTEG_CIA_FLAG = """
             UPDATE  BI_CCIA_CUST_INTEG
            	SET
            				PROC_RESULT = :#{#dto.procResult?:''}
                  ,ERR_MSG = :#{#dto.errMsg?:''}
                  ,PROC_DATE = SYSDATE
             WHERE CUST_ID = :#{#dto.custId?:''}
             AND OCCUR_DATE = :#{#dto.occurDate?:''}
                    ;
                    """;

}
