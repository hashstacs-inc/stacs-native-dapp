package io.stacs.nav.drs.api.model;

import io.stacs.nav.drs.api.enums.CallbackTypeEnum;
import io.stacs.nav.drs.api.enums.DecisionTypeEnum;
import io.stacs.nav.drs.api.enums.VotePatternEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * The type Policy.
 *
 * @author tangfashuang
 * @desc policy bo
 * @date 2018 -04-02
 */
@Setter @Getter public class Policy {

    /**
     * policy id
     */
    private String policyId;

    /**
     * policy name
     */
    private String policyName;

    /**
     * domain id of related to policy
     */
    private List<String> domainIds;
    /**
     * the decision type for vote ,1.FULL_VOTE,2.ONE_VOTE,3.ASSIGN_NUM
     */
    private DecisionTypeEnum decisionType;

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
    /**
     * version of policy,default 0
     */
    private int version;
}
