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

import com.kt.icis.oder.baseinfo.common.repository.BtCciaCustDivdRepo;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciaCustDivdCsnh627dDto;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciacustdivdCsnh622dDto;

import lombok.RequiredArgsConstructor;

/**
 * BtCciaCustDivdSvc:
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
public class BtCciaCustDivdSvc {

    private final BtCciaCustDivdRepo repository;

    /**
     * @return
     */
    public List<BtCciaCustDivdCsnh627dDto> infDeclareCursorIcisWork() {
        List<BtCciaCustDivdCsnh627dDto> outDto = repository.infDeclareCursorIcisWork();
        return outDto;
    }


    /**
     * ICIS(CCIA_CUST_DIVD)에 처리 결과 값을 갱신
     * @param writerDto
     * @return
     */
    public int updateIcisWork(BtCciaCustDivdCsnh627dDto writerDto) {
        int updateCnt = repository.updateIcisWork(writerDto);
        return updateCnt;
    }

    public List<BtCciacustdivdCsnh622dDto> findByCciacustdivdAndccpiprodhierAndccstbasicinfo(){
        List<BtCciacustdivdCsnh622dDto> list = repository.findByCciacustdivdAndccpiprodhierAndccstbasicinfo();
        return list;
    }


    public void updateBySaidCustidOccurdateNewcustid(BtCciacustdivdCsnh622dDto dto) {
        repository.updateBySaidCustidOccurdateNewcustid(dto);

    }
}
