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

import com.kt.icis.oder.baseinfo.common.repository.BtCwkoCiaSaTargInfoRepo;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCwkociasatarginfoCsnh624dDto;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCwkociasatarginfoCsnh625dDto;
import com.kt.icis.oder.baseinfo.common.repository.dto.BtCwkociasatarginfoCsnj626Dto;
import com.kt.icis.oder.baseinfo.common.repository.entity.BtCwkoCiaSaTargInfoEntt;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BtCwkociasatarginfoSvc {

    private final BtCwkoCiaSaTargInfoRepo repository;



    public int getCount(String custProcType, String custId, String occurDate) {
        int cnt = repository.getCount( custProcType,  custId,  occurDate);
        return cnt;
    }



    public List<BtCwkociasatarginfoCsnj626Dto> getSaIdSaCd(String custProcType, String custId, String occurDate) {
        return repository.getSaIdSaCd(custProcType, custId, occurDate);
    }


    public List<BtCwkoCiaSaTargInfoEntt> selectInfInsertSaWorkForInfo(
            BtCwkociasatarginfoCsnh625dDto btCwkociasatarginfoCsnh625dDto) {
        return repository.selectInfInsertSaWorkForInfo(btCwkociasatarginfoCsnh625dDto);
    }



    public void insertInfInsertSaWorkForInfo(List<BtCwkoCiaSaTargInfoEntt> btCwkoCiaSaTargInfoEnttList) {
        repository.saveAll(btCwkoCiaSaTargInfoEnttList);

    }
    public void save(BtCwkoCiaSaTargInfoEntt entt) {
        repository.save(entt);

    }



    public List<BtCwkoCiaSaTargInfoEntt> selectInfCInsertSaWorkForInfo(
            BtCwkociasatarginfoCsnh624dDto btCwkociasatarginfoCsnh624dDto) {
        return repository.selectInfCInsertSaWorkForInfo(btCwkociasatarginfoCsnh624dDto);
    }



    public void insertInfCInsertSaWorkForInfo(List<BtCwkoCiaSaTargInfoEntt> btCwkoCiaSaTargInfoEnttList) {
        repository.saveAll(btCwkoCiaSaTargInfoEnttList);

    }
    
    public int getUpdatedCount() {
        int cnt = repository.selectStatusCdAndProcDateCount();
        return cnt;
    }

}
