package com.higgschain.trust.drs.service.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author suimi
 * @date 2019/10/31
 */
@NoArgsConstructor @DynamicConfig @ConfigurationProperties(prefix = "drs.domain") @Getter @Setter
public class DomainConfig {

    private String baseUrl;
    private String chainPubKey;
    private String merchantPriKey;
    private String aesKey;
    private String merchantId;
    private String callbackUrl;

    public DomainConfig baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public DomainConfig chainPubKey(String chainPubKey) {
        this.chainPubKey = chainPubKey;
        return this;
    }

    public DomainConfig merchantPriKey(String merchantPriKey) {
        this.merchantPriKey = merchantPriKey;
        return this;
    }

    public DomainConfig aesKey(String aesKey) {
        this.aesKey = aesKey;
        return this;
    }

    public DomainConfig merchantId(String merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    public DomainConfig callbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
        return this;
    }
}
