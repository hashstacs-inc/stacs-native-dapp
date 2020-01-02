package io.stacs.nav.drs.api.model.policy;


import io.stacs.nav.drs.api.enums.ApiConstants;
import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * The type Register policy vo.
 *
 * @author tangfashuang
 * @date 2018 /05/18 14:13
 * @desc
 */
@Getter @Setter public class RegisterPolicyVO extends BaseTxVO {

    /**
     * policy id
     */
    @NotBlank @Length(max = 32) private String policyId;

    /**
     * policy name
     */
    @NotBlank @Length(max = 64) private String policyName;

    /**
     * rs vote pattern 1.SYNC 2.ASYNC
     */
    @NotNull private String votePattern;

    /**
     * callback type of slave 1.ALL 2.SELF
     */
    @NotNull private String callbackType;

    /**
     * 1. FULL_VOTE 2. ONE_VOTE 3. ASSIGN_NUM
     */
    @NotNull private String decisionType;

    /**
     * domain ids of related to policy
     */
    @NotEmpty private List<String> domainIds;

    private AssignMeta assignMeta;
    /**
     * An authorized rs_ids collection is required for modification
     */
    private List<String> requireAuthIds;

    @Override public String getMethodSign() {
        return ApiConstants.TransactionApiEnum.REGISTER_POLICY.getFunctionName();
    }

    @Override public String getFunctionName() {
        if (StringUtils.isEmpty(super.getFunctionName())) {
            return this.getMethodSign();
        }
        return super.getFunctionName();
    }
    @Override public String getSignValue() {
        return super.getSignValue()
            + policyId
            + policyName
            + votePattern
            + (assignMeta != null ? assignMeta.getSignValue() : "")
            + String.join(",", domainIds)
            + String.join(",", requireAuthIds)
            + getFunctionName();
    }
}
