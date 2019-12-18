package io.stacs.nav.dapp.core.callback;

import io.stacs.nav.drs.api.model.TransactionPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuyu
 * @description
 * @date 2019-12-18
 */
@Component @Slf4j public class CallbackProcessor implements InitializingBean, ApplicationContextAware {
    private ApplicationContext applicationContext;

    private Map<String, ITxCallbackHandler> handlerMap = new HashMap<>();

    @Override public void afterPropertiesSet() throws Exception {
        Map<String, ITxCallbackHandler> beansOfType = applicationContext.getBeansOfType(ITxCallbackHandler.class);
        beansOfType.forEach((k, v) -> {
            String key = v.supportType().getCode() + v.supportVersion();
            handlerMap.put(key, v);
            log.info("callback handler supported: {}, bean:{}", key, v.getClass().getSimpleName());
        });
    }

    @Override public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * process
     *
     * @param po
     */
    public void process(TransactionPO po) {
        String key = po.getTxType() + po.getVersion();
        ITxCallbackHandler handler = handlerMap.get(key);
        if (handler == null) {
            log.error("[process] not support callback handler:{}", key);
            return;
        }
        handler.handle(po);
    }
}
