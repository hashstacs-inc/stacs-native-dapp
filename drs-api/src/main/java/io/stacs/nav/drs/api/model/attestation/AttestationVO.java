package io.stacs.nav.drs.api.model.attestation;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dekuofa <br>
 * @date 2019-11-01 <br>
 */
@Getter @Setter public class AttestationVO {
    /**
     * 版本
     */
    private String attestationVersion;
    /**
     * 存证内容
     */
    private String attestation;
    /**
     * 备注
     */
    private String remark;
}
