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

import com.kt.icis.oder.baseinfo.common.repository.dto.BtMsysofficecodeDto;

public class BtMsysofficecodeSql {
	
	public String insertMsysofficecode(final BtMsysofficecodeDto inDto) {
		return new SQL()
			.INSERT_INTO (" BI_MSYSOFFICECODE ")
    		.INTO_COLUMNS(
    				 "OFFICECODE"
                    ,"OFFICENAME"
                    ,"PARENTCODEID"
                    ,"ORGCODEBU"
                    ,"ORGCODEBU_NAME"
                    ,"ORGCODEBR"
                    ,"ORGCODEBR_NAME"
                    ,"ORGCODENW"
                    ,"ORGCODENW_NAME"
                    ,"ORGCODEETC"
                    ,"ORGCODEETC_NAME"
                    ,"LOCCODE"
                    ,"OFFICETYPE"
                    ,"OFFICETYPE_NAME"
                    ,"DONGCODE"
                    ,"DONGCODE_NAME"
                    ,"ADDRTYPE"
                    ,"ADDRTYPENAME"
                    ,"BUNJI"
                    ,"HO"
                    ,"NOTE"
                    ,"OFFICENICK"
                    ,"DETAILADDR"
                    ,"CREATIONDTIME"
                    ,"LASTMODIFICATIONDTIME"
                    ,"LASTMODIFICATIONUSRID"
                    ,"STATUS"
                    ,"ACTIVE"
                    ,"REG_DATE"
                    ,"CHNG_DATE"
                    ,"EAI_SEQ"
    				)
    		.INTO_VALUES(
                    "#{officecode}"
                   ,"#{officename}"
                   ,"#{parentcodeid}"
                   ,"#{orgcodebu}"
                   ,"#{orgcodebuName}"
                   ,"#{orgcodebr}"
                   ,"#{orgcodebrName}"
                   ,"#{orgcodenw}"
                   ,"#{orgcodenwName}"
                   ,"#{orgcodeetc}"
                   ,"#{orgcodeetcName}"
                   ,"#{loccode}"
                   ,"#{officetype}"
                   ,"#{officetypeName}"
                   ,"#{dongcode}"
                   ,"#{dongcodeName}"
                   ,"#{addrtype}"
                   ,"#{addrtypename}"
                   ,"#{bunji}"
                   ,"#{ho}"
                   ,"#{note}"
                   ,"#{officenick}"
                   ,"#{detailaddr}"
                   ,"#{creationdtime}"
                   ,"#{lastmodificationdtime}"
                   ,"#{lastmodificationusrid}"
                   ,"#{status}"
                   ,"#{active}"
                   ,"SYSDATE"
                   ,"SYSDATE"
                   ,"TO_NUMBER(#{eaiSeq})"
    				)
		    .toString();
	}
    
	public String deleteMsysofficecode(final String officecode) {
		return new SQL()
		    .DELETE_FROM("/*+ INDEX(A BI_MSYSOFFICECODE_PK) */ BI_MSYSOFFICECODE A")
		    .WHERE("A.OFFICECODE  = #{officecode}")
		    .toString();
	}
	
	public String updateMsysofficecode(final BtMsysofficecodeDto inDto) {
		return new SQL()
		    .UPDATE("/*+ INDEX(A BI_MSYSOFFICECODE_PK) */ BI_MSYSOFFICECODE A")
		    .SET(
	               "A.OFFICECODE            = #{officecode}"
	              ,"A.OFFICENAME            = #{officename}"
	              ,"A.PARENTCODEID          = #{parentcodeid}"
	              ,"A.ORGCODEBU             = #{orgcodebu}"
	              ,"A.ORGCODEBU_NAME        = #{orgcodebuName}"
	              ,"A.ORGCODEBR             = #{orgcodebr}"
	              ,"A.ORGCODEBR_NAME        = #{orgcodebrName}"
	              ,"A.ORGCODENW             = #{orgcodenw}"
	              ,"A.ORGCODENW_NAME        = #{orgcodenwName}"
	              ,"A.ORGCODEETC            = #{orgcodeetc}"
	              ,"A.ORGCODEETC_NAME       = #{orgcodeetcName}"
	              ,"A.LOCCODE               = #{loccode}"
	              ,"A.OFFICETYPE            = #{officetype}"
	              ,"A.OFFICETYPE_NAME       = #{officetypeName}"
	              ,"A.DONGCODE              = #{dongcode}"
	              ,"A.DONGCODE_NAME         = #{dongcodeName}"
	              ,"A.ADDRTYPE              = #{addrtype}"
	              ,"A.ADDRTYPENAME          = #{addrtypename}"
	              ,"A.BUNJI                 = #{bunji}"
	              ,"A.HO                    = #{ho}"
	              ,"A.NOTE                  = #{note}"
	              ,"A.OFFICENICK            = #{officenick}"
	              ,"A.DETAILADDR            = #{detailaddr}"
	              ,"A.CREATIONDTIME         = #{creationdtime}"
	              ,"A.LASTMODIFICATIONDTIME = #{lastmodificationdtime}"
	              ,"A.LASTMODIFICATIONUSRID = #{lastmodificationusrid}"
	              ,"A.STATUS                = #{status}"
	              ,"A.ACTIVE                = #{active}"
	              ,"A.CHNG_DATE             = SYSDATE"
	              ,"A.EAI_SEQ               = TO_NUMBER(#{eaiSeq})")
		    .WHERE("A.OFFICECODE = #{officecode}")
		    .toString();
		    
	}
}
