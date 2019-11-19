package com.higgschain.trust.drs.service.utils;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author WangQuanzhou
 * @desc 钱包与Cas Realm对接  请求参数
 * @date 2018/2/14 15:02
 */
@Getter @Setter public class CasEncryptRequest implements Serializable {

    private static final long serialVersionUID = 4386940572141675351L;

    /**
     * requestParam字段
     */
    private String requestParam;

    /**
     * 签名字段
     */
    private String signature;

    @Override public String toString() {
        return "CasEncryptRequest{" + "requestParam='" + requestParam + '\'' + ", signature='" + signature + '\'' + '}';
    }
}
