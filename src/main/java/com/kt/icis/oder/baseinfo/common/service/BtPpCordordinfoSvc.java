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

import org.springframework.stereotype.Service;

import com.kt.icis.oder.baseinfo.common.repository.BtPpCordordinfoRepo;

import lombok.RequiredArgsConstructor;

/**
 * service
 */
@Service
@RequiredArgsConstructor
public class BtPpCordordinfoSvc {

    private final BtPpCordordinfoRepo btPpCordordinfoRepo;


    public String checkOnGoingOrdNo(String saId) {
        String ordNo = btPpCordordinfoRepo.checkOnGoingOrdNo(saId);
        return ordNo;
    }

}
