package io.stacs.nav.drs.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liuyu
 * @description
 * @date 2020-02-15
 */
@Getter @Setter @ToString public class UpgradeVersion {
    /**
     * the original version code
     */
    private int originalVersionCode;
    /**
     * To upgrade the version code
     */
    private int versionCode;
    /**
     * To upgrade the version name
     */
    private String version;

    /**
     * return new object
     *
     * @param originalVersionCode
     * @param versionCode
     * @param version
     * @return
     */
    public static UpgradeVersion of(int originalVersionCode, int versionCode, String version) {
        UpgradeVersion upgradeVersion = new UpgradeVersion();
        upgradeVersion.setOriginalVersionCode(originalVersionCode);
        upgradeVersion.setVersionCode(versionCode);
        upgradeVersion.setVersion(version);
        return upgradeVersion;
    }
}
