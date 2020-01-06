package io.stacs.nav.drs.api.model.staking;

import io.stacs.nav.drs.api.enums.ApiConstants;
import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author liuyu
 * @date 2020-01-06
 */
@Setter @Getter public class StakingCurrencyConfigVO extends BaseTxVO {
    @NotBlank private String stakingCurrency;

    @Override public String getMethodSign() {
        return ApiConstants.TransactionApiEnum.STAKING_CONFIG_CURRENCY.getFunctionName();
    }

    @Override public String getFunctionName() {
        return ApiConstants.TransactionApiEnum.SYSTEM_PROPERTY.getFunctionName();
    }

    @Override public String getSignValue() {
        return super.getSignValue() + "STAKING_CURRENCY_KEY" + stakingCurrency + getFunctionName();
    }
}
