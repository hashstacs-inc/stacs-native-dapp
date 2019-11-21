package io.stacs.nav.dapp.core.callback;

/**
 * @author liuyu
 * @description
 * @date 2019-10-23
 */
public interface ITxCallbackHandler {
    /**
     * handle event
     *
     * @param txId
     */
    void handle(String txId);
}
