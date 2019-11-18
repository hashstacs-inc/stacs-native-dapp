package com.higgschain.trust.drs.service.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.hashstacs.sdk.wallet.dock.bo.CasDecryptReponse;
import com.higgschain.trust.drs.exception.DappError;
import com.higgschain.trust.drs.exception.DappException;
import com.higgschain.trust.drs.model.PermissionInfo;
import com.higgschain.trust.drs.model.bd.BusinessDefine;
import com.higgschain.trust.drs.model.bd.FunctionDefine;
import com.higgschain.trust.drs.service.dao.TxRequestDao;
import com.higgschain.trust.drs.service.dao.po.TxRequestPO;
import com.higgschain.trust.drs.service.enums.RequestStatus;
import com.higgschain.trust.drs.service.model.TxRequestBO;
import com.higgschain.trust.drs.service.network.BlockChainFacade;
import com.higgschain.trust.drs.service.scheduler.InitTxDisruptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        //check
        checkRequest(bo);
        //make po
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
     * check request data
     *
     * @param bo
     */
    private void checkRequest(TxRequestBO bo) {
        //query business define by bdCode
        List<BusinessDefine> bdList = Lists.newArrayList();//TODO:/bd/query
        if (CollectionUtils.isEmpty(bdList)) {
            log.warn("business define is not find, txId:{}", bo.getTxId());
            throw new DappException(DappError.BD_NOT_FIND_ERROR);
        }
        BusinessDefine bd = bdList.get(0);
        List<FunctionDefine> functions = bd.getFunctions();
        Optional<FunctionDefine> define =
            functions.stream().filter(a -> a.getName().equals(bo.getFuncName())).findFirst();
        //check function
        define.orElseThrow(() -> {
            log.warn("function define is not find,functionName:{},txId:{}", bo.getFuncName(), bo.getTxId());
            return new DappException(DappError.FUNCTION_NOT_FIND_ERROR);
        });
        FunctionDefine fd = define.get();
        String permission = fd.getExecPermission();
        //get user permission by address/identity
        List<PermissionInfo> permissions = Lists.newArrayList();//TODO:/identity/permission/query
        if (CollectionUtils.isEmpty(permissions)) {
            log.warn("get permission is null,address:{},txId:{}", bo.getSubmitter(), bo.getTxId());
            throw new DappException(DappError.NO_PERMISSION_ERROR);
        }
        //check permission
        permissions.stream().filter(a -> a.getPermissionName().equals(permission)).findFirst().orElseThrow(() -> {
            log.warn("address:{} do not have permission:{},funcName:{},txId:{}", bo.getSubmitter(), permission,
                bo.getFuncName(), bo.getTxId());
            return new DappException(DappError.NO_PERMISSION_ERROR);
        });
    }

    public static void main(String[] args) {
        List<PermissionInfo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PermissionInfo pi = new PermissionInfo();
            pi.setPermissionName("name_" + i);
            list.add(pi);
        }
        String permission = "name_1";
        List<PermissionInfo> st =
            list.stream().filter(a -> a.getPermissionName().equals(permission)).collect(Collectors.toList());
        System.out.println(st.size());

        Optional<PermissionInfo> pf = list.stream().filter(a -> a.getPermissionName().equals(permission)).findFirst();
        pf.orElseThrow(() -> new RuntimeException("x"));
        System.out.println(pf.get());
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
        int r = txRequestDao.updateStatus(bo.getTxId(), RequestStatus.INIT.name(), RequestStatus.SUBMITTING.name());
        if (r != 1) {
            log.error("[processInit] update status is error,txId:{}", bo.getTxId());
            throw new DappException(DappError.DAPP_UPDATE_STATUS_ERROR);
        }
        try {
            //TODO:API
            //send to block chain
            CasDecryptReponse response = blockChainFacade.send(bo.getFuncName(), bo.getTxData());
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
