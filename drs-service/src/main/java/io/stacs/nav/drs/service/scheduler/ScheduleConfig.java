package io.stacs.nav.drs.service.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(value = "scheduling.enable", havingValue = "true", matchIfMissing = true) @Configuration
@EnableScheduling @Service @Slf4j public class ScheduleConfig {

}
