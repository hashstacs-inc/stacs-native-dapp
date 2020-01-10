package io.stacs.nav.drs.api.solidity;

import java.util.List;

/**
 * @author Su Jiulong
 * @date 2019/6/3
 */
public abstract class BaseBuilder {
    /**
     * Map the solidity parameter address type
     *
     * @param address
     * @return Current object reference
     */
    public abstract BaseBuilder buildAddress(String address);

    /**
     * Map the address array type of the solidity parameter
     *
     * @param addressList
     * @return
     */
    public abstract BaseBuilder buildAddressList(List<String> addressList);

    /**
     * Mapping solidity parameter unsigned integer
     *
     * @param uint
     * @return
     */
    public abstract BaseBuilder buildUint(Number uint);

    /**
     * Map an unsigned integer array of solidity parameters
     *
     * @param uintList List<? extends Number>
     * @return
     */
    public abstract BaseBuilder buildUintList(List<? extends Number> uintList);

    /**
     * The solidity parameter is mapped to a signed integer
     *
     * @param intParam
     * @return
     */
    public abstract BaseBuilder buildInt(Number intParam);

    /**
     * The mapping solidity parameter has an array of signed integers
     *
     * @param intList List<? extends Number>
     * @return
     */
    public abstract BaseBuilder buildIntList(List<? extends Number> intList);

    /**
     * Map the bool type of the solidity parameter
     *
     * @param bool
     * @return
     */
    public abstract BaseBuilder buildBool(Boolean bool);

    /**
     * Maps the array type of the solidity parameter bool
     *
     * @param boolList
     * @return
     */
    public abstract BaseBuilder buildBoolList(List<Boolean> boolList);

    /**
     * Map the solidity parameter string type
     *
     * @param string string
     * @return
     */
    public abstract BaseBuilder buildString(String string);

    /**
     * Mapping the byte array type of solidity parameter (bytes)
     *
     * @param hexString ：Hexadecimal string
     * @return
     */
    public abstract BaseBuilder buildBytes(String hexString);

    /**
     * An array of bytes (byte[32]) that maps the length of the solidity parameter 32.
     *
     * @param hexString64 ：A hexadecimal string of length 64
     * @return
     */
    public abstract BaseBuilder buildBytes32(String hexString64);

    /**
     * Map the solfat parameter function type
     *
     * @param function
     * @return
     */
    public abstract BaseBuilder buildFunction(Object function);

    /**
     * build type solidity type
     * @param typeEnum
     * @param value
     * @return
     */
    public abstract BaseBuilder buildByType(SolidityTypeEnum typeEnum, Object value);

    public abstract List<EvmSignArgs> build();
}
