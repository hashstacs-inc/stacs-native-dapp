package com.higgschain.trust.drs.service.service;

import com.alibaba.fastjson.JSON;
import com.higgschain.trust.drs.api.exception.DappError;
import com.higgschain.trust.drs.api.exception.DappException;
import com.higgschain.trust.drs.api.model.callback.TransactionReceipt;
import com.higgschain.trust.drs.service.dao.TxCallbackDao;
import com.higgschain.trust.drs.service.dao.TxRequestDao;
import com.higgschain.trust.drs.service.dao.po.TxCallbackPO;
import com.higgschain.trust.drs.service.dao.po.TxRequestPO;
import com.higgschain.trust.drs.service.enums.CallbackStatus;
import com.higgschain.trust.drs.service.enums.RequestStatus;
import com.higgschain.trust.drs.service.event.EventPublisher;
import com.higgschain.trust.drs.service.model.TxCallbackBO;
import com.higgschain.trust.drs.service.model.block.BlockHeader;
import com.higgschain.trust.drs.service.network.BlockChainFacade;
import com.higgschain.trust.drs.service.scheduler.InitTxDisruptor;
import com.higgschain.trust.drs.service.vo.CallbackVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.annotation.Validated;

import java.util.Comparator;
import java.util.List;

/**
 * @author liuyu
 * @description
 * @date 2019-11-07
 */
@Service @Slf4j @Validated public class TxCallbackService {
    @Autowired TxRequestDao txRequestDao;
    @Autowired TxCallbackDao txCallbackDao;
    @Autowired EventPublisher eventPublisher;
    @Autowired private TransactionTemplate txRequired;
    /**
     * receive transaction from block chain
     *
     * @param bo
     */
    public void receivedTxs(TxCallbackBO bo) {
        TxCallbackPO po = new TxCallbackPO();
        BeanUtils.copyProperties(bo, po);
        po.setStatus(RequestStatus.INIT.name());
        try {
            txCallbackDao.save(po);
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
     * process callback tx
     *
     * @param bo
     */
    public void processCallbackTx(TxCallbackBO bo) {
        log.info("[processCallbackTx]start process callback,blockHeight:{}", bo.getBlockHeight());
        String receipts = bo.getTxReceipts();
        CallbackVO callbackVO = JSON.parseObject(receipts, CallbackVO.class);
        List<TransactionReceipt> list = callbackVO.getReceipts();
        if (CollectionUtils.isEmpty(list)) {
            log.warn("[processCallbackTx]get receipts is empty,blockHeight:{},receiptJSON:{}", bo.getBlockHeight(),
                receipts);
            return;
        }
        //order by txid
        list.sort(Comparator.comparing(TransactionReceipt::getTxId));
        txRequired.execute(transactionStatus -> {
            list.forEach(v -> {
                TxRequestPO po = txRequestDao.queryByTxId(v.getTxId());
                if (po != null) {
                    //set status and receipt for request
                    txRequestDao.updateStatusAndReceipt(v.getTxId(), po.getStatus(), RequestStatus.END.name(),
                        JSON.toJSONString(v));
                    //callback dapp
                    eventPublisher.publish(bo.getBlockHeight(), v.getTxId(), v);
                }
            });
            //update status
            int r = txCallbackDao
                .updateStatus(bo.getBlockHeight(), CallbackStatus.INIT.name(), CallbackStatus.PROCESSED.name());
            if (r != 1) {
                log.error("[processCallbackTx]update status is error");
                throw new DappException(DappError.DB_ERROR);
            }
            return null;
        });
        log.info("[processCallbackTx]process callback is success,blockHeight:{}", bo.getBlockHeight());
    }
}
