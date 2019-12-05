package io.stacs.nav.drs.service.config;

import io.stacs.nav.drs.service.utils.config.DynamicConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author suimi
 * @date 2019/10/31
 */
@DynamicConfig @ConfigurationProperties(prefix = "drs") @Getter @Setter public class DrsConfig {

    private String downloadPath;

    private String configPath;

    private String dappStorePath;

}
