package io.stacs.nav.drs.service.service;

import com.alibaba.fastjson.JSON;
import io.stacs.nav.drs.api.enums.ActionTypeEnum;
import io.stacs.nav.drs.api.enums.ApiConstants;
import io.stacs.nav.drs.api.exception.DappError;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.RespData;
import io.stacs.nav.drs.api.model.TransactionPO;
import io.stacs.nav.drs.service.dao.TxRequestDao;
import io.stacs.nav.drs.service.enums.RequestStatus;
import io.stacs.nav.drs.service.enums.VersionEnum;
import io.stacs.nav.drs.service.event.EventPublisher;
import io.stacs.nav.drs.service.model.TxRequestBO;
import io.stacs.nav.drs.service.network.BlockChainFacade;
import io.stacs.nav.drs.service.network.BlockChainHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * @author liuyu
 * @description
 * @date 2019-11-07
 */
@Service @Slf4j @Validated public class TxReqProcessService {
    @Autowired TxRequestDao txRequestDao;
    @Autowired BlockChainFacade blockChainFacade;
    @Autowired BlockChainService blockChainService;
    @Autowired BlockChainHelper blockChainHelper;
    @Autowired EventPublisher eventPublisher;
    @Autowired TxNoticeService txNoticeService;

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
        int r = txRequestDao.updateStatus(bo.getTxId(), RequestStatus.INIT.name(), RequestStatus.SUBMITTING.name());
        if (r != 1) {
            log.error("[processInit] update status is error,txId:{}", bo.getTxId());
            throw new DappException(DappError.DAPP_UPDATE_STATUS_ERROR);
        }
        try {
            //send to block chain
            RespData respData = blockChainHelper
                .post(ApiConstants.TransactionApiEnum.fromTxName(bo.getFuncName()).getApi(), bo.getTxData(),
                    Object.class);
            //            RespData response = blockChainFacade
            //                .send(ApiConstants.TransactionApiEnum.fromTxName(bo.getFuncName()).getApi(), bo.getTxData());
            //update to END status
            if (respData.isSuccessful()) {
                r = txRequestDao.updateStatusAndReceipt(bo.getTxId(), RequestStatus.SUBMITTING.name(),
                    RequestStatus.PROCESSING.name(), JSON.toJSONString(respData));
                if (r != 1) {
                    log.error("[processInit] update status and receipt is error,txId:{}", bo.getTxId());
                    throw new DappException(DappError.DAPP_UPDATE_STATUS_ERROR);
                }
            } else {
                r = txRequestDao
                    .updateStatusAndReceipt(bo.getTxId(), RequestStatus.SUBMITTING.name(), RequestStatus.END.name(),
                        respData.getMsg());
                if (r != 1) {
                    log.error("[processInit] update status and receipt is error,txId:{}", bo.getTxId());
                    throw new DappException(DappError.DAPP_UPDATE_STATUS_ERROR);
                }
                //callback dapp
                TransactionPO tx = new TransactionPO();
                tx.setTxId(bo.getTxId());
                tx.setBizModel(JSON.toJSONString(bo.getTxData()));
                tx.setExecuteResult("0");
                tx.setErrorCode(respData.getCode());
                tx.setErrorMessage(respData.getMsg());
                ApiConstants.TransactionApiEnum transactionApiEnum = ApiConstants.TransactionApiEnum.fromTxName(bo.getFuncName());
                if(transactionApiEnum != null){
                    tx.setTxType(transactionApiEnum.getActionTypeEnum().getCode());
                }
                tx.setVersion(VersionEnum.V4.getCode());
                //event
                eventPublisher.publish(0L, bo.getTxId(), tx);
                //notify
                txNoticeService.notify(tx);
            }
        } catch (Throwable e) {
            log.error("[processInit]send to block chain has error", e);
            //update status to INIT
            r = txRequestDao.updateStatus(bo.getTxId(), RequestStatus.SUBMITTING.name(), RequestStatus.INIT.name());
            if (r != 1) {
                log.error("[processInit] update status is error,txId:{}", bo.getTxId());
                throw new DappException(DappError.DAPP_UPDATE_STATUS_ERROR);
            }
        }
    }
}
