package io.stacs.nav.drs.api.model.contract;

import com.alipay.sofa.common.utils.StringUtil;
import io.stacs.nav.drs.api.enums.ApiConstants;
import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;

/**
 * The type Contract invoke v 2 request.
 *
 * @author kongyu
 * @date 2018 /12/10
 */
@Getter @Setter @ToString(callSuper = true) public class ContractInvokeVO extends BaseTxVO {

    /**
     * if transfer，which is transfering amount
     */
    private BigDecimal value;

    /**
     * 调用方法签名(方法名+参数列表+返回值，例如：(uint) balanceOf(address))
     */
    @NotBlank private String methodSignature;

    /**
     * 智能合约调用传入参数列表
     */
    private Object[] args;

    /**
     * tx create address
     */
    @NotBlank @Length(max = 40) private String from;

    /**
     * contract address
     */
    @NotBlank @Length(max = 40) private String to;
    /**
     * remark of contract
     */
    private String remark;

    @Override public String getMethodSign() {
        return ApiConstants.TransactionApiEnum.CONTRACT_INVOKER.getFunctionName();
    }

    @Override public String getFunctionName() {
        if (StringUtils.isEmpty(super.getFunctionName())) {
            return this.getMethodSign();
        }
        return super.getFunctionName();
    }

    @Override public String getSignValue() {
        return super.getSignValue() + methodSignature + from + to + StringUtil.join(args, ",") + getFunctionName();
    }
}
