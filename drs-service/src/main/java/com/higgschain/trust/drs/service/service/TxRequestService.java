package com.higgschain.trust.drs.service.service;

import com.alibaba.fastjson.JSON;
import com.hashstacs.sdk.wallet.dock.bo.CasDecryptReponse;
import com.higgschain.trust.drs.exception.DappError;
import com.higgschain.trust.drs.exception.DappException;
import com.higgschain.trust.drs.service.dao.TxRequestDao;
import com.higgschain.trust.drs.service.dao.po.TxRequestPO;
import com.higgschain.trust.drs.service.enums.RequestStatus;
import com.higgschain.trust.drs.service.model.TxRequestBO;
import com.higgschain.trust.drs.service.network.BlockChainFacade;
import com.higgschain.trust.drs.service.scheduler.InitTxDisruptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * @author liuyu
 * @description
 * @date 2019-11-07
 */
@Service @Slf4j @Validated public class TxRequestService {
    @Autowired TxRequestDao txRequestDao;
    @Autowired InitTxDisruptor initTxDisruptor;
    @Autowired BlockChainFacade blockChainFacade;

    /**
     * receive transaction and request to chain
     *
     * @param bo
     */
    public void submitTx(@Valid TxRequestBO bo) {
        TxRequestPO po = new TxRequestPO();
        BeanUtils.copyProperties(bo, po);
        po.setTxData(JSON.toJSONString(bo.getTxData()));
        po.setStatus(RequestStatus.INIT.name());
        try {
            txRequestDao.save(po);
        } catch (DuplicateKeyException e) {
            log.error("save request info has duplicate key error", e);
            throw new DappException(DappError.IDEMPOTENT_ERROR);
        } catch (Throwable e) {
            log.error("save request info has other error", e);
            throw new DappException(DappError.DB_ERROR);
        }
        bo.setStatus(RequestStatus.INIT);
        initTxDisruptor.publish(bo.getTxId(), bo);
    }

    /**
     * process tx request
     *
     * @param bo
     */
    public void processTxRequest(TxRequestBO bo) {
        if (bo.getStatus() != RequestStatus.INIT) {
            log.warn("[processInit]status is not INIT,txId:{}", bo.getTxId());
            return;
        }
        //update status from INIT to PROCESSING
        int r = txRequestDao.updateStatus(bo.getTxId(), RequestStatus.INIT.name(), RequestStatus.PROCESSING.name());
        if (r != 1) {
            log.error("[processInit] update status is error,txId:{}", bo.getTxId());
            throw new DappException(DappError.DAPP_UPDATE_STATUS_ERROR);
        }
        try {
            //send to block chain
            CasDecryptReponse response = blockChainFacade.send(bo.getTxApi(), bo.getTxData());
            //update to END status
            r = txRequestDao
                .updateStatusAndReceipt(bo.getTxId(), RequestStatus.PROCESSING.name(), RequestStatus.END.name(),
                    response != null ? JSON.toJSONString(response) : null);
            if (r != 1) {
                log.error("[processInit] update status and receipt is error,txId:{}", bo.getTxId());
                throw new DappException(DappError.DAPP_UPDATE_STATUS_ERROR);
            }
        } catch (Throwable e) {
            log.error("[processInit]send to block chain has error", e);
            //update status to INIT
            r = txRequestDao.updateStatus(bo.getTxId(), RequestStatus.PROCESSING.name(), RequestStatus.INIT.name());
            if (r != 1) {
                log.error("[processInit] update status is error,txId:{}", bo.getTxId());
                throw new DappException(DappError.DAPP_UPDATE_STATUS_ERROR);
            }
        }
    }
}
