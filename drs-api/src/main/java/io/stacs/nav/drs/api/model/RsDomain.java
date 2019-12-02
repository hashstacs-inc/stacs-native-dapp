package io.stacs.nav.drs.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type rs-domain node.
 *
 * @author liuyu
 * @date 2019 /07/09
 * @desc rs domain BO
 */
@Getter
@Setter
@NoArgsConstructor
public class RsDomain {
    /**
     * ths id of domain
     */
    private String domainId;
    private String desc;
}
