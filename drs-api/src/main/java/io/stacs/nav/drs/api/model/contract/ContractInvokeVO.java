package io.stacs.nav.drs.api.model.contract;

import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;

/**
 * The type Contract invoke v 2 request.
 *
 * @author kongyu
 * @date 2018 /12/10
 */
@Getter @Setter public class ContractInvokeVO extends BaseTxVO {

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
     * BD functionName
     */
    @NotBlank private String functionName;

}
