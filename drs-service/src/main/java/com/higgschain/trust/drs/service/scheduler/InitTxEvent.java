package com.higgschain.trust.drs.service.scheduler;

import com.higgschain.trust.drs.service.model.TxRequestBO;
import lombok.Getter;
import lombok.ToString;

/**
 * @author suimi
 * @date 2019/8/15
 */
@ToString(exclude = "bo") @Getter public class InitTxEvent {

    private String txId;

    private TxRequestBO bo;

    public void set(String txId, TxRequestBO bo) {
        this.txId = txId;
        this.bo = bo;
    }

    public void clear() {
        this.txId = null;
        this.bo = null;
    }

}
