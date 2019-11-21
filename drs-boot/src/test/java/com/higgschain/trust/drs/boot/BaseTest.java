package com.higgschain.trust.drs.boot;

import com.alibaba.fastjson.JSON;
import com.alipay.sofa.ark.springboot.runner.ArkBootRunner;
import com.higgschain.trust.drs.api.model.bd.BusinessDefine;
import com.higgschain.trust.drs.api.model.callback.TransactionReceipt;
import com.higgschain.trust.drs.api.model.callback.TxReceiptData;
import com.higgschain.trust.drs.service.model.TxCallbackBO;
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
 * @date 2019-11-07
 */
@RunWith(ArkBootRunner.class)
@SpringBootTest(classes = DRSBootApplication.class)
public class BaseTest {
    @Autowired private TxRequestService txRequestService;
    @Autowired private TxCallbackService callbackService;

    @Test
    public void testSubmitTx()  {
        BusinessDefine bd = new BusinessDefine();
        bd.setTxId("tx_id_" + System.currentTimeMillis());
        txRequestService.submitTx(bd);
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
