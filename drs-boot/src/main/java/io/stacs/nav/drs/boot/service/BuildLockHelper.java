package io.stacs.nav.drs.boot.service;

import io.stacs.nav.drs.api.exception.DappException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import static io.stacs.nav.drs.api.exception.DappError.DAPP_COMMON_ERROR;

/**
 * @author tangkun
 * @date 2020-02-11
 */
@Slf4j
public final class BuildLockHelper {

    private final static String LOCK_PREFIX = "INSTALL_LOCK_PREFIX_";

    private final static Map<String, ReentrantLock> APP_LOCK = new ConcurrentHashMap<>();

    public final static String buildLock(String lockName){
        StringBuilder sb = new StringBuilder();
        sb.append(LOCK_PREFIX);
        sb.append(lockName);

        String lock = sb.toString().intern();

        return lock;
    }

    public final static synchronized ReentrantLock getLock(String appName){
        if(StringUtils.isEmpty(appName)){
            log.warn("appName is empty");
            throw new DappException(DAPP_COMMON_ERROR);
        }
        ReentrantLock lock =  APP_LOCK.get(appName);
        if(lock == null){
            ReentrantLock appLock = new ReentrantLock();
            APP_LOCK.put(appName,appLock);
            return appLock;
        }
        return lock;
    }

}
