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

package com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CustPartiTrtInCmdDto {

	private String saId;              
	private String befCustId;         
	private String aftCustId;         
	private String regOfcCd;          
	private String regerEmpNo;        
	private String regerEmpName;      
	private String reqerName;         // 신청자명
	private String reqerRelation;     // 신청자관계
	private String reqerNo;           // 신청자주민번호
	private String reqerTelNo;        // 신청자연락번호
	private String reqerTelNo1;       // 신청자연락번호1
	
	
	// Callback Sttus처리를 위한 input param 설정
	private String occurDate;
}
