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

import com.kt.icis.oder.baseinfo.common.repository.BtCciaCustChangInfoRepo;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciaCustChangInfoCsnh624dDto;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciacustintegCsnh624dDto;

import lombok.RequiredArgsConstructor;

/**
 * BtCciaCustChangInfoSvc:
 * <ul>
 * <li></li>
 * </ul>
 *
 * created by eunsub on 2023.09.05.
 *
 * @author eunsub
 */
@Service
@RequiredArgsConstructor
public class BtCciaCustChangInfoSvc {

    private final BtCciaCustChangInfoRepo repository;


    /**
     * ICIS(CCIA_CUST_INTEG)에 처리 결과 값을 갱신
     * @param dto
     * @return
     */
    public int infCUpdateCiaWork(BtCciaCustChangInfoCsnh624dDto dto) {
        return repository.infCUpdateCiaWork(dto);
    }

    /**
     * CIA 변동 데이터 조회
     */
    public List<BtCciacustintegCsnh624dDto> getInfCDeclareCursorCiaWork() {
        return repository.getInfCDeclareCursorCiaWork();
    }

}
