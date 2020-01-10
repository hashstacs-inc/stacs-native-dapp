package io.stacs.nav.drs.api.model.task;

import io.stacs.nav.drs.api.enums.ApiConstants;
import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;

/**
 * @author liuyu
 * @description
 * @date 2019-09-09
 */
@Getter @Setter public class TaskConfigVO extends BaseTxVO {
    /**
     * the task type
     */
    @NotNull private String type;
    /**
     * cron expression of task
     */
    @NotNull private String cronExpression;

    @Override public String getFeeCurrency() {
        if (StringUtils.isEmpty(super.getFeeCurrency())) {
            return "N/A";
        }
        return super.getFeeCurrency();
    }

    @Override public String getFeeMaxAmount() {
        if (StringUtils.isEmpty(super.getFeeMaxAmount())) {
            return "0";
        }
        return super.getFeeMaxAmount();
    }

    @Override public String getMethodSign() {
        return ApiConstants.TransactionApiEnum.TASK_CONFIG.getFunctionName();
    }

    @Override public String getFunctionName() {
        if (StringUtils.isEmpty(super.getFunctionName())) {
            return this.getMethodSign();
        }
        return super.getFunctionName();
    }

    @Override public String getSignValue() {
        return super.getSignValue() + type + cronExpression + getFunctionName();
    }
}
