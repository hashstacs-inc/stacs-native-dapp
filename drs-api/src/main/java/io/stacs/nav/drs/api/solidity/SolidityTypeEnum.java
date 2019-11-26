package io.stacs.nav.drs.api.solidity;

/**
 * @author Su Jiulong
 * @date 2019/5/30
 */
public enum SolidityTypeEnum {
    /**
     * solidity合约地址：对应于以太坊地址
     */
    ADDRESS("address"),
    /**
     * solidity合约地址数组：对应于java的List<String>
     */
    ADDRESS_ARR("address[]"),
    /**
     * solidity合约无符号整型：java基本类型或Number及其子类
     */
    UINT("uint"),
    /**
     * solidity合约无符号整型：java基本类型或Number及其子类
     */
    UINT256("uint256"),
    /**
     * solidity合约无符号整型数组: List
     */
    UINT_ARR("uint[]"),
    /**
     * solidity合约无符号整型数组: List
     */
    UINT256_ARR("uint256[]"),
    /**
     * solidity合约整型：java基本类型或Number及其子类
     */
    INT("int"),
    /**
     * solidity合约整型：List
     */
    INT_ARR("int[]"),
    /**
     * solidity合约boolean类型
     */
    BOOL("bool"),
    /**
     * solidity合约boolean数组
     */
    BOOL_ARR("bool[]"),
    /**
     * solidity字符串类型：对应于java的String
     */
    STRING("string"),
    /**
     * solidity byte[]
     */
    BYTES("bytes"),
    /**
     * solidity byte[32]
     */
    BYTES32("bytes32"),
    /**
     * solidity合约函数类型
     */
    FUNCTION("function");

    private String code;

    SolidityTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }}
