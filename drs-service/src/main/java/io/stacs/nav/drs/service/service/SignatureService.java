package io.stacs.nav.drs.service.service;

import com.alibaba.fastjson.JSON;
import com.alipay.sofa.ark.container.registry.PluginServiceProvider;
import com.alipay.sofa.ark.spi.model.Plugin;
import com.alipay.sofa.ark.spi.service.ArkInject;
import com.alipay.sofa.ark.spi.service.plugin.PluginManagerService;
import com.alipay.sofa.ark.spi.service.registry.RegistryService;
import io.stacs.nav.drs.api.IQueryService;
import io.stacs.nav.drs.api.ISignatureService;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.BaseTxVO;
import io.stacs.nav.drs.api.model.bd.BusinessDefine;
import io.stacs.nav.drs.api.model.bd.FunctionDefine;
import io.stacs.nav.drs.service.constant.Constants;
import io.stacs.nav.drs.service.dao.BusinessDefineDao;
import io.stacs.nav.drs.service.dao.po.BusinessDefinePO;
import io.stacs.nav.drs.service.utils.BeanConvertor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static io.stacs.nav.drs.api.enums.ApiConstants.TransactionApiEnum.CREATE_CONTRACT;
import static io.stacs.nav.drs.api.exception.DappError.BD_NOT_FIND_ERROR;
import static io.stacs.nav.drs.api.exception.DappError.FUNCTION_NOT_FIND_ERROR;

/**
 * @author suimi
 * @date 2019/12/12
 */
@Slf4j @Service public class SignatureService implements ISignatureService, InitializingBean {

    @Autowired BlockChainService bdService;
    @Autowired BusinessDefineDao businessDefineDao;

    @ArkInject PluginManagerService pluginManagerService;

    @ArkInject RegistryService registryService;

    @Override public String generateSignature(BaseTxVO vo) throws DappException {
        String execPolicyId;
        BusinessDefinePO po = businessDefineDao.queryBDByCode(vo.getBdCode());
        log.info("[generateSignature]query BD from database:{}",JSON.toJSONString(po));
        if(po == null){
            log.warn("[generateSignature]bd is not exists,bdCode:{}",vo.getBdCode());
            throw new DappException(BD_NOT_FIND_ERROR);
        }
        BusinessDefine bd = BeanConvertor.convertBean(po, BusinessDefine.class);
        if (bd != null && StringUtils.isNotEmpty(po.getFunctions())) {
            List<FunctionDefine> functionDefines = JSON.parseArray(po.getFunctions(), FunctionDefine.class);
            bd.setFunctions(functionDefines);
        }
        if (CREATE_CONTRACT.getFunctionName().equals(vo.getFunctionName())) {
            execPolicyId = bd.getInitPolicy();
        } else {
            List<FunctionDefine> functions = bd.getFunctions();
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

    // @formatter:off
    @Override public void afterPropertiesSet() {
        Plugin plugin = pluginManagerService.getPluginByName(Constants.SERVICE_NAME);
        if (plugin == null) {
            log.warn("init plugin is fail,get plugin is null,name:{}", Constants.SERVICE_NAME);
            return;
        }
        registryService.publishService(
            ISignatureService.class,
            this,
            new PluginServiceProvider(plugin));
    }
}
