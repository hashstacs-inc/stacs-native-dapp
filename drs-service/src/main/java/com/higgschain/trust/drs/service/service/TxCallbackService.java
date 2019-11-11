package com.higgschain.trust.drs.service.service;

import com.alibaba.fastjson.JSON;
import com.higgschain.trust.drs.exception.DappError;
import com.higgschain.trust.drs.exception.DappException;
import com.higgschain.trust.drs.model.callback.TransactionReceipt;
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
import org.springframework.validation.annotation.Validated;

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
        list.forEach(v -> {
            //callback dapp
            eventPublisher.publish(bo.getBlockHeight(), v.getTxId(), v);
            TxRequestPO po = txRequestDao.queryByTxId(v.getTxId());
            if (po != null) {
                //set receipt for request
                txRequestDao.updateReceipt(v.getTxId(), JSON.toJSONString(v));
            }
        });
        //update status
        int r = txCallbackDao
            .updateStatus(bo.getBlockHeight(), CallbackStatus.INIT.name(), CallbackStatus.PROCESSED.name());
        if (r != 1) {
            log.error("[processCallbackTx]update status is error");
            throw new DappException(DappError.DB_ERROR);
        }
        log.info("[processCallbackTx]process callback is success,blockHeight:{}", bo.getBlockHeight());
    }
}
