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

public class BtInCordordinfoSql {

    public static final String SELECT_CORDORDINFO_MPRDRELTNINFO = """
            SELECT ORD_NO
            FROM     IN_CORDORDINFO
            WHERE   SA_ID           =   :saId
            AND     COMPLETE_FLAG   =   '0'
            AND     SUBSTR(ORD_TYPE_CD, 2, 1) <> 'O'
            AND     SA_CD NOT LIKE 'C%'
            AND     ROWNUM = 1
            """;
}
