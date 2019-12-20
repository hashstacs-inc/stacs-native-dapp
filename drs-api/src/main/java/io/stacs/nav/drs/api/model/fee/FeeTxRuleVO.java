package io.stacs.nav.drs.api.model.fee;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author liuyu
 * @date 2019-09-05
 */
@Getter @Setter @ToString(callSuper = true) public class FeeTxRuleVO {
    /**
     * the policy-id of transaction
     */
    @NotNull private String policyId;
    /**
     * the amount of each charge
     */
    @NotNull private String amount;

    public String getSignValue(){
        return policyId + amount;
    }
}
