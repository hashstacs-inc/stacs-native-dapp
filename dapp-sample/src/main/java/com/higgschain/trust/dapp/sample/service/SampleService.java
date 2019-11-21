package com.higgschain.trust.dapp.sample.service;

import com.alipay.sofa.ark.spi.service.ArkInject;
import com.alipay.sofa.ark.spi.service.registry.RegistryService;
import com.higgschain.trust.drs.api.IDappApiService;
import com.higgschain.trust.drs.api.exception.DappException;
import com.higgschain.trust.drs.api.model.SampleRequest;
import com.higgschain.trust.drs.api.model.SampleResult;
import com.higgschain.trust.drs.api.model.permission.AuthPermissionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author suimi
 * @date 2019/10/30
 */
@Service @Slf4j public class SampleService {

    @ArkInject IDappApiService dappService;

    @ArkInject RegistryService registryService;

    public SampleResult service(SampleRequest request){
        log.info("dapp service is called...");
        log.info("registryService:{}",registryService);
        log.info("registryService.classLoader:{}",registryService.getClass().getClassLoader());
        log.info("classLoader--->",IDappApiService.class.getClassLoader());
//        ServiceFilter<IDappApiService> serviceFilter = new ServiceFilter<IDappApiService>() {
//
//            @Override public boolean match(ServiceReference serviceReference) {
//                //log.info("==>{}",serviceReference.getServiceMetadata());
//                log.info("{}=={}",serviceReference.getServiceMetadata().getServiceName(),IDappApiService.class.getCanonicalName());
//                boolean r = serviceReference.getServiceMetadata().getServiceName().equals(IDappApiService.class.getCanonicalName());
//                return r;
//            }
//        };
//
//        List<ServiceReference<IDappApiService>> serviceReferences = registryService.referenceServices(serviceFilter);
//        log.info("--->{}",serviceReferences.get(0).getService().getClass().getClassLoader());
//        log.info("--->{}",this.getClass().getClassLoader());
        try {
            dappService.authPermission(new AuthPermissionVO());
        }catch (DappException e){
            log.error("aaaa-->",e);
        }
        return dappService.service(request);
    }
}
