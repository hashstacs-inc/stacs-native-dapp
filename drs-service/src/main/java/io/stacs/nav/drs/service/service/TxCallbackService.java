package io.stacs.nav.drs.service.service;

import com.alibaba.fastjson.JSON;
import io.stacs.nav.drs.api.exception.DappError;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.callback.TransactionReceipt;
import io.stacs.nav.drs.service.dao.TxCallbackDao;
import io.stacs.nav.drs.service.dao.TxRequestDao;
import io.stacs.nav.drs.service.dao.po.TxCallbackPO;
import io.stacs.nav.drs.service.dao.po.TxRequestPO;
import io.stacs.nav.drs.service.enums.CallbackStatus;
import io.stacs.nav.drs.service.enums.RequestStatus;
import io.stacs.nav.drs.service.event.EventPublisher;
import io.stacs.nav.drs.service.model.TxCallbackBO;
import io.stacs.nav.drs.service.vo.CallbackVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
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
    @Autowired TxNoticeService txNoticeService;
    @Autowired private TransactionTemplate txRequired;
    /**
     * receive transaction from block chain
     *
     * @param bo
     */
    public void receivedBlock(TxCallbackBO bo) {
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
        String receipts = bo.getBlockData();
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
                    //notify
                    txNoticeService.notify(v);
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
