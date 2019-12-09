package io.stacs.nav.drs.service.model;

import com.alibaba.fastjson.JSON;
import io.stacs.nav.drs.api.model.TransactionPO;
import io.stacs.nav.drs.api.model.bo.Block;
import io.stacs.nav.drs.api.model.bo.BlockHeader;
import io.stacs.nav.drs.api.model.bo.SignedTransaction;
import io.stacs.nav.drs.api.model.callback.TransactionReceipt;
import io.stacs.nav.drs.service.dao.po.BlockCallbackPO;
import io.stacs.nav.drs.service.dao.po.BlockPO;
import io.stacs.nav.drs.service.utils.BeanConvertor;
import org.springframework.beans.BeanUtils;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author dekuofa <br>
 * @date 2019-12-09 <br>
 */
public interface ConvertHelper {

    Function<Block, BlockCallbackBO> block2CallbackBOConvert = block -> {
        BlockCallbackBO bo = new BlockCallbackBO();
        bo.setBlockHeight(block.getBlockHeader().getHeight());
        bo.setBlockData(JSON.toJSONString(block));
        return bo;
    };

    Function<BlockCallbackBO, BlockCallbackPO> block2CallbackPOConvert = bo -> {
        BlockCallbackPO po = new BlockCallbackPO();
        BeanUtils.copyProperties(bo, po);
        return po;
    };

    Function<BlockCallbackPO, BlockCallbackPO> setPOInitState = po -> {
        po.setStatus("INIT");
        return po;
    };

    // 块信息
    Function<BlockHeader, BlockPO> blockHeader2BlockPO = header -> {
        BlockPO po = BeanConvertor.convertBean(header, BlockPO.class);
        BeanUtils.copyProperties(header.getStateRootHash(), po);
        return po;
    };

    // 交易数据
    Function<SignedTransaction, TransactionPO> signedTx2TxPO = singedTx -> {
        TransactionPO txPO = BeanConvertor.convertBean(singedTx.getCoreTx(), TransactionPO.class);
        TransactionReceipt receipt = singedTx.getReceipt();
        if (Objects.nonNull(receipt) && Objects.nonNull(receipt.getReceiptData())) {
            txPO.setFeeAmount(receipt.getReceiptData().getFeeInfo().getFeeAmount().toString());
        }
        return txPO;
    };

}
