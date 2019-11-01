package com.higgschain.trust.drs.boot.service.dapp;

import java.util.List;
import java.util.Map;

/**
 * @author suimi
 * @date 2019/10/31
 */
public interface IDappService {
    Dapp save(Dapp info);

    Dapp findByFileName(String fileName);

    List<Dapp> findAll();

    Dapp findById(long id);

    void update(Dapp dapp);

    Dapp findByName(String dappName, String version);

    void configDapp(Long dappId, Map<String,String> config);

    Map<String,String> getConfig(Long dappId);
}
