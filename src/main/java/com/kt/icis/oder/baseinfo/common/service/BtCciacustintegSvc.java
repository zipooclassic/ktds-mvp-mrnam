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
package com.kt.icis.oder.baseinfo.common.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kt.icis.cmmnfrwk.utils.LogUtil;
import com.kt.icis.oder.baseinfo.common.repository.BtCciaCustIntegRepo;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciacustintegAndCciacustchanginfoDto;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciacustintegCsnh625dDto;

import lombok.RequiredArgsConstructor;

/**
 * @Class Name : BtCciacustintegSvc
 * @Class 설명 : Cciacustinteg CRUD
 * @작성일 : 2023. 10. 12
 * @작성자 : 미소시스템 / 91343659 / 심은섭
 */
@Service
@RequiredArgsConstructor
public class BtCciacustintegSvc {

    private final BtCciaCustIntegRepo repository;


    public List<BtCciacustintegAndCciacustchanginfoDto> getCciacustintegAndCciacustChanginfo() {
        List<BtCciacustintegAndCciacustchanginfoDto> dto =
                repository.getCciacustintegAndCciacustChanginfo();
        return dto;

    }


    /**
     * @Method Name : getInfDeclareCursorCiaWork
     * @Method 설명 : CIA 변동 데이터 조회
     * @작성일 : 2023. 10. 11
     * @작성자 : 미소시스템 / 91343659 / 심은섭
     * @param
     * @return
     * 		List<BtCciacustintegCsnh625dDto>
     */
    public List<BtCciacustintegCsnh625dDto> getInfDeclareCursorCiaWork() {
        return repository.getInfDeclareCursorCiaWork();
    }


    /**
     * @Method Name : getInfIsPendingCust
     * @Method 설명 : 다른 변동에서 통합될 고객ID로 사용되는지 확인
     * @작성일 : 2023. 10. 11
     * @작성자 : 미소시스템 / 91343659 / 심은섭
     * @param
     * @return
     * 		INT
     */
    public int getInfIsPendingCust(BtCciacustintegCsnh625dDto dto) {
        return repository.getInfIsPendingCust(dto);
    }


    /**
     * ICIS(CCIA_CUST_INTEG)에 처리 결과 값을 갱신
     * @param dto
     * @return
     */
    public int infUpdateCiaWork(BtCciacustintegCsnh625dDto dto) {
        LogUtil.info("BtCciacustintegSvc : call{}", dto.toString());
        return repository.updateCiaWork(dto);
    }


}
