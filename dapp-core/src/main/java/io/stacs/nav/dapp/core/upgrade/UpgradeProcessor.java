package io.stacs.nav.dapp.core.upgrade;

import com.alipay.sofa.ark.spi.service.ArkInject;
import io.stacs.nav.drs.api.IUpgradeService;
import io.stacs.nav.drs.api.model.UpgradeVersion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author liuyu
 * @description
 * @date 2020-02-14
 */
@Component @Slf4j public class UpgradeProcessor {
    @Value("${spring.application.name}") private String appName;
    @ArkInject IUpgradeService upgradeService;
    @Autowired IDappUpgradeHanlder dappUpgradeHanlder;
    @Autowired UpgradeDDLHandler upgradeDDLHandler;
    @Autowired UpgradeDMLHandler upgradeDMLHandler;

    /**
     * process upgrade
     */
    public void procss() throws IOException {
        // check can upgrade from drs
        if (upgradeService == null || !upgradeService.canUpgrade(appName)) {
            log.info("[process]Don't need upgrade,appName:{}", appName);
            return;
        }
        UpgradeVersion upgradeVersion = upgradeService.getUpgradeVersion(appName);
        if (upgradeVersion == null) {
            log.info("[process]get upgradeVersion is null,appName:{}", appName);
            return;
        }
        //check from dapp
        if (dappUpgradeHanlder != null && !dappUpgradeHanlder.shouldUpgrade(upgradeVersion)) {
            log.info("[process]Don't need upgrade,because shouldUpgrade return false,appName:{}", appName);
            return;
        }
        int originalVersionCode = upgradeVersion.getOriginalVersionCode();
        log.info("[process]originalVersionCode:{}", originalVersionCode);
        upgradeDDLHandler.handler(originalVersionCode);
        upgradeDMLHandler.handler(originalVersionCode);
    }
}
