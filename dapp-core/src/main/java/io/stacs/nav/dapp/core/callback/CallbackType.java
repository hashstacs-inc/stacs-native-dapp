package io.stacs.nav.dapp.core.callback;

import io.stacs.nav.drs.api.enums.VersionEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author liuyu
 * @description
 * @date 2020-01-02
 */
@Getter @Setter public class CallbackType {
    private String bdCode;
    private String functionName;
    private String version = VersionEnum.V4.getCode();

    public CallbackType(String functionName) {
        this.functionName = functionName;
    }

    public CallbackType(String bdCode, String functionName) {
        this.bdCode = bdCode;
        this.functionName = functionName;
    }

    public CallbackType(String bdCode, String functionName, String version) {
        this.bdCode = bdCode;
        this.functionName = functionName;
        this.version = version;
    }

    /**
     * key string
     *
     * @return
     */
    public String key(){
        String key = "";
        if(StringUtils.isNotBlank(bdCode)){
            key += bdCode;
        }
        return key + functionName + version;
    }

    public static CallbackType of(String functionName) {
        return new CallbackType(functionName);
    }

    public static CallbackType of(String bdCode, String functionName) {
        return new CallbackType(bdCode, functionName);
    }

    public static CallbackType of(String bdCode, String functionName, String version) {
        return new CallbackType(bdCode, functionName, version);
    }
}
