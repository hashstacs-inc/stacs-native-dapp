package io.stacs.nav.drs.api.model.attestation;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dekuofa <br>
 * @date 2019-11-01 <br>
 */
@Getter @Setter public class AttestationVO {
    /**
     * version
     */
    private String attestationVersion;
    /**
     * content of attestation
     */
    private String attestation;
    /**
     * remark
     */
    private String remark;
}
