package io.stacs.nav.drs.service.service;

import io.stacs.nav.drs.api.IUpgradeService;
import io.stacs.nav.drs.api.model.UpgradeVersion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuyu
 * @description
 * @date 2020-02-14
 */
@Service @Slf4j public class UpgradeService implements IUpgradeService {

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
