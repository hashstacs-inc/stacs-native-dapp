package io.stacs.nav.drs.service.service;

import com.alipay.sofa.ark.container.registry.PluginServiceProvider;
import com.alipay.sofa.ark.spi.model.Plugin;
import com.alipay.sofa.ark.spi.service.ArkInject;
import com.alipay.sofa.ark.spi.service.plugin.PluginManagerService;
import com.alipay.sofa.ark.spi.service.registry.RegistryService;
import io.stacs.nav.drs.api.IDappApiService;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.SampleRequest;
import io.stacs.nav.drs.api.model.SampleResult;
import io.stacs.nav.drs.api.model.attestation.SaveAttestationVO;
import io.stacs.nav.drs.api.model.bd.BusinessDefine;
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

/**
 * @author suimi
 * @date 2019/10/30
 */
@Slf4j @Service public class DappApiService implements IDappApiService, InitializingBean {

    @ArkInject RegistryService registryService;

    @ArkInject PluginManagerService pluginManagerService;

    @Autowired TestService testService;
    @Autowired TxRequestService requestService;

    //todo 反射动态代理优化

    @Override public SampleResult service(SampleRequest request) {
        log.info("request:{}", request);
        testService.test();
        return new SampleResult(true);
    }

    @Override public void publishBD(BusinessDefine bd) throws DappException {
        requestService.submitTx(bd);
    }

    @Override public void contractPublish(ContractCreateVO vo) throws DappException {
        requestService.submitTx(vo);
    }

    @Override public void contractInvoke(ContractInvokeVO vo) throws DappException {
        requestService.submitTx(vo);
    }

    @Override public void registPolicy(RegisterPolicyVO vo) throws DappException {
        requestService.submitTx(vo);
    }

    @Override public void modifyPolicy(ModifyPolicyVO vo) throws DappException {
        requestService.submitTx(vo);
    }

    @Override public void registerPermission(RegisterPermissionVO vo) throws DappException {
        requestService.submitTx(vo);
    }

    @Override public void authPermission(AuthPermissionVO vo) throws DappException {
        requestService.submitTx(vo);
    }

    @Override public void cancelPermission(CancelPermissionVO vo) throws DappException {
        requestService.submitTx(vo);
    }

    @Override public void identitySetting(IdentitySettingVO vo) throws DappException {
        requestService.submitTx(vo);
    }

    @Override public void identityManager(IdentityBDManageVO vo) throws DappException {
        requestService.submitTx(vo);
    }

    @Override public void settingKYC(KYCSettingVO vo) throws DappException {
        requestService.submitTx(vo);
    }

    @Override public void feeTxRuleConfig(FeeTxRuleConfigVO vo) throws DappException {
        requestService.submitTx(vo);
    }

    @Override public void systemPropertyConfig(SystemPropertyConfigVO vo) throws DappException {
        requestService.submitTx(vo);
    }

    @Override public void buildSnapshot(BuildSnapshotVO vo) throws DappException {
        requestService.submitTx(vo);
    }

    @Override public void saveAttestation(SaveAttestationVO vo) throws DappException {
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
