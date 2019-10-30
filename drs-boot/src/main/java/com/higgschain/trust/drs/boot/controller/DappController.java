package com.higgschain.trust.drs.boot.controller;

import com.alipay.sofa.ark.api.ArkClient;
import com.alipay.sofa.ark.api.ClientResponse;
import com.alipay.sofa.ark.api.ResponseCode;
import com.alipay.sofa.ark.loader.JarBizArchive;
import com.alipay.sofa.ark.loader.archive.JarFileArchive;
import com.alipay.sofa.ark.loader.jar.JarFile;
import com.alipay.sofa.ark.spi.model.Biz;
import com.alipay.sofa.ark.spi.service.ArkInject;
import com.alipay.sofa.ark.spi.service.biz.BizManagerService;
import com.higgschain.trust.drs.service.event.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.stream.Collectors;

import static com.alipay.sofa.ark.spi.constant.Constants.*;

/**
 * @author suimi
 * @date 2019/10/30
 */
@RestController @Slf4j @RequestMapping("/dapp") public class DappController {

    @ArkInject BizManagerService bizManagerService;

    /**
     * install dapp
     *
     * @return
     */
    @RequestMapping("/install") public ClientResponse install(String filePath) {
        log.info("start install dapp by filePath:{}", filePath);
        ClientResponse response = new ClientResponse();
        try {
            File dappFile = new File(filePath);
            DappInfo info = getInfo(dappFile);
            log.info("install dapp:{}", info);
            String[] args = new String[] {"logging.path=/" + info.getName()};
            response = ArkClient.installBiz(dappFile, args);
        } catch (Throwable e) {
            log.error("dapp install is failed", e);
            response.setCode(ResponseCode.FAILED);
            response.setMessage("install is failed,msg:" + e.getMessage());
        }
        return response;
    }

    private DappInfo getInfo(File file) throws IOException {
        JarFile bizFile = new JarFile(file);
        JarFileArchive jarFileArchive = new JarFileArchive(bizFile);
        JarBizArchive bizArchive = new JarBizArchive(jarFileArchive);
        DappInfo dappInfo = new DappInfo();
        Attributes manifestMainAttributes = bizArchive.getManifest().getMainAttributes();
        dappInfo.setName(manifestMainAttributes.getValue(ARK_BIZ_NAME));
        dappInfo.setVersion(manifestMainAttributes.getValue(ARK_BIZ_VERSION));
        dappInfo.setContextPath(manifestMainAttributes.getValue(WEB_CONTEXT_PATH));
        return dappInfo;
    }

    /**
     * install dapp
     *
     * @return
     */
    @GetMapping("/list") public List<DappInfo> list() {
        List<DappInfo> dapps = new ArrayList<>();
        Set<String> allBizNames = bizManagerService.getAllBizNames();
        for (String name : allBizNames) {
            List<Biz> bizList = bizManagerService.getBiz(name);
            List<DappInfo> collect = bizList.stream().map(
                biz -> new DappInfo(biz.getBizName(), biz.getBizVersion(), biz.getBizState().toString(),
                    biz.getWebContextPath())).collect(Collectors.toList());
            dapps.addAll(collect);
        }
        return dapps;
    }

    /**
     * uninstall dapp
     *
     * @return
     */
    @GetMapping("/uninstall/{serviceName}/{version}") public ClientResponse uninstall(
        @PathVariable("serviceName") String serviceName, @PathVariable("version") String version) {
        ClientResponse response = new ClientResponse();
        try {
            response = ArkClient.uninstallBiz(serviceName, version);
        } catch (Throwable e) {
            log.error("dapp install is failed", e);
            response.setCode(ResponseCode.FAILED);
            response.setMessage("install is failed,msg:" + e.getMessage());
        }
        return response;
    }

}
