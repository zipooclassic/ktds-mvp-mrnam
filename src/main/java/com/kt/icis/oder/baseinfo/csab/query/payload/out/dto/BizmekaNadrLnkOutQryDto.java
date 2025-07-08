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

package com.kt.icis.oder.baseinfo.csab.query.payload.out.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class BizmekaNadrLnkOutQryDto {
	private String dongCd;
	private String addrNoType;
	private String addrNo;
	private String addrHo;
	private String addrText;
	private String legDongCd;
	private String arnoId;
	private String roadId;
	private String roadNm;
	private String roadFullNm;
	private String raddrNo;
	private String raddrHo;
	private String rbldgId;
	private String rbldgNmId;
	private String rbldgName;
	private String rbldgText;
}
