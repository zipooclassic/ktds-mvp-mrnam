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

import com.kt.icis.oder.baseinfo.common.repository.BtCcpiprodhierRepo;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCciaCustDivdCsnh627dDto;

import lombok.RequiredArgsConstructor;

/**
 * BtCcpiprodhierSvc:
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
public class BtCcpiprodhierSvc {

    private final BtCcpiprodhierRepo repository;

    public List<BtCciaCustDivdCsnh627dDto> findByCcpiprodhierJoin() {

        List<BtCciaCustDivdCsnh627dDto> outDto = repository.findByCcpiprodhierJoin();
        return outDto;
    }

}
