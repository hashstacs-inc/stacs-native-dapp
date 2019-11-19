package com.higgschain.trust.drs.model;

import com.higgschain.trust.drs.enums.FunctionDefineEnum;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * The type Contract create request.
 *
 * @author duhongming
 * @date 2018 /6/24
 */
@Getter @Setter public class ContractCreateVO extends BaseTxVO {

    @NotBlank @Length(max = 40)
    private String fromAddr;
    /**
     * 合约地址
     */
    @NotBlank @Length(max = 40)
    private String contractAddress;

    @NotBlank
    @Length(max =64)
    private String name;

    @Length(max =1024)
    private String extension;
    /**
     * 合约构造器
     */
    @NotBlank
    private String contractor;

    /**
     * 合约代码
     */
    @NotBlank
    private String sourceCode;

    /**
     * bdCode
     */
    @NotBlank @Length(max = 64)
    private String bdCode;
    /**
     * 合约构造入参
     */
    private Object[] initArgs;

    @Override public String getFunctionName() {
        return FunctionDefineEnum.CREATE_CONTRACT.getFunctionName();
    }
}
