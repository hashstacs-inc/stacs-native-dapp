package com.higgschain.trust.drs.model.policy;

import com.higgschain.trust.drs.enums.FunctionDefineEnum;
import com.higgschain.trust.drs.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;
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

    @Override public String getFunctionName() {
        return FunctionDefineEnum.REGISTER_POLICY.getFunctionName();
    }
}
