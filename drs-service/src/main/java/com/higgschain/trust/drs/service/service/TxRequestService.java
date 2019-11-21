package com.higgschain.trust.drs.service.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.higgschain.trust.drs.api.enums.FunctionDefineEnum;
import com.higgschain.trust.drs.api.exception.DappError;
import com.higgschain.trust.drs.api.exception.DappException;
import com.higgschain.trust.drs.api.model.BaseTxVO;
import com.higgschain.trust.drs.api.model.RespData;
import com.higgschain.trust.drs.api.model.bd.BusinessDefine;
import com.higgschain.trust.drs.api.model.bd.FunctionDefine;
import com.higgschain.trust.drs.service.dao.TxRequestDao;
import com.higgschain.trust.drs.service.dao.po.TxRequestPO;
import com.higgschain.trust.drs.service.enums.RequestStatus;
import com.higgschain.trust.drs.service.model.TxRequestBO;
import com.higgschain.trust.drs.service.network.BlockChainFacade;
import com.higgschain.trust.drs.service.scheduler.InitTxDisruptor;
import com.higgschain.trust.drs.service.vo.PermissionCheckVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.higgschain.trust.drs.api.exception.DappError.BD_NOT_FIND_ERROR;
import static com.higgschain.trust.drs.api.exception.DappError.DAPP_NET_WORK_COMMON_ERROR;
import static com.higgschain.trust.drs.api.exception.DappException.newError;

/**
 * @author liuyu
 * @description
 * @date 2019-11-07
 */
@Service @Slf4j @Validated public class TxRequestService {
    /**
     * contract type of BD
     */
    private final static String BD_TYPE_CONTRACT = "contract";

    @Autowired TxRequestDao txRequestDao;
    @Autowired InitTxDisruptor initTxDisruptor;
    @Autowired BlockChainFacade blockChainFacade;

    /**
     * receive transaction and request to chain
     *
     * @param vo
     */
    public void submitTx(@Valid BaseTxVO vo) throws DappException {
        //query business define by bdCode
        BusinessDefine bd = blockChainFacade.queryBDInfoByCode(vo.getBdCode()).orElseThrow(newError(BD_NOT_FIND_ERROR));
        String execPolicyId;
        String execFuncName;
        String execPermission;
        if (FunctionDefineEnum.CREATE_CONTRACT.getFunctionName().equals(vo.getFunctionName())) {
            execPolicyId = bd.getInitPolicy();
            execFuncName = FunctionDefineEnum.CREATE_CONTRACT.getFunctionName();
            execPermission = bd.getInitPermission();
        } else {
            List<FunctionDefine> functions = bd.getFunctions();
            Optional<FunctionDefine> define =
                functions.stream().filter(a -> a.getName().equals(vo.getFunctionName())).findFirst();
            //check function
            define.orElseThrow(() -> {
                log.warn("function define is not find,functionName:{},txId:{}", vo.getFunctionName(), vo.getTxId());
                return new DappException(DappError.FUNCTION_NOT_FIND_ERROR);
            });
            FunctionDefine fd = define.get();
            execPolicyId = fd.getExecPolicy();
            execPermission = fd.getExecPermission();
            //contract type
            if (BD_TYPE_CONTRACT.equalsIgnoreCase(fd.getType())) {
                execFuncName = FunctionDefineEnum.CONTRACT_INVOKER.getFunctionName();
            } else {
                execFuncName = fd.getName();
            }
        }
        //check permission
        checkPermission(vo.getSubmitter(), execPermission);
        //set policy id
        vo.setExecPolicyId(execPolicyId);
        //make request
        TxRequestBO bo = new TxRequestBO();
        bo.setTxId(vo.getTxId());
        bo.setPolicyId(vo.getExecPolicyId());
        bo.setBdCode(vo.getBdCode());
        bo.setSubmitter(vo.getSubmitter());
        bo.setFuncName(execFuncName);
        bo.setTxData(vo);
        bo.setStatus(RequestStatus.INIT);
        //save and send
        saveAndSend(bo);
    }

    /**
     * save and send request
     *
     * @param bo
     */
    private void saveAndSend(TxRequestBO bo) {
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
        initTxDisruptor.publish(bo.getTxId(), bo);
    }

    /**
     * check permission
     *
     * @param address
     * @param permission
     */
    private void checkPermission(String address, String permission) {
        PermissionCheckVO vo = new PermissionCheckVO();
        vo.setAddress(address);
        vo.setPermissionNames(Lists.newArrayList(permission));
        //permission check
        boolean res = blockChainFacade.checkPermission(vo).orElseThrow(newError(DAPP_NET_WORK_COMMON_ERROR));

        if (!res) {
            log.warn("address:{} not has permission:{}", address, permission);
            throw new DappException(DappError.NO_PERMISSION_ERROR);
        }
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
            //send to block chain
            RespData response =
                blockChainFacade.send(FunctionDefineEnum.fromFuncName(bo.getFuncName()).getApi(), bo.getTxData());
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
