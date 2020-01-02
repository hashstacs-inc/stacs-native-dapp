/*
 * Copyright (c) 2013-2017, suimi
 */
package io.stacs.nav.drs.api.model.snapshot;

import io.stacs.nav.drs.api.enums.ApiConstants;
import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * @author suimi
 * @date 2019/1/7
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor public class BuildSnapshotVO extends BaseTxVO {
    @NotBlank @Size(max = 64) private String snapshotId;

    @Override public String getMethodSign() {
        return ApiConstants.TransactionApiEnum.BUILD_SNAPSHOT.getFunctionName();
    }

    @Override public String getFunctionName() {
        if (StringUtils.isEmpty(super.getFunctionName())) {
            return this.getMethodSign();
        }
        return super.getFunctionName();
    }

    @Override public String getSignValue(){
        return super.getSignValue()
                        + snapshotId
                        + getFunctionName();
    }
}
