package io.stacs.nav.drs.boot.service;

import io.stacs.nav.drs.boot.bo.Dapp;
import io.stacs.nav.drs.boot.vo.AppProfileVO;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author suimi
 * @date 2019/10/31
 */
public interface IDappLifecycleManage {
    /**
     * download
     *
     * @param url
     * @return
     * @throws IOException
     */
    Dapp download(String url) throws IOException;

    /**
     * init
     *
     * @param appName
     * @return
     */
    boolean initialized(String appName);
    /**
     * install
     *
     * @param fileName
     * @param isStart
     * @param isUpgrade
     * @return
     */
    boolean install(String fileName,boolean isStart,boolean isUpgrade);

    /**
     * uninstall
     *
     * @param appName
     * @return
     */
    boolean unInstall(String appName);

    /**
     * list all dapp
     *
     * @return
     */
    List<Dapp> list();

    /**
     * query config by appName
     *
     * @param appName
     * @return
     */
    Properties queryConfig(String appName);

    /**
     * config app
     *
     * @param appName
     * @param config
     */
    void config(String appName, Map<String, String> config);

    /**
     * Gets the currently installed app
     * @return
     */
    List<AppProfileVO> queryCurrentApps();

    /**
     * start dapp
     *
     * @param appName
     * @return
     */
    boolean start(String appName);

    /**
     * stop dapp
     *
     * @param appName
     * @param isUpgrading
     * @return
     */
    boolean stop(String appName,boolean isUpgrading);

    /**
     * dapp upgrade
     *
     * @param appName
     * @return
     */
    boolean upgrade(String appName) throws IOException;
}
