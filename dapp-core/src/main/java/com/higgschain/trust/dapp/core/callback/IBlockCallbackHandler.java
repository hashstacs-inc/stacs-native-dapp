package com.higgschain.trust.dapp.core.callback;

/**
 * @author liuyu
 * @description
 * @date 2019-10-23
 */
public interface IBlockCallbackHandler {
    /**
     * handle event
     *
     * @param height
     */
    void handle(Long height);
}
