package io.stacs.nav.drs.api.model.action;

import io.stacs.nav.drs.api.enums.ActionTypeEnum;
import io.stacs.nav.drs.api.enums.CallbackTypeEnum;
import io.stacs.nav.drs.api.enums.DecisionTypeEnum;
import io.stacs.nav.drs.api.enums.VotePatternEnum;
import io.stacs.nav.drs.api.model.Action;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * The type Register policy.
 *
 * @author tangfashuang
 * @desc register policy action
 * @date 2018 -03-27
 */
@Getter @Setter public class RegisterPolicy extends Action {

    /**
     * policy id
     */
    @NotBlank @Length(max = 32) private String policyId;
    /**
     * policy name
     */
    @Length(max = 64) private String policyName;
    /**
     * domain ids of related to policy
     */
    private List<String> domainIds;
    /**
     * the decision type for vote ,1.FULL_VOTE,2.ONE_VOTE,3.ASSIGN_NUM
     */
    private DecisionTypeEnum decisionType;
    /**
     * rs vote pattern 1.SYNC 2.ASYNC
     */
    private VotePatternEnum votePattern;
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
        this.setType(ActionTypeEnum.REGISTER_POLICY);
        this.setIndex(0);
    }

}
