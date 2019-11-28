package io.stacs.nav.drs.service.service;

import com.alipay.sofa.ark.container.registry.PluginServiceProvider;
import com.alipay.sofa.ark.spi.model.Plugin;
import com.alipay.sofa.ark.spi.service.ArkInject;
import com.alipay.sofa.ark.spi.service.plugin.PluginManagerService;
import com.alipay.sofa.ark.spi.service.registry.RegistryService;
import io.stacs.nav.drs.api.IDappApiService;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.BaseTxVO;
import io.stacs.nav.drs.api.model.SampleRequest;
import io.stacs.nav.drs.api.model.SampleResult;
import io.stacs.nav.drs.api.model.attestation.SaveAttestationVO;
import io.stacs.nav.drs.api.model.bd.BusinessDefine;
import io.stacs.nav.drs.api.model.bd.FunctionDefine;
import io.stacs.nav.drs.api.model.contract.ContractCreateVO;
import io.stacs.nav.drs.api.model.contract.ContractInvokeVO;
import io.stacs.nav.drs.api.model.fee.FeeTxRuleConfigVO;
import io.stacs.nav.drs.api.model.identity.IdentityBDManageVO;
import io.stacs.nav.drs.api.model.identity.IdentitySettingVO;
import io.stacs.nav.drs.api.model.identity.KYCSettingVO;
import io.stacs.nav.drs.api.model.permission.AuthPermissionVO;
import io.stacs.nav.drs.api.model.permission.CancelPermissionVO;
import io.stacs.nav.drs.api.model.permission.RegisterPermissionVO;
import io.stacs.nav.drs.api.model.policy.ModifyPolicyVO;
import io.stacs.nav.drs.api.model.policy.RegisterPolicyVO;
import io.stacs.nav.drs.api.model.property.SystemPropertyConfigVO;
import io.stacs.nav.drs.api.model.snapshot.BuildSnapshotVO;
import io.stacs.nav.drs.service.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static io.stacs.nav.drs.api.enums.ApiConstants.TransactionApiEnum.CREATE_CONTRACT;
import static io.stacs.nav.drs.api.exception.DappError.FUNCTION_NOT_FIND_ERROR;

/**
 * @author suimi
 * @date 2019/10/30
 */
@Slf4j @Service public class DappApiService implements IDappApiService, InitializingBean {

    @ArkInject RegistryService registryService;

    @ArkInject PluginManagerService pluginManagerService;

    @Autowired TestService testService;
    @Autowired TxRequestService requestService;
    @Autowired BDService bdService;

    @Override public SampleResult service(SampleRequest request) {
        log.info("request:{}", request);
        testService.test();
        return new SampleResult(true);
    }

    @Override public String getSignValue(BaseTxVO vo) {
        String execPolicyId;
        BusinessDefine bd = bdService.queryBDByCode(vo.getBdCode());
        if (CREATE_CONTRACT.getFunctionName().equals(vo.getFunctionName())) {
            execPolicyId = bd.getInitPolicy();
        } else {
            List<FunctionDefine> functions = bd.getFunctions();
            Optional<FunctionDefine> define =
                functions.stream().filter(a -> a.getName().equals(vo.getFunctionName())).findFirst();
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

    @Override public void publishBD(BusinessDefine bd) {
        requestService.submitTx(bd);
    }

    @Override public void contractPublish(ContractCreateVO vo) {
        requestService.submitTx(vo);
    }

    @Override public void contractInvoke(ContractInvokeVO vo) {
        requestService.submitTx(vo);
    }

    @Override public void registerPolicy(RegisterPolicyVO vo) {
        requestService.submitTx(vo);
    }

    @Override public void modifyPolicy(ModifyPolicyVO vo) {
        requestService.submitTx(vo);
    }

    @Override public void registerPermission(RegisterPermissionVO vo) {
        requestService.submitTx(vo);
    }

    @Override public void authPermission(AuthPermissionVO vo) {
        requestService.submitTx(vo);
    }

    @Override public void cancelPermission(CancelPermissionVO vo) {
        requestService.submitTx(vo);
    }

    @Override public void identitySetting(IdentitySettingVO vo) {
        requestService.submitTx(vo);
    }

    @Override public void identityManager(IdentityBDManageVO vo) {
        requestService.submitTx(vo);
    }

    @Override public void settingKYC(KYCSettingVO vo) {
        requestService.submitTx(vo);
    }

    @Override public void feeTxRuleConfig(FeeTxRuleConfigVO vo) {
        requestService.submitTx(vo);
    }

    @Override public void systemPropertyConfig(SystemPropertyConfigVO vo) {
        requestService.submitTx(vo);
    }

    @Override public void buildSnapshot(BuildSnapshotVO vo) {
        requestService.submitTx(vo);
    }

    @Override public void saveAttestation(SaveAttestationVO vo) {
        requestService.submitTx(vo);
    }

    @Override public void afterPropertiesSet() {
        Plugin plugin = pluginManagerService.getPluginByName(Constants.SERVICE_NAME);
        if (plugin == null) {
            log.warn("init plugin is fail,get plugin is null,name:{}", Constants.SERVICE_NAME);
            return;
        }
        registryService.publishService(IDappApiService.class, this, new PluginServiceProvider(plugin));
    }
}
