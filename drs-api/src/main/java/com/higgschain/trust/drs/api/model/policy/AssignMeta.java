package com.higgschain.trust.drs.api.model.policy;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * @author ganxiang
 * @date 10/29/0029
 */
@Getter @Setter public class AssignMeta {
    /**
     * the number to verify
     */
    @Min(0) private int verifyNum;
    /**
     * domain-ids that must be verified
     */
    private List<String> mustDomainIds;

    /**
     * the expression for vote rule
     * example: n/2+1
     */
    private String expression;
}
