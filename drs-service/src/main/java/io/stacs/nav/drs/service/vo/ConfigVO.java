package io.stacs.nav.drs.service.vo;

import io.stacs.nav.drs.service.config.DomainConfig;
import io.stacs.nav.drs.service.config.DrsConfig;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyu
 * @description
 * @date 2019-12-11
 */
@Getter @Setter public class ConfigVO{
    private DomainConfig domainConfig;
    private DrsConfig drsConfig;
}
