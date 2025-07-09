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

package com.kt.icis.oder.baseinfo.common.repository.entity;

import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WC_CWKOERPEMPINFO db table entity
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Table("WC_CWKOERPEMPINFO")
public class BtCwkoerpempinfoEntt implements Persistable<String> {

    // description = "ACTV_YN", nullable = true, maxLength = 1
    @Column("ACTV_YN")
    private String actvYn;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public boolean isNew() {
        return false;
    }

}