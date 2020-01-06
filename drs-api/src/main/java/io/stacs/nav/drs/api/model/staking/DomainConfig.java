package io.stacs.nav.drs.api.model.staking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author tangkun
 * @date 2019-09-06
 */
@Getter
@Setter
@AllArgsConstructor
public class DomainConfig implements java.io.Serializable {

    private String domainId;

    private String stakingAddress;

    private String participationAddress;
}
