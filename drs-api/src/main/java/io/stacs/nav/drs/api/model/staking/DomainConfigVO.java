package io.stacs.nav.drs.api.model.staking;

import com.alibaba.fastjson.JSON;
import io.stacs.nav.drs.api.enums.ApiConstants;
import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author tangkun
 * @date 2019-09-06
 */
@Getter @Setter public class DomainConfigVO extends BaseTxVO {
    @NotBlank private String domainId;

    @NotBlank private String stakingAddress;

    @NotBlank private String participationAddress;

    @Override public String getMethodSign() {
        return ApiConstants.TransactionApiEnum.STAKING_CONFIG_DOMAIN.getFunctionName();
    }

    @Override public String getFunctionName() {
        return ApiConstants.TransactionApiEnum.SYSTEM_PROPERTY.getFunctionName();
    }

    @Override public String getSignValue() {
        return super.getSignValue()
            + String.join("_", "DOMAIN_CONFIG", domainId)
            + JSON.toJSONString(new DomainConfig(domainId,stakingAddress,participationAddress))
            + getFunctionName();
    }
}
