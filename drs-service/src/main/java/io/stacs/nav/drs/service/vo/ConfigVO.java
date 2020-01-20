package io.stacs.nav.drs.service.vo;

import io.stacs.nav.drs.service.vo.config.DomainConfigVO;
import io.stacs.nav.drs.service.vo.config.DrsConfigVO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyu
 * @description
 * @date 2019-12-11
 */
@Getter @Setter public class ConfigVO implements java.io.Serializable{
    private DomainConfigVO domainConfig;
    private DrsConfigVO drsConfig;
}
