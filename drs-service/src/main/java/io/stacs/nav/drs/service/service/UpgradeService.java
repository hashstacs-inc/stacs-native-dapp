package io.stacs.nav.drs.service.service;

import com.alipay.sofa.ark.container.registry.PluginServiceProvider;
import com.alipay.sofa.ark.spi.model.Plugin;
import com.alipay.sofa.ark.spi.service.ArkInject;
import com.alipay.sofa.ark.spi.service.plugin.PluginManagerService;
import com.alipay.sofa.ark.spi.service.registry.RegistryService;
import io.stacs.nav.drs.api.IUpgradeService;
import io.stacs.nav.drs.api.model.UpgradeVersion;
import io.stacs.nav.drs.service.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuyu
 * @description
 * @date 2020-02-14
 */
@Service @Slf4j public class UpgradeService implements IUpgradeService, InitializingBean {
    @ArkInject PluginManagerService pluginManagerService;
    @ArkInject RegistryService registryService;

    @Override public void afterPropertiesSet() throws Exception {
        Plugin plugin = pluginManagerService.getPluginByName(Constants.SERVICE_NAME);
        if (plugin == null) {
            log.warn("init plugin is fail,get plugin is null,name:{}", Constants.SERVICE_NAME);
            return;
        }
        registryService.publishService(IUpgradeService.class, this, new PluginServiceProvider(plugin));
    }
    /**
     * hold is need upgrade message
     */
    private Map<String, UpgradeVersion> UPGRADE_HOLDER = new ConcurrentHashMap<>();

    /**
     * can need upgrade for dapp
     *
     * @param appName
     * @return
     */
    @Override public boolean canUpgrade(String appName) {
        return UPGRADE_HOLDER.containsKey(appName);
    }

    /**
     * return UpgradeVersion
     *
     * @param appName
     * @return
     */
    @Override public UpgradeVersion getUpgradeVersion(String appName) {
        return UPGRADE_HOLDER.get(appName);
    }

    /**
     * need upgrade for dapp
     *
     * @param appName
     * @param upgradeVersion
     */
    public void needUpgrade(String appName, UpgradeVersion upgradeVersion) {
        UPGRADE_HOLDER.put(appName, upgradeVersion);
    }

    /**
     * cancel upgrade
     *
     * @param appName
     */
    public void cancelUpgrade(String appName) {
        UPGRADE_HOLDER.remove(appName);
    }
}
