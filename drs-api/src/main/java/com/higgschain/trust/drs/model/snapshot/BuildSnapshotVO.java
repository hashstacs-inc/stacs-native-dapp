/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.drs.model.snapshot;

import com.higgschain.trust.drs.model.BaseTxVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

import static com.higgschain.trust.drs.enums.FunctionDefineEnum.BUILD_SNAPSHOT;

/**
 * @author suimi
 * @date 2019/1/7
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor public class BuildSnapshotVO extends BaseTxVO {
    @NotBlank @Size(max = 64) private String snapshotId;

    @Override public String getFunctionName() {
        return BUILD_SNAPSHOT.getFunctionName();
    }
}
