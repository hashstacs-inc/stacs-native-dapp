/*
 * Copyright (c) 2013-2017, suimi
 */
package io.stacs.nav.drs.api.model.snapshot;

import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

import static io.stacs.nav.drs.api.enums.FunctionDefineEnum.BUILD_SNAPSHOT;

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
