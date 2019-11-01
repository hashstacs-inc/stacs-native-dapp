package com.higgschain.trust.drs.boot.service.dapp;

import lombok.*;

/**
 * @author suimi
 * @date 2019/10/30
 */
@Getter @Setter @ToString public class Dapp {
    private long id;
    private String name;
    private String version;
    private String url;
    private String desc;
    private String fileName;
    private DappStatus status;
    private String contextPath;
}
