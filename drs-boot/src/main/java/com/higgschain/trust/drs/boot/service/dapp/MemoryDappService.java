package com.higgschain.trust.drs.boot.service.dapp;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author suimi
 * @date 2019/11/1
 */
@Service public class MemoryDappService implements IDappService {

    private int index = 0;

    Map<Long, Dapp> maps = new ConcurrentHashMap<>();

    Map<Long, Map> configs = new ConcurrentHashMap<>();

    @Override public Dapp save(Dapp info) {
        info.setId(index++);
        maps.put(info.getId(), info);
        return info;
    }

    @Override public Dapp findByFileName(String fileName) {
        List<Dapp> collect = maps.values().stream().filter(dapp -> dapp.getFileName().equalsIgnoreCase(fileName))
            .collect(Collectors.toList());
        if (collect.isEmpty()) {
            return null;
        }
        return collect.get(0);
    }

    @Override public List<Dapp> findAll() {
        return new ArrayList<>(maps.values());
    }

    @Override public Dapp findById(long id) {
        return maps.get(id);
    }

    @Override public void update(Dapp dapp) {
        maps.put(dapp.getId(), dapp);
    }

    @Override public Dapp findByName(String dappName, String version) {
        List<Dapp> collect = maps.values().stream()
            .filter(dapp -> dapp.getName().equalsIgnoreCase(dappName) && dapp.getVersion().equalsIgnoreCase(version))
            .collect(Collectors.toList());
        if (collect.isEmpty()) {
            return null;
        }
        return collect.get(0);
    }

    @Override public void configDapp(Long dappId, Map<String,String> config) {
        configs.put(dappId, config);
    }

    @Override public Map<String,String> getConfig(Long dappId) {
        return configs.get(dappId);
    }
}
