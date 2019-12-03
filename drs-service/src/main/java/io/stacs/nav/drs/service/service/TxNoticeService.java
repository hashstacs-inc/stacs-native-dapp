package io.stacs.nav.drs.service.service;

import com.alipay.sofa.ark.container.registry.PluginServiceProvider;
import com.alipay.sofa.ark.spi.model.Plugin;
import com.alipay.sofa.ark.spi.service.ArkInject;
import com.alipay.sofa.ark.spi.service.plugin.PluginManagerService;
import com.alipay.sofa.ark.spi.service.registry.RegistryService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.stacs.nav.drs.api.ITxNoticeService;
import io.stacs.nav.drs.api.exception.DappError;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.callback.TransactionReceipt;
import io.stacs.nav.drs.service.constant.Constants;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuyu
 * @description
 * @date 2019-12-03
 */
@Service @Slf4j public class TxNoticeService implements ITxNoticeService, InitializingBean {
    private static TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
    private Cache<String, LockObject> lockCache = CacheBuilder.newBuilder().weakValues().build();

    @ArkInject RegistryService registryService;
    @ArkInject PluginManagerService pluginManagerService;

    /**
     * wait
     *
     * @param txId
     * @param timeout
     * @return
     */
    @Override public TransactionReceipt syncWait(String txId, long timeout) {
        LockObject lockObject = createOrGetLockObj(txId);
        long remain = TIME_UNIT.toNanos(timeout);
        LockObject finalLockObject = null;
        Lock lock = lockObject.getLock();
        lock.lock();
        try {
            // wait notify
            while (remain > 0) {
                // check weather finish
                finalLockObject = lockCache.getIfPresent(txId);
                if (finalLockObject != null && finalLockObject.isFinish()) {
                    break;
                }
                try {
                    remain = lockObject.getCondition().awaitNanos(remain);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } finally {
            lock.unlock();
        }
        if (remain <= 0) {
            log.warn("wait is timeout for txId:{}", txId);
            throw new DappException(DappError.DAPP_WAIT_TIMEOUT);
        }
        return finalLockObject.getResult();
    }

    /**
     * notify
     *
     * @param transactionReceipt
     */
    public void notify(TransactionReceipt transactionReceipt) {
        LockObject lockObject = createOrGetLockObj(transactionReceipt.getTxId());
        // notify
        Lock lock = lockObject.getLock();
        lock.lock();
        try {
            if (!lockObject.isFinish()) {
                lockObject.setResult(transactionReceipt);
                lockObject.setFinish(true);
                lockObject.getCondition().signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * create or get lock object
     *
     * @param uniqueKey
     * @return
     */
    private LockObject createOrGetLockObj(String uniqueKey) {
        LockObject lockObj;
        try {
            lockObj = lockCache.get(uniqueKey, () -> {
                LockObject newLockObj = new LockObject();
                Lock lock = new ReentrantLock();
                newLockObj.setFinish(false);
                newLockObj.setCondition(lock.newCondition());
                newLockObj.setLock(lock);
                return newLockObj;
            });
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return lockObj;
    }

    @Override public void afterPropertiesSet() throws Exception {
        Plugin plugin = pluginManagerService.getPluginByName(Constants.SERVICE_NAME);
        if (plugin == null) {
            log.warn("init plugin is fail,get plugin is null,name:{}", Constants.SERVICE_NAME);
            return;
        }
        registryService.publishService(ITxNoticeService.class, this, new PluginServiceProvider(plugin));
    }

    @Getter @Setter public class LockObject {
        /**
         * lock
         */
        private Lock lock;
        /**
         * condition
         */
        private Condition condition;
        /**
         * finish
         */
        private boolean finish;
        /**
         * result
         */
        private TransactionReceipt result;
    }
}
