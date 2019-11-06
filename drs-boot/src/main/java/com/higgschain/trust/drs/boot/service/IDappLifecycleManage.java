package com.higgschain.trust.drs.boot.service;

import com.higgschain.trust.drs.boot.bo.Dapp;

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
     * @return
     */
    boolean install(String fileName);

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
}
