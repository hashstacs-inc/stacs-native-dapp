package io.stacs.nav.drs.boot.bo;

import io.stacs.nav.drs.boot.enums.DappStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author suimi
 * @date 2019/10/30
 */
@Getter @Setter @ToString public class Dapp {
    /**
     * the app name
     */
    private String name;
    /**
     * the app version
     */
    private String version;
    /**
     * context path for web app
     */
    private String contextPath;
    /**
     * status,DOWNLOAD、INITIALIZED、RUNNING、STOPPED
     */
    private DappStatus status;
    /**
     * app run error info
     */
    private String runError;
    /**
     * app jar file name
     */
    private String fileName;
    /**
     * app icon url
     */
    private String icon;
    /**
     * author of app
     */
    private String author;
    /**
     * the app description
     */
    private String remark;
    /**
     * version code for upgrade
     */
    private int versionCode;
}
