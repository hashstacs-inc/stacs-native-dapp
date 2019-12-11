package io.stacs.nav.drs.service.vo.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author suimi
 * @date 2019/10/31
 */
@Getter @Setter
public class DomainConfigVO{
    private String baseUrl;
    private String chainPubKey;
    private String merchantPriKey;
    private String aesKey;
    private String merchantId;
    private String callbackUrl;
}
