package io.stacs.nav.drs.api.model;

import io.stacs.nav.drs.api.model.policy.PolicyVO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyu
 * @description
 * @date 2020-02-07
 */
@Getter
@Setter
public class TxDetail extends TransactionVO{
    private PolicyVO policy;
}
