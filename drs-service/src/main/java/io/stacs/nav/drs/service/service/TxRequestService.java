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
import io.stacs.nav.drs.service.dao.BusinessDefineDao;
import io.stacs.nav.drs.service.dao.TxRequestDao;
import io.stacs.nav.drs.service.dao.po.BusinessDefinePO;
import io.stacs.nav.drs.service.dao.po.TxRequestPO;
import io.stacs.nav.drs.service.enums.RequestStatus;
import io.stacs.nav.drs.service.model.TxRequestBO;
import io.stacs.nav.drs.service.network.BlockChainFacade;
import io.stacs.nav.drs.service.network.BlockChainHelper;
import io.stacs.nav.drs.service.scheduler.InitTxDisruptor;
import io.stacs.nav.drs.service.utils.BeanConvertor;
import io.stacs.nav.drs.service.vo.PermissionCheckVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static io.stacs.nav.drs.api.enums.ApiConstants.QueryApiEnum.CHECK_PERMISSION;
import static io.stacs.nav.drs.api.enums.ApiConstants.TransactionApiEnum.CONTRACT_INVOKER;
import static io.stacs.nav.drs.api.enums.ApiConstants.TransactionApiEnum.CREATE_CONTRACT;
import static io.stacs.nav.drs.api.exception.DappError.BD_NOT_FIND_ERROR;
import static io.stacs.nav.drs.api.exception.DappError.DAPP_NETWORK_COMMON_ERROR;
import static io.stacs.nav.drs.api.exception.DappException.newError;

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
    @Autowired BlockChainHelper blockChainHelper;
    @Autowired BlockChainService blockChainService;
    @Autowired BusinessDefineDao businessDefineDao;

    /**
     * receive transaction and request to chain
     *
     * @param vo
     */
    public void submitTx(@Valid BaseTxVO vo) throws DappException {
        log.info("[submitTx]is start,{}", JSON.toJSONString(vo));
        //query business define by bdCode
        BusinessDefinePO po = businessDefineDao.queryBDByCode(vo.getBdCode());
        log.info("[submit]query BD from database:{}", JSON.toJSONString(po));
        if (po == null) {
            log.warn("[submit]bd is not exists,bdCode:{}", vo.getBdCode());
            throw new DappException(BD_NOT_FIND_ERROR);
        }
        BusinessDefine bd = BeanConvertor.convertBean(po, BusinessDefine.class);
        if (bd != null && StringUtils.isNotEmpty(po.getFunctions())) {
            List<FunctionDefine> functionDefines = JSON.parseArray(po.getFunctions(), FunctionDefine.class);
            bd.setFunctions(functionDefines);
        }
        String execPolicyId;
        String execFuncName;
        String execPermission;
        if (CREATE_CONTRACT.getFunctionName().equals(vo.getFunctionName())) {
            execPolicyId = bd.getInitPolicy();
            execFuncName = CREATE_CONTRACT.getFunctionName();
            execPermission = bd.getInitPermission();
        } else {
            List<FunctionDefine> functions = bd.getFunctions();
            Optional<FunctionDefine> define =
                functions.stream().filter(a -> a.getName().equals(vo.getFunctionName())).findFirst();
            //check function
            if (!define.isPresent()) {
                log.warn("function define is not find,functionName:{},txId:{}", vo.getFunctionName(), vo.getTxId());
                throw new DappException(DappError.FUNCTION_NOT_FIND_ERROR);
            }
            FunctionDefine fd = define.get();
            execPolicyId = fd.getExecPolicy();
            execPermission = fd.getExecPermission();
            //contract type
            if (BD_TYPE_CONTRACT.equalsIgnoreCase(fd.getType())) {
                execFuncName = CONTRACT_INVOKER.getFunctionName();
            } else {
                execFuncName = fd.getName();
            }
        }
        log.info("[submit]txId:{},execPolicyId:{}", vo.getTxId(), execPolicyId);
        log.info("[submit]txId:{},execFuncName:{}", vo.getTxId(), execFuncName);
        log.info("[submit]txId:{},execPermission:{}", vo.getTxId(), execPermission);
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
        RespData<Boolean> res = blockChainHelper.post(CHECK_PERMISSION.getApi(), vo,
            Boolean.class);// blockChainFacade.checkPermission(vo).orElseThrow(newError(DAPP_NETWORK_COMMON_ERROR));

        //TODO:liuyu
        //        if (!res.isSuccessful() || !res.getData()) {
        //            log.warn("address:{} not has permission:{}", address, permission);
        //            throw new DappException(DappError.NO_PERMISSION_ERROR);
        //        }
    }
}
