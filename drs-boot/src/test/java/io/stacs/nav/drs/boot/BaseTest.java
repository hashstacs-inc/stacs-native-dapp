package io.stacs.nav.drs.boot;

import com.alibaba.fastjson.JSON;
import com.alipay.sofa.ark.springboot.runner.ArkBootRunner;
import io.stacs.nav.drs.api.model.bd.BusinessDefine;
import io.stacs.nav.drs.api.model.block.BlockHeader;
import io.stacs.nav.drs.api.model.callback.TransactionReceipt;
import io.stacs.nav.drs.api.model.callback.TxReceiptData;
import io.stacs.nav.drs.service.model.BlockCallbackBO;
import io.stacs.nav.drs.service.service.BlockCallbackService;
import io.stacs.nav.drs.service.service.TxRequestService;
import io.stacs.nav.drs.service.vo.CallbackVO;
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
@RunWith(ArkBootRunner.class) @SpringBootTest(classes = DRSBootApplication.class) public class BaseTest {
    @Autowired private TxRequestService txRequestService;
    @Autowired private BlockCallbackService callbackService;


    @Test public void testSubmitTx() {
        BusinessDefine bd = new BusinessDefine();
        bd.setTxId("tx_id_" + System.currentTimeMillis());
        txRequestService.submitTx(bd);
    }

    @Test public void testReceiveCallbackTx() {
        BlockCallbackBO bo = new BlockCallbackBO();
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
        bo.setBlockData(JSON.toJSONString(vo));
        callbackService.receivedBlock(bo);
    }

}
