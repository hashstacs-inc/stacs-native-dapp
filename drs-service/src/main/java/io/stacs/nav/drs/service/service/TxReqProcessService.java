package io.stacs.nav.drs.service.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import io.stacs.nav.drs.api.enums.ApiConstants;
import io.stacs.nav.drs.api.exception.DappError;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.BaseTxVO;
import io.stacs.nav.drs.api.model.RespData;
import io.stacs.nav.drs.api.model.bd.BusinessDefine;
import io.stacs.nav.drs.api.model.bd.FunctionDefine;
import io.stacs.nav.drs.service.dao.TxRequestDao;
import io.stacs.nav.drs.service.dao.po.TxRequestPO;
import io.stacs.nav.drs.service.enums.RequestStatus;
import io.stacs.nav.drs.service.model.TxRequestBO;
import io.stacs.nav.drs.service.network.BlockChainFacade;
import io.stacs.nav.drs.service.scheduler.InitTxDisruptor;
import io.stacs.nav.drs.service.vo.PermissionCheckVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static io.stacs.nav.drs.api.enums.ApiConstants.TransactionApiEnum.CONTRACT_INVOKER;
import static io.stacs.nav.drs.api.enums.ApiConstants.TransactionApiEnum.CREATE_CONTRACT;
import static io.stacs.nav.drs.api.exception.DappError.DAPP_NETWORK_COMMON_ERROR;
import static io.stacs.nav.drs.api.exception.DappException.newError;

/**
 * @author liuyu
 * @description
 * @date 2019-11-07
 */
@Service @Slf4j @Validated public class TxReqProcessService {
    @Autowired TxRequestDao txRequestDao;
    @Autowired BlockChainFacade blockChainFacade;
    @Autowired BlockChainService blockChainService;
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
            RespData response = blockChainFacade
                .send(ApiConstants.TransactionApiEnum.fromTxName(bo.getFuncName()).getApi(), bo.getTxData());
            //update to END status
            r = txRequestDao
                .updateStatusAndReceipt(bo.getTxId(), RequestStatus.SUBMITTING.name(), RequestStatus.PROCESSING.name(),
                    response != null ? JSON.toJSONString(response) : null);
            if (r != 1) {
                log.error("[processInit] update status and receipt is error,txId:{}", bo.getTxId());
                throw new DappException(DappError.DAPP_UPDATE_STATUS_ERROR);
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
