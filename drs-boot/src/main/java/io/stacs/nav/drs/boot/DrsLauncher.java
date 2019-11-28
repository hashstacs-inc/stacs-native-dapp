package io.stacs.nav.drs.boot;

import com.alipay.sofa.ark.bootstrap.ArkLauncher;
import com.alipay.sofa.ark.spi.archive.ExecutableArchive;

/**
 * @author dekuofa <br>
 * @date 2019-11-27 <br>
 */
public class DrsLauncher {

    // todo 替换 arkLauncher

    private ArkLauncher launcher;

    public DrsLauncher() {
        launcher = new ArkLauncher();
    }

    public DrsLauncher(ExecutableArchive executableArchive) {
        launcher = new ArkLauncher(executableArchive);
    }

    public static void main(String[] args) throws Exception {
        String configPath = "--drs.spring.config.location=";

        (new ArkLauncher()).launch(args);
    }
}
