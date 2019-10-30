package com.higgschain.trust.drs.boot.controller;

import lombok.*;

/**
 * @author suimi
 * @date 2019/10/30
 */
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString public class DappInfo {
    private String name;
    private String version;
    private String status;
    private String contextPath;
}
