package io.stacs.nav.drs.service.model.action;

import io.stacs.nav.slave.api.enums.ActionTypeEnum;
import io.stacs.nav.slave.api.enums.manage.CallbackTypeEnum;
import io.stacs.nav.slave.api.enums.manage.DecisionTypeEnum;
import io.stacs.nav.slave.api.enums.manage.VotePatternEnum;
import io.stacs.nav.slave.model.bo.action.ActionJsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * modify policy.
 *
 * @author liuyu
 * @desc modify policy action
 * @date 2019-06-24
 */
@ActionJsonDeserialize(types = ActionTypeEnum.MODIFY_POLICY) @Getter @Setter public class ModifyPolicyAction
    extends Action {

    /**
     * policy id
     */
    @NotBlank @Length(max = 32) private String policyId;
    /**
     * policy name
     */
    @NotBlank @Length(max = 64) private String policyName;
    /**
     * domain ids of related to policy
     */
    @NotEmpty private List<String> domainIds;
    /**
     * the decision type for vote ,1.FULL_VOTE,2.ONE_VOTE,3.ASSIGN_NUM
     */
    @NotNull private DecisionTypeEnum decisionType;
    /**
     * rs vote pattern 1.SYNC 2.ASYNC
     */
    @NotNull private VotePatternEnum votePattern;
    /**
     * callback type of slave 1.ALL 2.SELF
     */
    @NotNull private CallbackTypeEnum callbackType;
    /**
     * the number to verify
     */
    private int verifyNum;
    /**
     * domain-ids that must be verified
     */
    private List<String> mustDomainIds;
    /**
     * the expression for vote rule
     * example: n/2+1
     */
    private String expression;
    /**
     * An authorized domain_ids collection is required for modification
     */
    private List<String> requireAuthIds;

    public ModifyPolicyAction() {
        this.setType(ActionTypeEnum.MODIFY_POLICY);
        this.setIndex(0);
    }
}
