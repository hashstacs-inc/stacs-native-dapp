package com.higgschain.trust.drs.boot;

import com.alibaba.fastjson.JSON;
import com.alipay.sofa.ark.springboot.runner.ArkBootRunner;
import com.hashstacs.sdk.crypto.GspECKey;
import com.higgschain.trust.drs.model.callback.TransactionReceipt;
import com.higgschain.trust.drs.model.callback.TxReceiptData;
import com.higgschain.trust.drs.service.model.TxCallbackBO;
import com.higgschain.trust.drs.service.model.TxRequestBO;
import com.higgschain.trust.drs.service.model.block.BlockHeader;
import com.higgschain.trust.drs.service.service.TxCallbackService;
import com.higgschain.trust.drs.service.service.TxRequestService;
import com.higgschain.trust.drs.service.vo.CallbackVO;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author liuyu
 * @description
 * @date 2019-11-07
 */
@RunWith(ArkBootRunner.class)
@SpringBootTest(classes = DRSBootApplication.class)
public class BaseTest {
    @Autowired private TxRequestService txRequestService;
    @Autowired private TxCallbackService callbackService;

    @Test
    public void testSubmitTx()  {
        TxRequestBO bo = new TxRequestBO();
        bo.setTxId("tx_id_test_" + System.currentTimeMillis());
        bo.setPolicyId("ISSUE");
        GspECKey ecKey = new GspECKey();
        bo.setSubmitter(ecKey.getHexAddress());
        bo.setBdCode("REGIST_POLICY");
        bo.setFuncName("test()");
        bo.setTxData(new Object());
        txRequestService.submitTx(bo);
    }

    @Test
    public void testReceiveCallbackTx(){
        TxCallbackBO bo = new TxCallbackBO();
        bo.setBlockHeight(2L);
        CallbackVO vo = new CallbackVO();
        BlockHeader header = new BlockHeader();
        header.setHeight(2L);
        vo.setBlockHeader(header);
        List<TransactionReceipt> receipts = Lists.newArrayList();
        TransactionReceipt tr = new TransactionReceipt();
        tr.setResult(true);
        tr.setTxId("tx_id_test_1573176949319");
        tr.setReceiptData(new TxReceiptData());
        receipts.add(tr);

        vo.setReceipts(receipts);
        bo.setTxReceipts(JSON.toJSONString(vo));
        callbackService.receivedTxs(bo);
    }
}
