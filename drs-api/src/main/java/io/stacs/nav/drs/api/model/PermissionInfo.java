package io.stacs.nav.drs.api.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyu
 * @description
 * @date 2019-11-18
 */
@Getter @Setter public class PermissionInfo {
    /**
     * index of permission , increments by 1 <br/>
     * 系统 permission，idx 范围：0~20，idx由大到小使用<br/>
     * 普通 permission，从 21 开始，递增使用
     */
    private Integer permissionIndex;

    private String permissionName;
}
