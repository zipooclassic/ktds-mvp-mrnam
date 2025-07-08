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
package com.kt.icis.oder.baseinfo.csnh.command.controller.client.payload.out.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Csnh626dToCcmc122sOutCmdDto {

    private String vcCustProcType;
    private String vcCustId;
    private String vcOccurDate;
    private String vcSaId;
    private String vcSaCd;

    private String vcRegDate;
    private String vcRegModuleId;
    private String vcOrdNo;
    private String vcStatusCd;
    private String vcResultCd;

    private String vcResultMsg;
    private String vcProcDate;
    private BigDecimal lRetryCnt;
    private String vcProcResvDate;
    private String vcCombResultCd;
    private String vcCombResultMsg;
    private String vcCombProcDate;
    private String vcCombSaId;
}
//*      [O] vcCustProcType      STRING  1       -- 처리구분
//*      [O] vcCustId            STRING  11      -- 고객ID
//*      [O] vcOccurDate         STRING  14      -- 발생일시
//*      [O] vcSaId              STRING  11      -- 서비스계약ID
//*      [O] vcSaCd              STRING  4       -- 상품코드
//*      [O] vcRegDate           STRING  14      -- 등록일시
//*      [O] vcRegModuleId       STRING  20      -- 대상편성 모듈ID
//*      [O] vcOrdNo             STRING  15      -- 오더번호
//*      [O] vcStatusCd          STRING  1       -- 처리상태코드
//*      [O] vcResultCd          STRING  4       -- 처리결과코드

//*      [O] vcResultMsg         STRING  1000    -- 처리결과 메시지
//*      [O] vcProcDate          STRING  14      -- 처리일시
//*      [O] lRetryCnt           LONG            -- 재처리 시도 횟수
//*      [O] vcProcResvDate      STRING  14      -- 처리예정일시
//*      [O] vcCombResultCd      STRING  4       -- 결합처리결과코드
//*      [O] vcCombResultMsg     STRING  100     -- 결합처리결과 메시지
//*      [O] vcCombProcDate      STRING  14      -- 결합처리일시
//*      [O] vcCombSaId          STRING  11      -- 결합계약ID