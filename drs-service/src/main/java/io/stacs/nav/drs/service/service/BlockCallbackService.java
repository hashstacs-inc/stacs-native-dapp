package io.stacs.nav.drs.service.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import io.stacs.nav.drs.api.exception.DappError;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.TransactionPO;
import io.stacs.nav.drs.service.action.handler.BDPublishExecHandler;
import io.stacs.nav.drs.service.dao.*;
import io.stacs.nav.drs.service.dao.po.BlockCallbackPO;
import io.stacs.nav.drs.service.dao.po.BlockPO;
import io.stacs.nav.drs.service.dao.po.TxRequestPO;
import io.stacs.nav.drs.service.enums.ActionExecTypeEnum;
import io.stacs.nav.drs.service.enums.CallbackStatus;
import io.stacs.nav.drs.service.enums.RequestStatus;
import io.stacs.nav.drs.service.event.EventPublisher;
import io.stacs.nav.drs.service.model.ActionConverterUtil;
import io.stacs.nav.drs.service.model.BlockCallbackBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.annotation.Validated;

import java.util.*;

import static io.stacs.nav.drs.service.model.ConvertHelper.blockHeader2BlockPO;

/**
 * @author liuyu
 * @description
 * @date 2019-11-07
 */
@Service @Slf4j @Validated public class BlockCallbackService {
    @Autowired TxRequestDao txRequestDao;
    @Autowired BlockCallbackDao blockCallbackDao;
    @Autowired BlockDao blockDao;
    @Autowired TransactionDao txDao;
    @Autowired BlockCallbackDao txCallbackDao;
    @Autowired EventPublisher eventPublisher;
    @Autowired TxNoticeService txNoticeService;
    @Autowired private TransactionTemplate txRequired;

    @Autowired private BDPublishExecHandler bdPublishExecHandler;

    private static final String TX_SUCCESS = "1";

    /**
     * receive transaction from block chain
     *
     * @param bo
     */
    public void receivedBlock(BlockCallbackBO bo) {
        BlockCallbackPO po = new BlockCallbackPO();
        BeanUtils.copyProperties(bo, po);
        po.setStatus(RequestStatus.INIT.name());
        try {
            blockCallbackDao.save(po);
        } catch (DuplicateKeyException e) {
            log.error("[receivedTxs] has duplicate key error", e);
            throw new DappException(DappError.IDEMPOTENT_ERROR);
        } catch (Throwable e) {
            log.error("[receivedTxs] has other error", e);
            throw new DappException(DappError.DB_ERROR);
        }
        log.info("[receivedTxs]is success,blockHeight:{}", bo.getBlockHeight());
    }

    /**
     * process callback block
     */
    public void processCallbackBlock(BlockCallbackBO bo) {
        // todo 1. 块信息
        //      2. 交易数据
        //      3. BD、policy、contract
        BlockVO block = JSON.parseObject(bo.getBlockData(), BlockVO.class);
        BlockPO blockPO = blockHeader2BlockPO.apply(block.getBlockHeader());
        Map<ActionExecTypeEnum, List<ActionPO>> actionsMap = new HashMap<>();
        List<TransactionPO> txList = block.getTransactionList();
        txList.stream().filter(tx -> TX_SUCCESS.equals(tx.getExecuteResult())).sorted(
            Comparator.comparing(TransactionPO::getTxId)).map(ActionConverterUtil::doConvert).forEach(
            optPairs -> optPairs
                .ifPresent(pairs -> pairs.forEach(pair -> actionsMap.compute(pair.left(), (k, oldVal) -> {
                    if (oldVal == null)
                        return Lists.newArrayList((ActionPO)pair.right());
                    oldVal.add((ActionPO)pair.right());
                    return oldVal;
                }))));
        // order by txid
        txRequired.execute(transactionStatus -> {
            // 1. save block
            blockDao.add(blockPO);
            // 2. save txs
            txDao.batchInsert(txList);
            // todo 3. save bd、policy、contract
            bdPublishExecHandler.doHandler((List)actionsMap.get(bdPublishExecHandler.execType()));

            txList.forEach(tx -> {
                TxRequestPO po = txRequestDao.queryByTxId(tx.getTxId());
                if (po != null) {
                    //set status and receipt for request
                    txRequestDao.updateStatusAndReceipt(tx.getTxId(), po.getStatus(), RequestStatus.END.name(),
                                                        JSON.toJSONString(tx));
                    //callback dapp
                    eventPublisher.publish(bo.getBlockHeight(), tx.getTxId(), tx);
                    //notify
                    txNoticeService.notify(tx);
                }
            });
            //update status
            int r = blockCallbackDao.updateStatus(bo.getBlockHeight(), CallbackStatus.INIT.name(),
                                                  CallbackStatus.PROCESSED.name());
            if (r != 1) {
                log.error("[processCallbackTx]update status is error");
                throw new DappException(DappError.DB_ERROR);
            }
            return null;
        });
        log.info("[processCallbackTx]process callback is success,blockHeight:{}", bo.getBlockHeight());
    }

    private void handleBlock() {

    }

    private void handleActions(Map<ActionExecTypeEnum, List<Object>> actionsMap) {

    }

    private <T> Optional<T> headOfOptList(List<T> list) {
        return (list == null || list.size() == 0) ? Optional.empty() : Optional.of(list.get(0));
    }

    private <T> Optional<Class> optClass(T optObj) {
        return optObj == null ? Optional.empty() : Optional.of(optObj.getClass());
    }

}
