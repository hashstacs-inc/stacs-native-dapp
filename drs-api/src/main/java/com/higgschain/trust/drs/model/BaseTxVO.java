package com.higgschain.trust.drs.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 基本交易类型VO 包含交易VO的公共字段
 *
 * @author ganxiang
 * @date 10/29/2019
 */
@Getter @Setter public abstract class BaseTxVO {

    private String txId;
    /**
     * tx submitter address
     */
    private String submitter;

    private String execPolicyId;

    private String submitterSign;
    /**
     * Currency for handling fees
     */
    private String feeCurrency;

    private String feeMaxAmount;

    private String bdCode = "SystemBD";

    public abstract String getFunctionName();
}
