package com.higgschain.trust.drs.service;

import com.alipay.sofa.ark.spi.model.PluginContext;
import com.alipay.sofa.ark.spi.service.PluginActivator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author suimi
 * @date 2019/10/30
 */
@Slf4j public class ServiceActivator implements PluginActivator {
    @Override public void start(PluginContext pluginContext) {
        log.info("Starting {} service...", this.getClass().getSimpleName());
    }

    @Override public void stop(PluginContext pluginContext) {
        log.info("Stopping {} service...", this.getClass().getSimpleName());
    }
}
