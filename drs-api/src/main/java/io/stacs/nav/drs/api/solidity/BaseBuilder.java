package io.stacs.nav.drs.api.solidity;

import java.util.List;

/**
 * @author Su Jiulong
 * @date 2019/6/3
 */
public abstract class BaseBuilder {
    /**
     * 映射solidity参数 地址类型
     *
     * @param address evm地址
     * @return 当前对象引用
     */
    public abstract BaseBuilder buildAddress(String address);

    /**
     * 映射solidity参数 地址数组类型
     *
     * @param addressList
     * @return 当前对象引用
     */
    public abstract BaseBuilder buildAddressList(List<String> addressList);

    /**
     * 映射solidity参数 无符号整型
     *
     * @param uint 基本数据类型或者 Number及其子类
     * @return 当前对象引用
     */
    public abstract BaseBuilder buildUint(Number uint);

    /**
     * 映射solidity参数 无符号整型数组
     *
     * @param uintList List<? extends Number>
     * @return 当前对象引用
     */
    public abstract BaseBuilder buildUintList(List<? extends Number> uintList);

    /**
     * 映射solidity参数 有符号整型
     *
     * @param intParam 基本数据类型或者 Number及其子类
     * @return 当前对象引用
     */
    public abstract BaseBuilder buildInt(Number intParam);

    /**
     * 映射solidity参数 有符号整型数组
     *
     * @param intList List<? extends Number>
     * @return 当前对象引用
     */
    public abstract BaseBuilder buildIntList(List<? extends Number> intList);

    /**
     * 映射solidity参数 bool类型
     *
     * @param bool Boolean类型
     * @return 当前对象引用
     */
    public abstract BaseBuilder buildBool(Boolean bool);

    /**
     * 映射solidity参数 bool数组类型
     *
     * @param boolList
     * @return 当前对象引用
     */
    public abstract BaseBuilder buildBoolList(List<Boolean> boolList);

    /**
     * 映射solidity参数 字符串类型
     *
     * @param string string
     * @return 当前对象引用
     */
    public abstract BaseBuilder buildString(String string);

    /**
     * 映射solidity参数 字节数组类型（bytes）
     *
     * @param hexString ：十六进制字符串
     * @return 当前对象引用
     */
    public abstract BaseBuilder buildBytes(String hexString);

    /**
     * 映射solidity参数 32长度的字节数组（byte[32]）
     *
     * @param hexString64 ：长度为64的十六进制字符串
     * @return 当前对象引用
     */
    public abstract BaseBuilder buildBytes32(String hexString64);

    /**
     * 映射solidity参数 函数类型
     *
     * @param function
     * @return 当前对象引用
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
