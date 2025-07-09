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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Csnh626dToCcmc122sInCmdDto {

    private String custProcType;
    private String custId;
    private String occurDate;
    private String saId;
    private String saCd;
    private String exptYn;
    private String regerEmpNo;
    private String regerEmpName;
    private String regOfcCd;
}
//*      [I] vcCustProcType      STRING  1       -- 정보변경구분 (C : 고객식별정보변경, I : 고객통합)
//*      [I] vcCustId            STRING  11      -- 고객ID (고객식별정보변경 : 변경대상 고객ID, 고객통합 : 삭제할 고객ID)
//*      [I] vcOccurDate         STRING  14      -- CIA에서 정보변경이 발생한 일시
//*      [I] vcSaId              STRING  11      -- 서비스계약ID. (NULL인 경우, 개별처리 없이 후행서비스 호출)
//*      [I] vcSaCd              STRING  4       -- 상품코드
//*      [I] vcExptYn            STRING  1       -- 처리제외 여부 (Y : 처리제외)
//*      [I] vcRegerEmpNo        STRING  9       -- 등록자사번
//*      [I] vcRegerEmpName      STRING  10      -- 등록자명
//*      [I] vcRegOfcCd          STRING  6       -- 등록국