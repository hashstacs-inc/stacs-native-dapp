package io.stacs.nav.drs.api;

import io.stacs.nav.drs.api.model.UpgradeVersion;

/**
 * @author liuyu
 * @description
 * @date 2020-02-14
 */
public interface IUpgradeService {
    /**
     * check can upgrade of dapp
     *
     * @param appName
     * @return
     */
    default boolean canUpgrade(String appName){
        return false;
    }

    /**
     * return UpgradeVersion of upgrade
     *
     * @param appName
     * @return
     */
    UpgradeVersion getUpgradeVersion(String appName);
}
