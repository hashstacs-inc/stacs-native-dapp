package com.higgschain.trust.dapp.sample.service;

import com.alipay.sofa.ark.spi.service.ArkInject;
import com.higgschain.trust.drs.api.IDappService;
import com.higgschain.trust.drs.model.SampleRequest;
import com.higgschain.trust.drs.model.SampleResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author suimi
 * @date 2019/10/30
 */
@Service @Slf4j public class SampleService {
    @ArkInject IDappService dappService;

    public SampleResult service(SampleRequest request){
        log.info("dapp service is called...");
        return dappService.service(request);
    }
}
