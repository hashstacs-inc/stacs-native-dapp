package com.higgschain.trust.drs.service.service;

import com.alipay.sofa.ark.container.registry.PluginServiceProvider;
import com.alipay.sofa.ark.spi.service.ArkInject;
import com.alipay.sofa.ark.spi.service.plugin.PluginManagerService;
import com.alipay.sofa.ark.spi.service.registry.RegistryService;
import com.higgschain.trust.drs.api.IDappService;
import com.higgschain.trust.drs.model.SampleRequest;
import com.higgschain.trust.drs.model.SampleResult;
import com.higgschain.trust.drs.service.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author suimi
 * @date 2019/10/30
 */
@Slf4j @Service public class DappService implements IDappService, InitializingBean {

    @ArkInject RegistryService registryService;

    @ArkInject PluginManagerService pluginManagerService;

    @Autowired TestService testService;

    @Override public SampleResult service(SampleRequest request) {
        log.info("request:{}", request);
        testService.test();
        return new SampleResult(true);
    }

    @Override public void afterPropertiesSet() {
        registryService.publishService(IDappService.class, this,
            new PluginServiceProvider(pluginManagerService.getPluginByName(Constants.SERVICE_NAME)));
        log.info("published service:{}", IDappService.class);
    }
}
