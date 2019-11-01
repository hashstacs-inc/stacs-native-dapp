package com.higgschain.trust.drs.boot.service;

import com.alipay.sofa.ark.api.ArkClient;
import com.alipay.sofa.ark.api.ClientResponse;
import com.alipay.sofa.ark.api.ResponseCode;
import com.alipay.sofa.ark.loader.JarBizArchive;
import com.alipay.sofa.ark.loader.archive.JarFileArchive;
import com.alipay.sofa.ark.loader.jar.JarFile;
import com.higgschain.trust.drs.boot.config.DrsConfig;
import com.higgschain.trust.drs.boot.service.dapp.Dapp;
import com.higgschain.trust.drs.boot.service.dapp.DappStatus;
import com.higgschain.trust.drs.boot.service.dapp.IDappService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.stream.Collectors;

import static com.alipay.sofa.ark.spi.constant.Constants.*;

/**
 * @author suimi
 * @date 2019/10/31
 */
@Slf4j @Service public class DappLifecycleManage implements IDappLifecycleManage {

    @Autowired DrsConfig drsConfig;

    @Autowired IDappService dappService;

    @Override public Dapp download(String urlPath) throws IOException {
        URL url = new URL(urlPath);
        String fileName = FilenameUtils.getName(urlPath);
        File destination = new File(drsConfig.getDownloadPath(), fileName);
        Dapp info = dappService.findByFileName(fileName);
        if (!destination.exists()) {
            FileUtils.copyURLToFile(url, destination);
        }
        if (info == null) {
            info = getInfo(destination);
        }
        info.setStatus(DappStatus.Download);
        return dappService.save(info);

    }

    private Dapp getInfo(File file) throws IOException {
        JarFile bizFile = new JarFile(file);
        JarFileArchive jarFileArchive = new JarFileArchive(bizFile);
        JarBizArchive bizArchive = new JarBizArchive(jarFileArchive);
        Dapp dapp = new Dapp();
        Attributes manifestMainAttributes = bizArchive.getManifest().getMainAttributes();
        dapp.setFileName(file.getName());
        dapp.setName(manifestMainAttributes.getValue(ARK_BIZ_NAME));
        dapp.setVersion(manifestMainAttributes.getValue(ARK_BIZ_VERSION));
        dapp.setContextPath(manifestMainAttributes.getValue(WEB_CONTEXT_PATH));
        return dapp;
    }

    @Override public void config(Long dappId, Map<String, String> config) {
        dappService.configDapp(dappId, config);
    }

    @Override public boolean install(String fileName) {
        try {
            Dapp dapp = dappService.findByFileName(fileName);
            if (dapp == null) {
                throw new RuntimeException("dapp not download");
            }
            File dappFile = new File(drsConfig.getDownloadPath(), dapp.getFileName());
            Dapp info = getInfo(dappFile);
            Map<String, String> config = dappService.getConfig(info.getId());
            log.info("install dapp:{}, with config:{}", info, config);
            List<String> params = new ArrayList<>();
            config.entrySet().forEach(e -> params.add(e.getKey() + "=" + e.getValue()));
            ClientResponse response = ArkClient.installBiz(dappFile, params.toArray(new String[0]));
            dapp.setStatus(DappStatus.Installed);
            dappService.update(dapp);
            return ResponseCode.SUCCESS.equals(response.getCode());
        } catch (Throwable e) {
            log.error("dapp install is failed", e);
            return false;
        }
    }

    @Override public boolean unInstall(String dappName, String version) {
        try {
            Dapp dapp = dappService.findByName(dappName, version);
            ClientResponse response = ArkClient.uninstallBiz(dappName, version);
            if (dapp != null) {
                dapp.setStatus(DappStatus.Uninstalled);
                dappService.update(dapp);
            }
            return ResponseCode.SUCCESS.equals(response.getCode());
        } catch (Throwable e) {
            log.error("dapp uninstall is failed", e);
            return false;
        }
    }

    @Override public List<Dapp> list() {
        return dappService.findAll();
    }

}
