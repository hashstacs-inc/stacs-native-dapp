package com.higgschain.trust.drs.boot;

import com.alipay.sofa.ark.springboot.runner.ArkBootRunner;
import com.higgschain.trust.drs.boot.service.IDappLifecycleManage;
import com.higgschain.trust.drs.service.model.TxRequestBO;
import com.higgschain.trust.drs.service.service.TxRequestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author liuyu
 * @description
 * @date 2019-11-07
 */
@RunWith(ArkBootRunner.class)
@SpringBootTest(classes = DRSBootApplication.class)
public class BaseTest {
    @Autowired private TxRequestService txRequestService;
    @Autowired private IDappLifecycleManage dappLifecycleManage;

    @Test
    public void testSubmitTx() throws InterruptedException {
        dappLifecycleManage.list();
        TxRequestBO bo = new TxRequestBO();
        bo.setTxId("tx_id_test_" + System.currentTimeMillis());
        bo.setTxApi("/test/api");
        bo.setPolicyId("ISSUE");
        bo.setTxData(new Object());
        txRequestService.submitTx(bo);
        Thread.sleep(1000000);
    }
}
