package io.stacs.nav.drs.service;

import io.stacs.nav.drs.api.model.callback.TransactionReceipt;
import io.stacs.nav.drs.service.config.ConfigurationManagerTest;
import io.stacs.nav.drs.service.service.TxNoticeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * @author liuyu
 * @description
 * @date 2019-12-03
 */
public class TxNoticeServiceTest extends ConfigurationManagerTest {
    @Autowired TxNoticeService noticeService;

    @Test public void test() {
        String txId = "tx-1234";
        //do some thing
        doSomething(txId);
        //wait
        TransactionReceipt tr = noticeService.wait(txId, 6000L);
        Assert.isTrue(tr != null, "wait result is null");
    }

    /**
     * do some thing
     *
     * @param txId
     */
    private void doSomething(String txId) {
        new Thread(() -> {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TransactionReceipt tr = new TransactionReceipt();
            tr.setTxId(txId);
            noticeService.notify(tr);
        }).start();
    }
}
