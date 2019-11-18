package com.higgschain.trust.drs.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 基本交易类型VO 包含交易VO的公共字段
 *
 * @author ganxiang
 * @date 10/29/0029
 */
@Getter @Setter public abstract class BaseTxVO {

    @NotBlank @Length(max = 64) private String txId;
    /**
     * tx submitter address
     */
    @NotBlank @Length(max = 40) private String submitter;

    @NotBlank @Length(max = 32) private String execPolicyId;

    @NotBlank @Length(max = 64) private String submitterSign;
    /**
     * Currency for handling fees
     */
    @Length(max = 32) private String feeCurrency;

    @Length(max = 18) private String feeMaxAmount;

    public abstract String getFunctionName();
}
