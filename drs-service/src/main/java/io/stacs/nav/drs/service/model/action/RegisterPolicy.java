package io.stacs.nav.drs.service.model.action;

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

import static io.stacs.nav.slave.api.enums.ActionTypeEnum.REGISTER_POLICY;

/**
 * The type Register policy.
 *
 * @author tangfashuang
 * @desc register policy action
 * @date 2018 -03-27
 */
@ActionJsonDeserialize(types = REGISTER_POLICY) @Getter @Setter public class RegisterPolicy extends Action {

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
     * An authorized domain-ids collection is required for modification
     */
    private List<String> requireAuthIds;

    public RegisterPolicy() {
        this.setType(REGISTER_POLICY);
        this.setIndex(0);
    }

}
