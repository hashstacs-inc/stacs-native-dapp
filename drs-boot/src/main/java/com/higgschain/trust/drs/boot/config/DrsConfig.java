package com.higgschain.trust.drs.boot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author suimi
 * @date 2019/10/31
 */
@Configuration @ConfigurationProperties(prefix = "drs") @Getter @Setter public class DrsConfig {

    private String downloadPath;

    private String configPath;

    private String dappStorePath;
}
