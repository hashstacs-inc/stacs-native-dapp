package com.higgschain.trust.drs.service.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author WangQuanzhou
 * @desc 钱包与Cas Realm对接  返回参数
 * @date 2018/2/14 15:28
 */
@Getter @Setter public class CasDecryptResponse {

    /**
     * 消息代码，由RespCode中的code和subCode组成，分别是3位
     */
    private String respCode = "000000";

    /**
     * 消息描述
     */
    private String msg = "success";

    /**
     * 数据对象
     */

    private Object data;

    @Override public String toString() {
        return "CasDecryptReponse{" + "respCode='" + respCode + '\'' + ", msg='" + msg + '\'' + ", data=" + data + '}';
    }
}
