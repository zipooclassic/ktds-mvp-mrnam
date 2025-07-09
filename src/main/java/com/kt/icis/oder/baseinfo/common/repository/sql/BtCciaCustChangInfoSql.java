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

public class BtCciaCustChangInfoSql {
    
	
	//CIA 변동 데이터 조회
    public final static String SELECT_BI_CCIA_CUST_CHANG_INFO_AND_VWBICCSTBASICINFO= """
		SELECT  CIA.CUST_ID 
		       ,CIA.OCCUR_DATE 
		       ,NVL2(CIA.ICIS_CUST_ID, 1, 0) EXIST 
		FROM ( 
		    SELECT  /*+ ORDERED USE_NL(C A) INDEX(A BI_CCIA_CUST_CHANG_INFO_IX01) */
		            A.CUST_ID 
		           ,A.OCCUR_DATE 
		           ,C.CUST_ID                ICIS_CUST_ID 
		           ,NVL(C.CUST_NO_TYPE, '*') ICIS_CUST_NO_TYPE 
		      FROM  BI_CCIA_CUST_CHANG_INFO A 
		           ,VW_BI_CCSTBASICINFO     C 
		     WHERE  1=1 
		       AND  A.PROC_RESULT   IN ('0', 'T') 
		       AND  A.CUST_ID       =   C.CUST_ID(+) 
		     ORDER BY OCCUR_DATE 
		     ) CIA 
		WHERE CIA.ICIS_CUST_NO_TYPE <> '3' 
		   OR (CIA.ICIS_CUST_NO_TYPE = '3' AND TO_CHAR(SYSDATE, 'HH24') BETWEEN  (SELECT remark FROM BI_CSYSCD WHERE GRP_ID = '0Z00' AND CD = 'S') AND (SELECT remark FROM BI_CSYSCD WHERE GRP_ID = '0Z00' AND CD = 'E'));
            """;
    
    
    /* 34. CIA연동 테이블에 작업중 플래그 처리 */
    public static final String UPDATE_CCIACUSTCHANGINFO_CIA_FLAG = """
			  UPDATE  BI_CCIA_CUST_CHANG_INFO
				SET
    				PROC_RESULT = :#{#dto.procResult?:''}
			       ,ERR_MSG = :#{#dto.errMsg?:''}
			       ,PROC_DATE = SYSDATE
			  WHERE CUST_ID = :#{#dto.custId?:''}
			  AND OCCUR_DATE = :#{#dto.occurDate?:''}
            ;
            """;

}
