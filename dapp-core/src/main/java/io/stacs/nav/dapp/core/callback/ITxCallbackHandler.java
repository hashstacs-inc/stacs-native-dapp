package io.stacs.nav.dapp.core.callback;

import io.stacs.nav.drs.api.model.TransactionPO;

/**
 * @author liuyu
 * @description
 * @date 2019-10-23
 */
public interface ITxCallbackHandler {
    /**
     *
     * @param po
     */
    void handle(TransactionPO po);
}
