package io.stacs.nav.drs.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 基本交易类型VO 包含交易VO的公共字段
 *
 * @author ganxiang
 * @date 10/29/2019
 */
@Getter @Setter @ToString public abstract class BaseTxVO {

    @NotBlank @Length(max = 64) private String txId;

    @NotBlank @Length(max = 32) private String bdCode;
    /**
     * tx submitter address
     */
    @NotBlank @Length(max = 40) private String submitter;

    private String submitterSign;

    private String execPolicyId;
    /**
     * Currency for handling fees
     */
    private String feeCurrency;

    private String feeMaxAmount;

    public abstract String getFunctionName();
    /**
     * get sign value
     *
     * @return
     */
    public String getSignValue(){
        return txId + bdCode + execPolicyId + feeCurrency + feeMaxAmount;
    }
}
