package io.stacs.nav.drs.api.model.contract;

import io.stacs.nav.drs.api.enums.FunctionDefineEnum;
import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Contract create request.
 *
 * @author duhongming
 * @date 2018 /6/24
 */
@Getter @Setter public class ContractCreateVO extends BaseTxVO {

    private String fromAddr;
    /**
     * 合约地址
     */
    private String contractAddress;

    private String name;

    private String extension;
    /**
     * 合约构造器
     */
    private String contractor;

    /**
     * 合约代码
     */
    private String sourceCode;
    /**
     * 合约构造入参
     */
    private Object[] initArgs;

    @Override public String getFunctionName() {
        return FunctionDefineEnum.CREATE_CONTRACT.getFunctionName();
    }
}
