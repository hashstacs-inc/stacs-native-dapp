package io.stacs.nav.dapp.core.upgrade;

import io.stacs.nav.drs.api.model.UpgradeVersion;

/**
 * @author liuyu
 * @description
 * @date 2020-02-15
 */
public interface IDappUpgradeHanlder {
    /**
     * should upgrade
     *
     * @param upgradeVersion
     * @return
     */
    default boolean shouldUpgrade(UpgradeVersion upgradeVersion) {
        return true;
    }
}
