package io.stacs.nav.drs.api.model.fee;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author liuyu
 * @date 2019-09-05
 */
@Getter @Setter public class FeeTxRuleVO {
    /**
     * the policy-id of transaction
     */
    @NotNull private String policyId;
    /**
     * the amount of each charge
     */
    @NotNull private String amount;

}
