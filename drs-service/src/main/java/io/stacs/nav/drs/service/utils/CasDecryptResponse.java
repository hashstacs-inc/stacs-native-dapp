package io.stacs.nav.drs.service.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author WangQuanzhou
 * @date 2018/2/14 15:28
 */
@Getter @Setter public class CasDecryptResponse {

    private String respCode = "000000";

    private String msg = "success";


    private Object data;

    @Override public String toString() {
        return "CasDecryptReponse{" + "respCode='" + respCode + '\'' + ", msg='" + msg + '\'' + ", data=" + data + '}';
    }
}
