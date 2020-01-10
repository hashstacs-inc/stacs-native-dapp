package io.stacs.nav.drs.api.solidity;

/**
 * @author Su Jiulong
 * @date 2019/5/30
 */
public enum SolidityTypeEnum {
    /**
     * solidity Contract address: corresponding to ethereum address
     */
    ADDRESS("address"),
    /**
     * solidity Array of contract addresses: List for Java
     */
    ADDRESS_ARR("address[]"),
    /**
     * solidity Contract unsigned integer: Java base type or Number and its subclasses
     */
    UINT("uint"),
    /**
     * solidity Contract unsigned integer: Java base type or Number and its subclasses
     */
    UINT256("uint256"),
    /**
     * solidity Contract unsigned integer array: List
     */
    UINT_ARR("uint[]"),
    /**
     * solidity Contract unsigned integer array: List
     */
    UINT256_ARR("uint256[]"),
    /**
     * solidity Contract integer: Java base type or Number and its subclasses
     */
    INT("int"),
    /**
     * solidity Contract integer: List
     */
    INT_ARR("int[]"),
    /**
     * solidity Contract Boolean type
     */
    BOOL("bool"),
    /**
     * solidity Contract Boolean array
     */
    BOOL_ARR("bool[]"),
    /**
     * solidity String type: String corresponding to Java
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
     * solidity Contract function type
     */
    FUNCTION("function");

    private String code;

    SolidityTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }}
