package io.stacs.nav.drs.api.solidity;

import com.alibaba.fastjson.JSONArray;
import org.spongycastle.util.encoders.Hex;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Su Jiulong
 * @date 2019/6/3
 */
public class SolidityParamBuilder extends BaseBuilder {

    private List<EvmSignArgs> signArgs = null;

    public SolidityParamBuilder() {
        signArgs = new ArrayList<>();
    }

    public SolidityParamBuilder(int size) {
        signArgs = new ArrayList<>(size);
    }

    @Override
    public BaseBuilder buildAddress(String address) {
        signArgs.add(new EvmSignArgs(SolidityTypeEnum.ADDRESS, address));
        return this;
    }

    @Override
    public BaseBuilder buildAddressList(List addressList) {
        signArgs.add(new EvmSignArgs(SolidityTypeEnum.ADDRESS_ARR, addressList));
        return this;
    }


    @Override
    public BaseBuilder buildUint(Number uint) {
        signArgs.add(new EvmSignArgs(SolidityTypeEnum.UINT, uint));
        return this;
    }

    @Override
    public BaseBuilder buildUintList(List<? extends Number> uintList) {
        signArgs.add(new EvmSignArgs(SolidityTypeEnum.UINT_ARR, uintList));
        return this;
    }

    @Override
    public BaseBuilder buildInt(Number intParam) {
        signArgs.add(new EvmSignArgs(SolidityTypeEnum.INT, intParam));
        return this;
    }

    @Override
    public BaseBuilder buildIntList(List<? extends Number> intList) {
        signArgs.add(new EvmSignArgs(SolidityTypeEnum.INT_ARR, intList));
        return this;
    }

    @Override
    public BaseBuilder buildBool(Boolean bool) {
        signArgs.add(new EvmSignArgs(SolidityTypeEnum.BOOL, bool));
        return this;
    }

    @Override
    public BaseBuilder buildBoolList(List<Boolean> boolList) {
        signArgs.add(new EvmSignArgs(SolidityTypeEnum.BOOL_ARR, boolList));
        return this;
    }

    @Override
    public BaseBuilder buildString(String string) {
        signArgs.add(new EvmSignArgs(SolidityTypeEnum.STRING, string));
        return this;
    }

    @Override
    public BaseBuilder buildBytes(String hexString) {
        byte[] bytes = Hex.decode(hexString);
        signArgs.add(new EvmSignArgs(SolidityTypeEnum.BYTES, bytes));
        return this;
    }

    @Override
    public BaseBuilder buildBytes32(String hexString64) {
        if (null == hexString64 || hexString64.length() != 64) {
            throw new IllegalArgumentException("Only hex strings of 64 length are accepted");
        }
        byte[] bytes = Hex.decode(hexString64);
        signArgs.add(new EvmSignArgs(SolidityTypeEnum.BYTES32, bytes));
        return this;
    }

    @Override
    public BaseBuilder buildFunction(Object function) {
        signArgs.add(new EvmSignArgs(SolidityTypeEnum.FUNCTION, function));
        return this;
    }

    @Override
    public BaseBuilder buildByType(SolidityTypeEnum typeEnum, Object value) {
        if(typeEnum == SolidityTypeEnum.BYTES || typeEnum == SolidityTypeEnum.BYTES32){
            byte[] bytes = Hex.decode(value.toString());
            signArgs.add(new EvmSignArgs(typeEnum, bytes));
        }else if(value instanceof JSONArray){
            List list = JSONArray.parseArray(((JSONArray)value).toJSONString());
            signArgs.add(new EvmSignArgs(typeEnum, list));
        }else{
            signArgs.add(new EvmSignArgs(typeEnum, value));
        }
        return this;
    }

    @Override
    public List<EvmSignArgs> build() {
        return signArgs;
    }


}
