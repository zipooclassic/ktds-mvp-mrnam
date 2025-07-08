/**************************************************************************************
 * ICIS version 1.0
 *
 *  Copyright ⓒ 2023 kt/ktds corp. All rights reserved.
 *
 *  This is a proprietary software of kt corp, and you may not use this file except in
 *  compliance with license agreement with kt corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of kt corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 *************************************************************************************/

package com.kt.icis.oder.baseinfo.csnl.command.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kt.icis.oder.baseinfo.common.command.repository.cboc.CbocustagentCmdRepo;
import com.kt.icis.oder.baseinfo.common.query.repository.cboc.BiCbocustagentQryRepo;
import com.kt.icis.oder.baseinfo.common.query.repository.dto.ccst.BiCcstcustagentQryDto;
import com.kt.icis.oder.baseinfo.csnl.command.repository.Csnl400dCmdRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Csnl400dCmdSvc {

    private final Csnl400dCmdRepo csnl400dCmdRepo;

    private final BiCbocustagentQryRepo biCbocustagentQryRepo;
    private final CbocustagentCmdRepo cbocustagentCmdRepo;

    public List<BiCcstcustagentQryDto> reader() {
        return csnl400dCmdRepo.reader();
    }

    public int countByIcisCustIdAndAgntSeq(String icisCustId, String agntSeq) {
        return biCbocustagentQryRepo.countByIcisCustIdAndAgntSeq(icisCustId, agntSeq);
    }

    public void deleteByIcisCustIdAndAgntSeq(String icisCustId, String agntSeq) {
        cbocustagentCmdRepo.deleteByIcisCustIdAndAgntSeq(icisCustId, agntSeq);
    }

    public void insertData(BiCcstcustagentQryDto dto) {
        cbocustagentCmdRepo.insertData(dto);
    }

    public void updateData(BiCcstcustagentQryDto dto) {
    	cbocustagentCmdRepo.updateData(dto);
    }

    public int writer(BiCcstcustagentQryDto btCcstcustagentDto) {
        return csnl400dCmdRepo.writer(btCcstcustagentDto);
    }

}
