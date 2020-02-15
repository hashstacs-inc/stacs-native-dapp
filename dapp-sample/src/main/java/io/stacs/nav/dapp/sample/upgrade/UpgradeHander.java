package io.stacs.nav.dapp.sample.upgrade;

import io.stacs.nav.dapp.core.upgrade.IDappUpgradeHanlder;
import io.stacs.nav.drs.api.model.UpgradeVersion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author liuyu
 * @description
 * @date 2020-02-15
 */
@Component @Slf4j public class UpgradeHander implements IDappUpgradeHanlder {
    /**
     * should upgrade
     *
     * @param upgradeVersion
     * @return
     */
    @Override public boolean shouldUpgrade(UpgradeVersion upgradeVersion) {
        log.info("[shouldUpgrade]upgradeVersion:{}", upgradeVersion);
        return true;
    }
}
