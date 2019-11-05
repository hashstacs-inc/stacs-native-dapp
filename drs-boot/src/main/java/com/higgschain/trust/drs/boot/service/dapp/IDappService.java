package com.higgschain.trust.drs.boot.service.dapp;

import com.higgschain.trust.drs.boot.bo.Dapp;
import com.higgschain.trust.drs.boot.enums.DappStatus;

import java.util.List;

/**
 * @author suimi
 * @date 2019/10/31
 */
public interface IDappService {
    /**
     * save app info
     *
     * @param app
     * @return
     */
    Dapp save(Dapp app);

    /**
     * find app info by app name
     *
     * @param appName
     * @return
     */
    Dapp findByAppName(String appName);

    /**
     * dapp is exists
     *
     * @param appName
     * @return
     */
    boolean isExists(String appName);

    /**
     * query all dapp
     *
     * @return
     */
    List<Dapp> findAll();

    /**
     * update status and run error
     *
     * @param appName
     * @param toStatus
     * @param runError
     */
    void updateStatus(String appName, DappStatus toStatus, String runError);
}
