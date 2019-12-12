package io.stacs.nav.drs.boot.service;

import com.alipay.sofa.ark.api.ArkClient;
import com.alipay.sofa.ark.api.ClientResponse;
import com.alipay.sofa.ark.api.ResponseCode;
import com.alipay.sofa.ark.loader.JarBizArchive;
import com.alipay.sofa.ark.loader.archive.JarFileArchive;
import com.alipay.sofa.ark.loader.jar.JarEntry;
import com.alipay.sofa.ark.loader.jar.JarFile;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import io.stacs.nav.drs.api.exception.DappError;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.boot.bo.Dapp;
import io.stacs.nav.drs.boot.config.BaseConfig;
import io.stacs.nav.drs.boot.enums.DappStatus;
import io.stacs.nav.drs.boot.service.dapp.IDappService;
import io.stacs.nav.drs.boot.service.dappstore.DappStoreService;
import io.stacs.nav.drs.boot.vo.AppProfileVO;
import io.stacs.nav.drs.service.config.DrsConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import static com.alipay.sofa.ark.spi.constant.Constants.*;
import static io.stacs.nav.drs.service.constant.Constants.DRS_VERSION_KEY;
import static io.stacs.nav.drs.service.utils.ResourceLoader.getManifest;

/**
 * @author suimi
 * @date 2019/10/31
 */
@Slf4j @Service public class DappLifecycleManage
    implements IDappLifecycleManage, ApplicationListener<ApplicationReadyEvent> {
    /**
     * app config file name
     */
    private static final String DAPP_CONFIG_FILE_NAME = "application.properties";
    private static final String SPRING_APP_NAME = "spring.application.name";
    private static final String WEB_SERVER_PORT = "server.port";
    private static final String WEB_SERVER_CONTEXT_PATH = "server.servlet.context-path";

    @Autowired BaseConfig baseConfig;

    @Autowired DrsConfig drsConfig;

    @Autowired IDappService dappService;
    @Autowired DappStoreService dappStoreService;

    @Override public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        init();
    }

    /**
     * init
     */
    private void init() {
        List<Dapp> dappList = list();
        if (CollectionUtils.isEmpty(dappList)) {
            return;
        }
        //auto install
        dappList.forEach(v -> {
            if (v.getStatus() == DappStatus.RUNNING) {
                v.setStatus(DappStatus.STOPPED);
                install(v);
            }
        });
    }

    @Override public Dapp download(String urlPath) throws IOException {
        String fileName = FilenameUtils.getName(urlPath);
        log.info("[download]the app file name:{}", fileName);
        File destination = new File(drsConfig.getDownloadPath(), UUID.randomUUID().toString());
        try {
            URL url = new URL(urlPath);
            FileUtils.copyURLToFile(url, destination);
        } catch (Throwable e) {
            File source = new File(urlPath);
            if (!source.exists()) {
                log.warn("download is fail,source file is not exists");
                throw new DappException(DappError.DAPP_SOURCE_FILE_NOT_EXISTS_ERROR);
            }
            FileUtils.copyFile(source, destination);
        }
        log.info("[download]the app file are saved to destination:{}", destination);
        //make jar file
        JarFile bizFile = new JarFile(destination);
        //get info from app file
        Dapp app = getInfo(bizFile);
        //check from database
        if (dappService.isExists(app.getName())) {
            log.warn("[download]the app is already download,url:{}", urlPath);
            throw new DappException(DappError.DAPP_ALREADY_EXISTS);
        }
        //generator config file from Jar file
        genAppConfig(bizFile, app.getName());
        //move
        Files.move(destination, new File(drsConfig.getDownloadPath(), fileName));
        //set DOWNLOADED status
        app.setStatus(DappStatus.DOWNLOADED);
        app.setFileName(fileName);
        return dappService.save(app);

    }

    @Override public boolean initialized(String appName) {
        Dapp dapp = dappService.findByAppName(appName);
        if (dapp == null) {
            log.warn("[initialized] app is not exists,appName:{}", appName);
            throw new DappException(DappError.DAPP_NOT_EXISTS);
        }
        if (dapp.getStatus() == DappStatus.INITIALIZED) {
            log.warn("[initialized] app status is already INITIALIZED,appName:{},status:{}", appName, dapp.getStatus());
            throw new DappException(DappError.DAPP_ALREADY_INITIALIZED);
        }
        //update status
        dappService.updateStatus(appName, DappStatus.INITIALIZED, null);
        return true;
    }

    /**
     * parse app info from Jar file
     *
     * @param bizFile
     * @return
     * @throws IOException
     */
    private Dapp getInfo(JarFile bizFile) throws IOException {
        JarFileArchive jarFileArchive = new JarFileArchive(bizFile);
        JarBizArchive bizArchive = new JarBizArchive(jarFileArchive);
        Dapp dapp = new Dapp();
        Attributes manifestMainAttributes = bizArchive.getManifest().getMainAttributes();
        dapp.setName(manifestMainAttributes.getValue(ARK_BIZ_NAME));
        String appVersion = manifestMainAttributes.getValue(ARK_BIZ_VERSION);
        dapp.setVersion(appVersion);
        String contextPath = manifestMainAttributes.getValue(WEB_CONTEXT_PATH);
        log.info("[getInfo]appName:{}",dapp.getName());
        log.info("[getInfo]contextPath:{}",contextPath);
        log.info("[getInfo]version:{}",appVersion);
        dapp.setContextPath(contextPath);

        String usedDrsVersion = manifestMainAttributes.getValue(DRS_VERSION_KEY);
        log.info("[getInfo]dapp usedDrsVersion:{}", usedDrsVersion);
        if (StringUtils.isEmpty(usedDrsVersion)) {
            log.warn("[getInfo]dapp drs version is not exists");
            throw new DappException(DappError.DAPP_DRS_VERSION_NOT_EXISTS);
        }
        Optional<Manifest> manifestOpt = getManifest(DappLifecycleManage.class);
        if (!manifestOpt.isPresent()) {
            log.warn("[getInfo]dapp version is not exists");
            throw new DappException(DappError.DAPP_COMMON_ERROR);
        }
        Manifest manifest = manifestOpt.get();
        String version = manifest.getMainAttributes().getValue(ARK_BIZ_VERSION);
        log.info("[getInfo]drs version:{}", version);
        if (!usedDrsVersion.equals(version)) {
            log.warn("[getInfo]dapp version:{} is unequal {}", usedDrsVersion, version);
            throw new DappException(DappError.DAPP_VERSION_UNEQUAL);
        }
        return dapp;
    }

    /**
     * generator config file
     *
     * @param bizFile
     * @param appName
     */
    private void genAppConfig(JarFile bizFile, String appName) throws IOException {
        String filePath = getConfigPath(appName);
        boolean isGened = false;
        Enumeration myEnum = bizFile.entries();
        while (myEnum.hasMoreElements()) {
            JarEntry myJarEntry = (JarEntry)myEnum.nextElement();
            if (DAPP_CONFIG_FILE_NAME.equals(myJarEntry.getName())) {
                InputStream in = null;
                FileOutputStream out = null;
                try {
                    in = bizFile.getInputStream(myJarEntry);
                    out = new FileOutputStream(filePath);
                    byte[] data = IOUtils.toByteArray(in);
                    IOUtils.write(data, out);
                } finally {
                    IOUtils.closeQuietly(in);
                    IOUtils.closeQuietly(out);
                }
                isGened = true;
                break;
            }
        }
        if (isGened) {
            log.info("generator config file is success,appName:{}", appName);
            resetWebContextPath(appName, filePath);
        } else {
            log.warn("generator config file is fail,appName:{}", appName);
        }
    }

    /**
     * reset web-context-path
     *
     * @param appName
     * @param filePath
     */
    private void resetWebContextPath(String appName, String filePath) {
        Properties p = new Properties();
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            File f = new File(filePath);
            is = new FileInputStream(f);
            p.load(is);
            p.put(WEB_SERVER_CONTEXT_PATH, "/" + appName);
            os = new FileOutputStream(f);
            p.store(os, null);
        } catch (IOException e) {
            log.warn("[resetWebContextPath] has IO error,appName:{}", appName, e);
            throw new DappException(DappError.DAPP_CONFIG_NOT_FOUND);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }

    /**
     * config path for app
     *
     * @param appName
     * @return
     */
    private String getConfigPath(String appName) {
        String configPath = drsConfig.getConfigPath() + File.separator + appName;
        File file = new File(configPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return configPath + File.separator + DAPP_CONFIG_FILE_NAME;
    }

    /**
     * install by app name
     *
     * @param appName
     * @return
     */
    @Override public boolean install(String appName) {
        Dapp dapp = dappService.findByAppName(appName);
        if (dapp == null) {
            log.warn("[install] app is not exists,appName:{}", appName);
            throw new DappException(DappError.DAPP_NOT_EXISTS);
        }
        this.install(dapp);
        return true;
    }

    /**
     * install by dapp info
     *
     * @param dapp
     */
    public void install(Dapp dapp) {
        if (dapp == null) {
            return;
        }
        DappStatus fromStatus = dapp.getStatus();
        String appName = dapp.getName();
        if (fromStatus == DappStatus.DOWNLOADED) {
            log.warn("[install] app status is not INITIALIZED,appName:{},status:{}", appName, fromStatus);
            throw new DappException(DappError.DAPP_NOT_INITIALIZED);
        }
        if (fromStatus == DappStatus.RUNNING) {
            log.warn("[install] app status is already RUNNING,appName:{},status:{}", appName, fromStatus);
            throw new DappException(DappError.DAPP_ALREADY_RUNNING);
        }
        DappStatus toStatus;
        String runError = null;
        try {
            File dappFile = new File(drsConfig.getDownloadPath(), dapp.getFileName());
            String configPath = getConfigPath(dapp.getName());
            log.info("install dapp:{}, with configPath:{}", dapp, configPath);
            ClientResponse response =
                ArkClient.installBiz(dappFile, new String[] {"--spring.config.location=" + configPath});
            if (ResponseCode.SUCCESS.equals(response.getCode())) {
                toStatus = DappStatus.RUNNING;
            } else {
                toStatus = DappStatus.STOPPED;
                runError = response.getMessage();
            }
        } catch (Throwable e) {
            log.error("dapp install is failed", e);
            runError = "install fail,unknown error";
            toStatus = DappStatus.STOPPED;
        }
        //update status
        dappService.updateStatus(appName, toStatus, runError);
        if (runError != null) {
            log.warn("dapp install is failed,{}", runError);
            throw new DappException(runError);
        }
    }

    @Override public boolean unInstall(String appName) {
        Dapp dapp = dappService.findByAppName(appName);
        if (dapp == null) {
            log.warn("[unInstall] app is not exists,appName:{}", appName);
            throw new DappException(DappError.DAPP_NOT_EXISTS);
        }
        String runError = null;
        try {
            ClientResponse response = ArkClient.uninstallBiz(dapp.getName(), dapp.getVersion());
            if (ResponseCode.SUCCESS.equals(response.getCode())) {
            } else {
                runError = response.getMessage();
            }
        } catch (Throwable e) {
            log.error("dapp uninstall is failed", e);
            runError = "unInstall fail,unknown error";
        }
        if (runError != null) {
            log.warn("dapp uninstall is failed,{}", runError);
            throw new DappException(runError);
        }
        //unInstall
        int r = dappService.unInstall(appName);
        log.info("unInstall dapp:{},result:{}",appName,r);
        try{
            new File(drsConfig.getDownloadPath(), dapp.getFileName()).delete();
        }catch (Throwable e){
            log.error("delete dapp file has error",e);
        }
        return true;
    }

    @Override public List<Dapp> list() {
        return dappService.findAll();
    }

    @Override public Properties queryConfig(String appName) {
        if (!dappService.isExists(appName)) {
            log.warn("[queryConfig] app is not exists,appName:{}", appName);
            throw new DappException(DappError.DAPP_NOT_EXISTS);
        }
        String filePath = getConfigPath(appName);
        Properties p = new Properties();
        FileInputStream is = null;
        try {
            is = new FileInputStream(new File(filePath));
            p.load(is);
            //filter
            p.remove(WEB_SERVER_PORT);
            p.remove(WEB_CONTEXT_PATH);
        } catch (IOException e) {
            log.warn("[queryConfig] has IO error,appName:{}", appName, e);
            throw new DappException(DappError.DAPP_CONFIG_NOT_FOUND);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return p;
    }

    @Override public void config(String appName, Map<String, String> config) {
        if (!dappService.isExists(appName)) {
            log.warn("[config] app is not exists,appName:{}", appName);
            throw new DappException(DappError.DAPP_NOT_EXISTS);
        }
        String filePath = getConfigPath(appName);
        Properties p = new Properties();
        if (!config.isEmpty()) {
            p.putAll(config);
        }
        if (!p.containsKey(SPRING_APP_NAME)) {
            p.put(SPRING_APP_NAME, appName);
        }
        if (!p.containsKey(WEB_SERVER_PORT)) {
            p.put(WEB_SERVER_PORT, baseConfig.getServerPort() + "");
        }
        p.put(WEB_SERVER_CONTEXT_PATH, "/" + appName);
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(new File(filePath));
            p.store(os, null);
        } catch (IOException e) {
            log.warn("[config] has IO error,appName:{}", appName, e);
            throw new DappException(DappError.DAPP_CONFIG_NOT_FOUND);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    @Override public List<AppProfileVO> queryCurrentApps() {
        List<Dapp> list = dappService.findAll();
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        List<AppProfileVO> appProfileVOList = Lists.newArrayList();
        list.forEach(v -> {
            try {
                AppProfileVO vo = dappStoreService.queryAppByName(v.getName());
                if(vo!=null) {
                    //set status
                    vo.setStatus(v.getStatus().name());
                    appProfileVOList.add(vo);
                }
            } catch (IOException e) {
                log.error("has io error", e);
            }
        });
        return appProfileVOList;
    }
}
