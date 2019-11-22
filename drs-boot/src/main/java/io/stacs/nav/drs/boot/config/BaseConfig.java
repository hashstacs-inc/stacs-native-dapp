package io.stacs.nav.drs.boot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuyu
 * @description
 * @date 2019-11-22
 */
@Configuration @Getter @Setter public class BaseConfig {
    @Value("${server.port:8080}") private int serverPort;
}
