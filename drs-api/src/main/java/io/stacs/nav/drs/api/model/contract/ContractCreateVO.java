package io.stacs.nav.drs.api.model.contract;

import io.stacs.nav.crypto.StacsECKey;
import io.stacs.nav.drs.api.enums.ApiConstants;
import io.stacs.nav.drs.api.model.BaseTxVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * The type Contract create request.
 *
 * @author duhongming
 * @date 2018 /6/24
 */
@Getter @Setter @ToString(callSuper = true) public class ContractCreateVO extends BaseTxVO {

    private String fromAddr;
    /**
     * contract address
     */
    private String contractAddress;

    private String name;

    private String extension;
    /**
     * constructor
     */
    private String contractor;
    /**
     *
     */
    private String symbol;
    /**
     * source code of contract
     */
    private String sourceCode;
    /**
     * args
     */
    private Object[] initArgs;

    public String getContractAddress() {
        if(StringUtils.isEmpty(contractAddress)){
            contractAddress = new StacsECKey().getHexAddress();
        }
        return contractAddress;
    }

    @Override public String getMethodSign() {
        return ApiConstants.TransactionApiEnum.CREATE_CONTRACT.getFunctionName();
    }

    @Override public String getFunctionName() {
        if (StringUtils.isEmpty(super.getFunctionName())) {
            return this.getMethodSign();
        }
        return super.getFunctionName();
    }
    /**
     * get sign value
     *
     * @return
     */
    @Override
    public String getSignValue(){
        return super.getSignValue()
                    + fromAddr
                    + getContractAddress()
                    + name
                    + symbol
                    + extension
                    + getFunctionName();
    }
}
