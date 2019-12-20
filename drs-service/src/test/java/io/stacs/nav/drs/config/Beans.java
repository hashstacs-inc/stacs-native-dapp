package io.stacs.nav.drs.config;

import io.stacs.nav.drs.api.ISubmitterService;
import io.stacs.nav.drs.service.service.SubmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dekuofa <br>
 * @date 2019-12-18 <br>
 */
@Configuration public class Beans {
    @Autowired SubmitterService submitterService;

    @ConditionalOnBean(value = SubmitterService.class) @Bean(name = "iSubmitterService")
    public ISubmitterService submitterService() {
        return submitterService.newInstance();
    }
}
