package io.stacs.nav.drs.api.model.staking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyu
 * @date 2020-01-06
 */
@Getter
@Setter
@AllArgsConstructor
public class StakingDomain implements java.io.Serializable {

    private String domainId;

    private String stakingAddress;

    private String participationAddress;
}
