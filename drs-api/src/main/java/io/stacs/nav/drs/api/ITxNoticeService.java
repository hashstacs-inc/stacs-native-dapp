package io.stacs.nav.drs.api;

import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.callback.TransactionReceipt;

/**
 * @author liuyu
 * @description
 * @date 2019-12-03
 */
public interface ITxNoticeService {
    /**
     * sync wait result from block chain
     *
     * @param uniqueKey
     * @param timeout
     * @return
     */
    TransactionReceipt syncWait(String uniqueKey,long timeout) throws DappException;
}
