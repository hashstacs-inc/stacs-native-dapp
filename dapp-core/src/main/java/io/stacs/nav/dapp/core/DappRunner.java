package io.stacs.nav.dapp.core;

import io.stacs.nav.dapp.core.upgrade.UpgradeProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author liuyu
 * @description
 * @date 2020-02-14
 */
@Component @Slf4j public class DappRunner implements ApplicationRunner {
    @Autowired UpgradeProcessor upgradeProcessor;
    @Override public void run(ApplicationArguments args) throws Exception {
        log.info("dapp runner is invoke");
        upgradeProcessor.procss();
    }
}
