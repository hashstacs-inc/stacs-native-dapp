package com.higgschain.trust.drs.service.config;

import com.alipay.sofa.ark.springboot.runner.ArkBootRunner;
import com.higgschain.trust.drs.ConfigWithoutDataSource;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

/**
 * @author dekuofa <br>
 * @date 2019-11-18 <br>
 */
@FixMethodOrder(value = NAME_ASCENDING) @RunWith(ArkBootRunner.class)
@SpringBootTest(classes = ConfigWithoutDataSource.class) public class ConfigurationManagerTest {
    @Autowired ConfigurationManager manager;

    @Test public void load_test() {
        DomainConfig config = manager.getConfigByClass(DomainConfig.class);
        assertEquals("http://localhost:7070/", config.getBaseUrl());
    }

    @Test public void update_test() {
        DomainConfig config = manager.getConfigByClass(DomainConfig.class);
        assertEquals("http://localhost:7070/", config.getBaseUrl());
        config = new DomainConfig().aesKey("testAesKey").baseUrl("baseUrlTest").callbackUrl("callBackTest")
            .chainPubKey("pubKey").merchantId("test_merchantId").merchantPriKey("private_key");
        manager.updateConfig(config);
        config = manager.getConfigByClass(DomainConfig.class);
        assertEquals("baseUrlTest", config.getBaseUrl());
        assertEquals("test_merchantId", config.getMerchantId());
        assertEquals("testAesKey", config.getAesKey());
        assertEquals("callBackTest", config.getCallbackUrl());
        assertEquals("pubKey", config.getChainPubKey());
        assertEquals("private_key", config.getMerchantPriKey());
    }

}
