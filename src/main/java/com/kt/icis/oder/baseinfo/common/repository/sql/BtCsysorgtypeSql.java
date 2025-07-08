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

import org.apache.ibatis.jdbc.SQL;

import com.kt.icis.oder.baseinfo.common.repository.dto.BtCsysorgtypeDto;

public class BtCsysorgtypeSql {
	public String insertCsysortype(final BtCsysorgtypeDto inDto) {
		return new SQL()
			.INSERT_INTO (" BI_CSYSORGTYPE ")
			.INTO_COLUMNS(
					 "ORGTYPE"
                    ,"BUSINESSGB"
                    ,"ORGTYPENAME"
                    ,"DISSEQ"
                    ,"ACTIVE"
                    ,"CREATIONDTIME"
                    ,"UPDATEDTIME"	
                    ,"REG_DATE"
                    ,"CHNG_DATE"
                    ,"EAI_SEQ"
					)
    		.INTO_VALUES(
    				 "#{orgtype}"
                    ,"#{businessgb}"
                    ,"#{orgtypename}"
                    ,"#{disseq}"
                    ,"#{active}"
                    ,"#{creationdtime}"
                    ,"#{updatedtime}"
                    ,"SYSDATE"
                    ,"SYSDATE"
                    ,"#{eaiSeq}"
    				)
    		.toString();
	}
	
	public String deleteCsysortype(final String orgtype) {
		return new SQL()
		    .DELETE_FROM("/*+ INDEX(A BI_CSYSORGTYPE_PK) */ BI_CSYSORGTYPE A")
		    .WHERE("A.ORGTYPE  = #{orgtype}")
		    .toString();
	}
	
	public String updateCsysortype(final BtCsysorgtypeDto inDto) {
		return new SQL()
		    .UPDATE("/*+ INDEX(A BI_CSYSORGTYPE_PK) */ BI_CSYSORGTYPE A")
		    .SET(
	    		   "A.ORGTYPE       = #{orgtype}"
	              ,"A.BUSINESSGB    = #{businessgb}"
	              ,"A.ORGTYPENAME   = #{orgtypename}"
	              ,"A.DISSEQ        = #{disseq}"
	              ,"A.ACTIVE        = #{active}"
	              ,"A.CREATIONDTIME = #{creationdtime}"
	              ,"A.UPDATEDTIME   = #{updatedtime}"
	              ,"A.CHNG_DATE     = SYSDATE"
	              ,"A.EAI_SEQ       = #{eaiSeq}")
		    .WHERE("A.ORGTYPE = #{orgtype}")
		    .toString();
		    
	}
}
