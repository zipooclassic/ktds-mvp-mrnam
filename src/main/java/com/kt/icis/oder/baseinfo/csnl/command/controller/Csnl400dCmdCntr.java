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

package com.kt.icis.oder.baseinfo.csnl.command.controller;

import java.util.List;

import org.springframework.stereotype.Component;

import com.kt.icis.cmmnfrwk.utils.LogUtil;
import com.kt.icis.oder.baseinfo.common.query.repository.dto.ccst.BiCcstcustagentQryDto;
import com.kt.icis.oder.baseinfo.csnl.command.service.Csnl400dCmdSvc;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Csnl400dCmdCntr {

    private final Csnl400dCmdSvc csnl400dCmdSvc;

    public void batch() {
        List<BiCcstcustagentQryDto> btCcstcustagentDtoList = csnl400dCmdSvc.reader();
        
        for(BiCcstcustagentQryDto btCcstcustagentDto : btCcstcustagentDtoList) {
        	csnl400dCmdSvc.writer(process(btCcstcustagentDto));
        }
    }

    private BiCcstcustagentQryDto process(BiCcstcustagentQryDto btCcstcustagentDto) {
        LogUtil.info("doProcess:chkDataValidation DATA 검증 ..... \\n");
        
        if (btCcstcustagentDto.getIcisCustId() == null || btCcstcustagentDto.getIcisCustId().length()<1) {
            btCcstcustagentDto.setWoErrMsg("ICIS CUST_ID 오류 ["+btCcstcustagentDto.getIcisCustId()==null?"":btCcstcustagentDto.getIcisCustId()+"]");
            LogUtil.info("chkDataValidation : ICIS CUST_ID 오류 [{}]",btCcstcustagentDto.getIcisCustId()==null?"":btCcstcustagentDto.getIcisCustId());
            LogUtil.info("doProcess:chkDataValidation 오류:작업 테이블 업데이트 \\n");
            updateCbocust(btCcstcustagentDto);
            return btCcstcustagentDto;
        }
        
        LogUtil.info("doProcess:setCustAgentInfo AGENT 정보 현행화 처리 ..... \n");
        LogUtil.info("setCustAgentInfo_NS:ICIS_CUST_ID [{}]", btCcstcustagentDto.getIcisCustId());
        LogUtil.info("setCustAgentInfo_NS:EAI_SEQ [{}]", btCcstcustagentDto.getEaiSeq());
        LogUtil.info("setCustAgentInfo_RS:AGNT_SEQ [{}]", btCcstcustagentDto.getAgntSeq()==null?"":btCcstcustagentDto.getAgntSeq());
        int nProc = csnl400dCmdSvc.countByIcisCustIdAndAgntSeq(btCcstcustagentDto.getIcisCustId(), btCcstcustagentDto.getAgntSeq());
        if ("D".equals(btCcstcustagentDto.getEaiTrtDivCd())) {
            if (nProc<1) {
                LogUtil.info("삭제할 데이터가 없습니다.");
                updateCbocust(btCcstcustagentDto);
                return btCcstcustagentDto;
            } else {
            	csnl400dCmdSvc.deleteByIcisCustIdAndAgntSeq(btCcstcustagentDto.getIcisCustId(), btCcstcustagentDto.getAgntSeq());
            }
        } else if ("I".equals(btCcstcustagentDto.getEaiTrtDivCd()) || "U".equals(btCcstcustagentDto.getEaiTrtDivCd())) {
            if (nProc<1) {
                LogUtil.info("INSERT: Start ......... \\n");
                try {
                	csnl400dCmdSvc.insertData(btCcstcustagentDto);
                } catch(Exception e) {
                    btCcstcustagentDto.setWoErrMsg("CBOCUSTAGENT 입력 오류");
                    btCcstcustagentDto.setWoFlag("N");
                    updateCbocust(btCcstcustagentDto);
                    return btCcstcustagentDto;
                }
                LogUtil.info("INSERT: End ......... \\n");
            } else {
                LogUtil.info("UPDATE: Start ......... \\n");
                csnl400dCmdSvc.updateData(btCcstcustagentDto);
                LogUtil.info("UPDATE: End ......... \\n");
            }
        } else {
            LogUtil.info("처리 유형 오류 입니다.");
            updateCbocust(btCcstcustagentDto);
            return btCcstcustagentDto;
        }
        updateCbocust(btCcstcustagentDto);
        return btCcstcustagentDto;
    }

    private void updateCbocust(BiCcstcustagentQryDto btCcstcustagentDto) {
        if (btCcstcustagentDto.getWoFlag()==null || "".equals(btCcstcustagentDto.getWoFlag())) {
            btCcstcustagentDto.setWoFlag("1");
        }
        LogUtil.info("====  수신 작업테이블 UPDATE DATA ------- \n");
        LogUtil.info("=vcWO_FLAG       [{}]\\n", btCcstcustagentDto.getWoFlag());
        LogUtil.info("=vcWO_ERR_MSG    [{}]\\n", btCcstcustagentDto.getWoErrMsg()==null?"":btCcstcustagentDto.getWoErrMsg());
    }

}
