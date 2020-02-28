package io.stacs.nav.drs.boot.service;

import com.alipay.sofa.ark.api.ArkClient;
import com.alipay.sofa.ark.api.ClientResponse;
import com.alipay.sofa.ark.api.ResponseCode;
import com.alipay.sofa.ark.loader.JarBizArchive;
import com.alipay.sofa.ark.loader.archive.JarFileArchive;
import com.alipay.sofa.ark.loader.jar.JarEntry;
import com.alipay.sofa.ark.loader.jar.JarFile;
import com.alipay.sofa.ark.spi.model.BizInfo;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import io.stacs.nav.drs.api.exception.DappError;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.UpgradeVersion;
import io.stacs.nav.drs.boot.bo.Dapp;
import io.stacs.nav.drs.boot.config.BaseConfig;
import io.stacs.nav.drs.boot.dao.AppUpgradeHistoryDao;
import io.stacs.nav.drs.boot.dao.po.AppUpgradeHistoryPO;
import io.stacs.nav.drs.boot.enums.DappStatus;
import io.stacs.nav.drs.boot.service.dapp.IDappService;
import io.stacs.nav.drs.boot.service.dappstore.DappStoreService;
import io.stacs.nav.drs.boot.vo.AppProfileVO;
import io.stacs.nav.drs.service.config.DrsConfig;
import io.stacs.nav.drs.service.service.UpgradeService;
import io.stacs.nav.drs.service.utils.BeanConvertor;
import io.stacs.nav.drs.service.utils.config.ConfigListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nonnull;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

import static com.alipay.sofa.ark.spi.constant.Constants.*;
import static io.stacs.nav.drs.service.constant.Constants.DRS_VERSION_KEY;
import static io.stacs.nav.drs.service.utils.ResourceLoader.getManifest;

/**
 * @author suimi
 * @date 2019/10/31
 */
@Slf4j @Service public class DappLifecycleManage
    implements ConfigListener, IDappLifecycleManage, ApplicationListener<ApplicationReadyEvent> {
    /**
     * app config file name
     */
    private static final String DAPP_CONFIG_FILE_NAME = "application.properties";
    private static final String SPRING_APP_NAME = "spring.application.name";
    private static final String SPRING_JMX_NAME = "spring.jmx.default-domain";
    private static final String WEB_SERVER_PORT = "server.port";
    private static final String WEB_SERVER_CONTEXT_PATH = "server.servlet.context-path";

    @Autowired BaseConfig baseConfig;

    @Autowired DrsConfig drsConfig;
    @Autowired AppUpgradeHistoryDao appUpgradeHistoryDao;

    @Autowired IDappService dappService;
    @Autowired DappStoreService dappStoreService;
    @Autowired TransactionTemplate txRequired;
    @Autowired UpgradeService upgradeService;

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
        dappList.parallelStream().forEach(v -> {
            if (v.getStatus() == DappStatus.RUNNING || v.getStatus() == DappStatus.INSTALLING
                || v.getStatus() == DappStatus.STARTING) {
                v.setStatus(DappStatus.STOPPED);
                try {
                    log.info("auto install dapp:{}", v.getName());
                    install(v, null, true, false);
                } catch (Throwable e) {
                    log.error("auto install dapp has error", e);
                }
            } else if (v.getStatus() == DappStatus.UPGRADING) {
                try {
                    log.info("auto upgrade dapp:{}", v.getName());
                    upgrade(v.getName());
                } catch (Throwable e) {
                    log.error("auto upgrade dapp has error", e);
                }
            }
        });
    }

    @Override public Dapp download(String urlPath) throws IOException {
        String fileName = FilenameUtils.getName(urlPath);
        log.info("[download]the app file name:{}", fileName);
        File destination = new File(drsConfig.getDownloadPath(), UUID.randomUUID().toString());
        //download file
        this.downloadFile(urlPath, destination);
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
        genAppConfig(bizFile, app.getName(), null);
        //move
        Files.move(destination, new File(drsConfig.getDownloadPath(), fileName));
        //set DOWNLOADED status
        app.setStatus(DappStatus.DOWNLOADED);
        app.setFileName(fileName);
        //query dapp from appstore
        AppProfileVO appProfileVO = dappStoreService.queryAppByName(app.getName());
        if (appProfileVO != null) {
            //set versionCode of appStore
            app.setVersionCode(appProfileVO.getVersionCode());
        }
        return dappService.save(app);

    }

    /**
     * download dapp file
     *
     * @param urlPath
     * @param destination
     * @return
     * @throws IOException
     */
    private void downloadFile(String urlPath, File destination) throws IOException {
        try {
            URL url = new URL(urlPath);
            FileUtils.copyURLToFile(url, destination);
        } catch (Throwable e) {
            File source = new File(urlPath);
            if (!source.exists()) {
                log.warn("[downloadFile] is fail,source file is not exists");
                throw new DappException(DappError.DAPP_SOURCE_FILE_NOT_EXISTS_ERROR);
            }
            FileUtils.copyFile(source, destination);
        }
        log.info("[downloadFile]the app file are saved to destination:{}", destination);
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
        log.info("[getInfo]appName:{}", dapp.getName());
        log.info("[getInfo]contextPath:{}", contextPath);
        log.info("[getInfo]version:{}", appVersion);
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
     * @param fileName the config file name,allow null default:DAPP_CONFIG_FILE_NAME
     */
    private void genAppConfig(JarFile bizFile, String appName, String fileName) throws IOException {
        String filePath = getConfigPath(appName, fileName);
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
            resetConfigProperty(appName, filePath);
        } else {
            log.warn("generator config file is fail,appName:{}", appName);
        }
    }

    /**
     * reset property config
     *
     * @param appName
     * @param filePath
     */
    private void resetConfigProperty(String appName, String filePath) {
        Properties p = new Properties();
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            File f = new File(filePath);
            is = new FileInputStream(f);
            p.load(is);
            p.put(WEB_SERVER_CONTEXT_PATH, "/" + appName);
            p.put(SPRING_APP_NAME, appName);
            p.put(SPRING_JMX_NAME, appName);
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
        return getConfigPath(appName, null);
    }

    /**
     * config path for app
     *
     * @param appName
     * @param fileName
     * @return
     */
    private String getConfigPath(String appName, String fileName) {
        String configPath = drsConfig.getConfigPath() + File.separator + appName;
        File file = new File(configPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return configPath + File.separator + (StringUtils.isEmpty(fileName) ? DAPP_CONFIG_FILE_NAME : fileName);
    }

    /**
     * install by app name
     *
     * @param appName
     * @param isStart
     * @param isUpgrade
     * @return
     */
    @Override public boolean install(String appName, boolean isStart, boolean isUpgrade) {
        Dapp dapp = dappService.findByAppName(appName);
        if (dapp == null) {
            log.warn("[install] app is not exists,appName:{}", appName);
            throw new DappException(DappError.DAPP_NOT_EXISTS);
        }
        this.install(dapp, null, isStart, isUpgrade);
        return true;
    }

    /**
     * install by dapp info
     *
     * @param dapp
     * @param configName dapp config file name,allow null
     * @param isStart
     * @param isUpgrade
     */
    public void install(Dapp dapp, String configName, boolean isStart, boolean isUpgrade) {
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
        ReentrantLock reentrantLock = null;
        boolean flag = false;
        try {
            reentrantLock = BuildLockHelper.getLock(appName);

            flag = reentrantLock.tryLock(3, TimeUnit.SECONDS);
            if (!flag) {
                log.warn("[install] app status is already installing,appName:{},status:{}", appName, fromStatus);
                throw new DappException(DappError.DAPP_ALREADY_INSTALLING);
            }

            //update state to installing
            DappStatus doingStatus =
                isUpgrade ? DappStatus.UPGRADING : (isStart ? DappStatus.STARTING : DappStatus.INSTALLING);
            dappService.updateStatus(appName, doingStatus, "");

            File dappFile = new File(drsConfig.getDownloadPath(), dapp.getFileName());
            String configPath = getConfigPath(dapp.getName(), configName);
            log.info("install dapp:{}, with configPath:{}", dapp, configPath);
            ClientResponse response =
                ArkClient.installBiz(dappFile, new String[] {"--spring.config.location=" + configPath});
            if (ResponseCode.SUCCESS.equals(response.getCode())) {
                toStatus = isUpgrade ? DappStatus.UPGRADING : DappStatus.RUNNING;
            } else {
                toStatus = isStart ? DappStatus.STOPPED : DappStatus.INSTALLERROR;
                runError = response.getMessage();
            }
        } catch (DappException ex) {
            log.error("dapp install get lock failed", ex);
            throw ex;
        } catch (Throwable e) {
            log.error("dapp install is failed", e);
            runError = "install fail,unknown error";
            toStatus = isStart ? DappStatus.STOPPED : DappStatus.INSTALLERROR;
        } finally {
            if (reentrantLock != null && flag == true) {
                reentrantLock.unlock();
            }
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
            //check dapp is installed
            ClientResponse hasRes = ArkClient.checkBiz(dapp.getName(), dapp.getVersion());
            Set<BizInfo> bizInfoSet = hasRes.getBizInfos();
            if (!CollectionUtils.isEmpty(bizInfoSet)) {
                ClientResponse response = ArkClient.uninstallBiz(dapp.getName(), dapp.getVersion());
                if (ResponseCode.SUCCESS.equals(response.getCode())) {
                } else {
                    runError = response.getMessage();
                }
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
        log.info("unInstall dapp:{},result:{}", appName, r);
        try {
            new File(drsConfig.getDownloadPath(), dapp.getFileName()).delete();
        } catch (Throwable e) {
            log.error("delete dapp file has error", e);
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
            p.remove(SPRING_APP_NAME);
            p.remove(WEB_SERVER_PORT);
            p.remove(WEB_CONTEXT_PATH);
            p.remove(SPRING_JMX_NAME);
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
        if (!p.containsKey(SPRING_JMX_NAME)) {
            p.put(SPRING_JMX_NAME, appName);
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
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<AppProfileVO> mList = Lists.newArrayList();
        try {
            List<AppProfileVO> appProfileVOList = dappStoreService.queryApps();
            if (CollectionUtils.isEmpty(appProfileVOList)) {
                return mList;
            }
            Map<String, AppProfileVO> appProfileVOMap =
                appProfileVOList.stream().collect(Collectors.toMap(AppProfileVO::getName, v -> v));
            list.forEach(v -> {
                AppProfileVO vo = appProfileVOMap.get(v.getName());
                if (vo != null) {
                    //set status
                    vo.setStatus(v.getStatus().name());
                    mList.add(vo);
                }
            });
        } catch (IOException e) {
            log.error("has io error", e);
        }
        return mList;
    }

    @Override public boolean start(String appName) {
        install(appName, true, false);
        return false;
    }

    @Override public boolean stop(String appName, boolean isUpgrading) {
        Dapp dapp = dappService.findByAppName(appName);
        if (dapp == null) {
            log.warn("[stop] app is not exists,appName:{}", appName);
            throw new DappException(DappError.DAPP_NOT_EXISTS);
        }
        DappStatus fromStatus = dapp.getStatus();
        if (fromStatus == DappStatus.STOPPED) {
            log.warn("[stop] dapp is already STOPPED,appName:{},status:{}", appName, fromStatus);
            return true;
        }
        if (!isUpgrading && fromStatus != DappStatus.RUNNING) {
            log.warn("[stop] app status is not RUNNING,appName:{},status:{}", appName, fromStatus);
            throw new DappException(DappError.DAPP_NOT_RUNNING);
        }
        String runError = null;
        try {
            ClientResponse response = ArkClient.uninstallBiz(dapp.getName(), dapp.getVersion());
            if (ResponseCode.SUCCESS.equals(response.getCode())) {
            } else {
                runError = response.getMessage();
            }
        } catch (Throwable e) {
            log.error("[stop]dapp uninstall is failed", e);
            runError = "stop has fail," + e.getMessage();
        }
        if (runError != null) {
            log.warn("[stop]dapp stop has failed,{}", runError);
            throw new DappException(runError);
        }
        //update status
        if (!isUpgrading) {
            dappService.updateStatus(appName, DappStatus.STOPPED, null);
        }
        return true;
    }

    @Override public boolean upgrade(String appName) throws IOException {
        log.info("[upgrade] is start,appName:{}", appName);
        Dapp originalDapp = dappService.findByAppName(appName);
        if (originalDapp == null) {
            log.warn("[upgrade] app is not exists,appName:{}", appName);
            throw new DappException(DappError.DAPP_NOT_EXISTS);
        }
        //query from appstore
        AppProfileVO appProfileVO = dappStoreService.queryAppByName(appName);
        //compare version code
        if (originalDapp.getVersionCode() >= appProfileVO.getVersionCode()) {
            log.warn("[upgrade]Dapp Not need upgrade,drs.version:{},appstore.version:{},appName:{}",
                originalDapp.getVersionCode(), appProfileVO.getVersionCode(), appName);
            throw new DappException(DappError.DAPP_NOT_NEED_UPGRADE);
        }
        String fileName;
        File destination;
        JarFile bizFile;
        Dapp upgradeApp;
        try {
            //set status
            dappService.updateStatus(appName, DappStatus.UPGRADING, "");
            String urlPath = appProfileVO.getDownloadUrl();
            log.info("[upgrade]urlPath:{}", urlPath);
            fileName = FilenameUtils.getName(urlPath);
            fileName = "upgrade-" + appProfileVO.getVersionCode() + "-" + System.currentTimeMillis() + "-" + fileName;
            log.info("[upgrade]the app file name:{}", fileName);
            destination = new File(drsConfig.getDownloadPath(), fileName);
            //download file
            this.downloadFile(urlPath, destination);
            //make jar file
            bizFile = new JarFile(destination);
            //get info from app file
            upgradeApp = getInfo(bizFile);
            if (!StringUtils.equals(upgradeApp.getName(), appName)) {
                log.warn("[upgrade] name not same upgradeApp.name:{},appName:{}", originalDapp.getName(), appName);
                throw new DappException(DappError.DAPP_UPGRADE_NAME_NOT_SAME_ERROR);
            }
        } catch (DappException e) {
            //recover status
            dappService.updateStatus(appName, originalDapp.getStatus(), "");
            throw e;
        } catch (Throwable e) {
            log.error("[upgrade]has unknown error", e);
            //recover status
            dappService.updateStatus(appName, originalDapp.getStatus(), "");
            throw e;
        }
        //dapp config name
        String dappConfigFileName =
            "upgrade-" + appProfileVO.getVersionCode() + "-" + System.currentTimeMillis() + "-" + DAPP_CONFIG_FILE_NAME;
        //generator config file from Jar file
        genAppConfig(bizFile, upgradeApp.getName(), dappConfigFileName);
        //stop current dapp
        stop(appName, true);
        //reset status、fileName
        upgradeApp.setStatus(DappStatus.STOPPED);
        upgradeApp.setFileName(fileName);
        boolean isInstalled = false;
        DappException dappException = null;
        try {
            upgradeService.needUpgrade(appName, UpgradeVersion
                .of(originalDapp.getVersionCode(), appProfileVO.getVersionCode(), upgradeApp.getVersion()));
            //install new dapp
            install(upgradeApp, dappConfigFileName, true, true);
            isInstalled = true;
            log.info("[upgrade]installed upgrade-dapp");
        } catch (DappException e) {
            log.warn("[upgrade]install new dapp has dapp error", e);
            dappException = e;
        } catch (Throwable e) {
            log.warn("[upgrade]install new dapp has error", e);
        } finally {
            upgradeService.cancelUpgrade(appName);
        }
        if (!isInstalled) {
            //recover the original dapp
            log.info("[upgrade]start recover original-dapp:{}", originalDapp);
            install(appName, true, true);
            //recover status
            dappService.updateStatus(appName, originalDapp.getStatus(), "");
            log.info("[upgrade]recovered original-dapp");
            if (dappException != null) {
                throw dappException;
            }
            return false;
        }
        //replace dapp file
        Files.copy(destination, new File(drsConfig.getDownloadPath(), originalDapp.getFileName()));
        //replace dapp config
        Files.copy(new File(getConfigPath(appName, dappConfigFileName)), new File(getConfigPath(appName)));
        log.info("[upgrade]replaced dapp file");
        //reset original file name、versionCode、status
        upgradeApp.setFileName(originalDapp.getFileName());
        upgradeApp.setVersionCode(appProfileVO.getVersionCode());
        upgradeApp.setStatus(DappStatus.RUNNING);
        //make history record
        AppUpgradeHistoryPO history = BeanConvertor.convertBean(upgradeApp, AppUpgradeHistoryPO.class);
        //Save the updated dapp file name as history
        history.setFileName(fileName);
        //Save the updated dapp config file name as history
        history.setConfigName(dappConfigFileName);
        try {
            txRequired.execute(transactionStatus -> {
                //update database
                dappService.updateBySelective(upgradeApp);
                //save history
                appUpgradeHistoryDao.save(history);
                return null;
            });
            log.info("[upgrade]updated database");
        } catch (Throwable e) {
            log.error("[upgrade]update dapp info has database error", e);
            throw new DappException(DappError.DAPP_UPGRADE_DATABASE_ERROR);
        }
        log.info("[upgrade] is success");
        return true;
    }

    @Override public <T> void updateNotify(T config) {
        if (config instanceof DrsConfig) {
            this.drsConfig = (DrsConfig)config;
        }
    }

    @Nonnull @Override public Predicate<Object> filter() {
        return obj -> obj instanceof DrsConfig;
    }
}
