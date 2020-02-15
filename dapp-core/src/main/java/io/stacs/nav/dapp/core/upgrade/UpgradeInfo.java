package io.stacs.nav.dapp.core.upgrade;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.core.io.Resource;

/**
 * @author liuyu
 * @description
 * @date 2020-02-14
 */
@Getter @Setter @ToString public class UpgradeInfo {
    /**
     * version code
     */
    private int versionCode;
    /**
     * sql resource
     */
    private Resource resource;
}
