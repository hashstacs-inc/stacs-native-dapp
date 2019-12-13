package io.stacs.nav.drs.service.service;

import com.alibaba.fastjson.JSON;
import io.stacs.nav.drs.api.ISignatureService;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.BaseTxVO;
import io.stacs.nav.drs.api.model.bd.BusinessDefine;
import io.stacs.nav.drs.api.model.bd.FunctionDefine;
import io.stacs.nav.drs.service.dao.BusinessDefineDao;
import io.stacs.nav.drs.service.dao.po.BusinessDefinePO;
import io.stacs.nav.drs.service.utils.BeanConvertor;
import io.stacs.nav.drs.service.utils.JSONHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static io.stacs.nav.drs.api.enums.ApiConstants.TransactionApiEnum.CREATE_CONTRACT;
import static io.stacs.nav.drs.api.exception.DappError.DAPP_COMMON_ERROR;
import static io.stacs.nav.drs.api.exception.DappError.FUNCTION_NOT_FIND_ERROR;
import static io.stacs.nav.drs.api.exception.DappException.newError;

/**
 * @author suimi
 * @date 2019/12/12
 */
@Slf4j @Service public class SignatureService implements ISignatureService {

    @Autowired BlockChainService bdService;
    @Autowired BusinessDefineDao businessDefineDao;

    @Override public String generateSignature(BaseTxVO vo) throws DappException {
        String execPolicyId;
        BusinessDefinePO po = businessDefineDao.queryBDByCode(vo.getBdCode());
        log.info("[generateSignature]query BD from database:{}",po);
        BusinessDefine bd = BeanConvertor.convertBean(po, BusinessDefine.class);
        if (bd != null && StringUtils.isNotEmpty(po.getFunctions())) {
            List<FunctionDefine> functionDefines = JSON.parseArray(po.getFunctions(), FunctionDefine.class);
            bd.setFunctions(functionDefines);
        }
        if (CREATE_CONTRACT.getFunctionName().equals(vo.getFunctionName())) {
            execPolicyId = bd.getInitPolicy();
        } else {
            List<FunctionDefine> functions = JSONHelper.parseJSONOArray(bd.getFunctions()).flatMap(
                arr -> JSONHelper.toJavaList(arr, FunctionDefine.class)).orElseThrow(newError(DAPP_COMMON_ERROR));
            Optional<FunctionDefine> define = functions.stream().filter(a -> a.getName().equals(vo.getFunctionName()))
                .findFirst();
            //check function
            if (!define.isPresent()) {
                log.warn("function define is not find,functionName:{},txId:{}", vo.getFunctionName(), vo.getTxId());
                throw new DappException(FUNCTION_NOT_FIND_ERROR);
            }
            FunctionDefine fd = define.get();
            execPolicyId = fd.getExecPolicy();
        }
        vo.setExecPolicyId(execPolicyId);
        return vo.getSignValue();
    }
}
