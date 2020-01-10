package io.stacs.nav.drs.service.utils;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author WangQuanzhou
 * @date 2018/2/14 15:02
 */
@Getter @Setter public class CasEncryptRequest implements Serializable {

    private static final long serialVersionUID = 4386940572141675351L;

    private String requestParam;

    private String signature;

    @Override public String toString() {
        return "CasEncryptRequest{" + "requestParam='" + requestParam + '\'' + ", signature='" + signature + '\'' + '}';
    }
}
