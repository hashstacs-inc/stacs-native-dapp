package io.stacs.nav.drs.api.solidity;

/**
 * @author Su Jiulong
 * @date 2019/5/30
 */
public class EvmSignArgs {
    /**
     * Solidity  contract data type
     */
    private SolidityTypeEnum paramType;
    /**
     * The value corresponding to the contract type
     */
    private Object paramValue;

    public EvmSignArgs() {
    }

    public EvmSignArgs(SolidityTypeEnum paramType, Object paramValue) {
        this.paramType = paramType;
        this.paramValue = paramValue;
    }

    public SolidityTypeEnum getParamType() {
        return paramType;
    }

    public void setParamType(SolidityTypeEnum paramType) {
        this.paramType = paramType;
    }

    public Object getParamValue() {
        return paramValue;
    }

    public void setParamValue(Object paramValue) {
        this.paramValue = paramValue;
    }
}
