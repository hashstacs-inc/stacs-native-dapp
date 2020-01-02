package io.stacs.nav.drs.api.model.fee;

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
@Getter @Setter public class FeeConfigVO extends BaseTxVO {
    /**
     * currency
     */
    @NotNull private String currency;
    /**
     * the received address of fee
     */
    @NotNull private String receiveAddr;

    @Override public String getMethodSign() {
        return ApiConstants.TransactionApiEnum.SET_FEE_CONFIG.getFunctionName();
    }

    @Override public String getFunctionName() {
        if (StringUtils.isEmpty(super.getFunctionName())) {
            return this.getMethodSign();
        }
        return super.getFunctionName();
    }

    @Override public String getSignValue() {
        return super.getSignValue() + currency + receiveAddr + getFunctionName();
    }
}
