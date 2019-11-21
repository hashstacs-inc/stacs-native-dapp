/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.drs.api.model.snapshot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * @author suimi
 * @date 2019/1/7
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor public class SnapshotVO {
    @NotBlank @Size(max = 64) private String snapshotId;
    private Long blockHeight;

}
