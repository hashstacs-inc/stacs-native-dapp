package io.stacs.nav.drs.boot.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyu
 * @description
 * @date 2019-12-05
 */
@Getter @Setter public class AppProfileVO {
    /**
     * the app unique name
     */
    private String name;
    /**
     * the app show name
     */
    private String showName;
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
     * app download url
     */
    private String downloadUrl;
    /**
     * NULL,DOWNLOADED,INITIALIZED,RUNNING,STOPPED
     */
    private String status;
}
