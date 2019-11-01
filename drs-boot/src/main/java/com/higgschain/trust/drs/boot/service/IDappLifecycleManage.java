package com.higgschain.trust.drs.boot.service;

import com.higgschain.trust.drs.boot.service.dapp.Dapp;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author suimi
 * @date 2019/10/31
 */
public interface IDappLifecycleManage {

    Dapp download(String url) throws IOException;

    void config(Long dappId, Map<String,String> config);

    boolean install(String fileName);

    boolean unInstall(String dappName, String version);

    List<Dapp> list();

}
