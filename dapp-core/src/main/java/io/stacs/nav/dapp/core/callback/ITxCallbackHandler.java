package io.stacs.nav.dapp.core.callback;

import io.stacs.nav.drs.api.enums.VersionEnum;
import io.stacs.nav.drs.api.model.TransactionPO;

/**
 * @author liuyu
 * @description
 * @date 2019-10-23
 */
public interface ITxCallbackHandler {
    /**ß
     * action handler supported type
     *
     * @return
     */
    String[] supportType();
    /**
     * supported version
     *
     * @return
     */
    default String supportVersion() {
        return VersionEnum.V4.getCode();
    }
    /**
     *
     * @param po
     */
    void handle(TransactionPO po);
}
