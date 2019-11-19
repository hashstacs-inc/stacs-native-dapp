package com.higgschain.trust.drs.service.service;

import com.alipay.sofa.ark.container.registry.PluginServiceProvider;
import com.alipay.sofa.ark.spi.model.Plugin;
import com.alipay.sofa.ark.spi.service.ArkInject;
import com.alipay.sofa.ark.spi.service.plugin.PluginManagerService;
import com.alipay.sofa.ark.spi.service.registry.RegistryService;
import com.higgschain.trust.drs.api.IDappApiService;
import com.higgschain.trust.drs.exception.DappException;
import com.higgschain.trust.drs.model.SampleRequest;
import com.higgschain.trust.drs.model.SampleResult;
import com.higgschain.trust.drs.model.bd.BusinessDefine;
import com.higgschain.trust.drs.service.constant.Constants;
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

    @Override public SampleResult service(SampleRequest request) {
        log.info("request:{}", request);
        testService.test();
        return new SampleResult(true);
    }

    @Override public void publishBD(BusinessDefine bd) throws DappException {
        requestService.submitTx(bd);
    }

    @Override public void afterPropertiesSet() {
        Plugin plugin = pluginManagerService.getPluginByName(Constants.SERVICE_NAME);
        if (plugin == null) {
            log.warn("init plugin is fail,get plugin is null,name:{}", Constants.SERVICE_NAME);
            return;
        }
        registryService.publishService(IDappApiService.class, this, new PluginServiceProvider(plugin));
        log.info("published service:{}", IDappApiService.class);
    }
}
