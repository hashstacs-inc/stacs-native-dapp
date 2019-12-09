package io.stacs.nav.drs.service.utils.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author dekuofa <br>
 * @date 2019-11-18 <br>
 */
@Component @Slf4j public class ConfigurationManager {

    private static final Function<Object, Consumer<ConfigListener>> NOTIFY_ALL_LISTENER = config -> listener -> {
        if (listener.filter().test(config)) {
            listener.updateNotify(config);
        }
    };
    @Autowired private ApplicationContext context;
    private ConcurrentHashMap<Class<?>, Object> configMap;
    private Collection<ConfigListener> listeners;

    @PostConstruct public void init() {
        configMap = new ConcurrentHashMap<>();
        context.getBeansWithAnnotation(DynamicConfig.class).values()
            .forEach(config -> configMap.putIfAbsent(ClassUtils.getUserClass(config.getClass()), config));
        listeners = context.getBeansOfType(ConfigListener.class).values();
        // init set config
        for (Object config : configMap.values()) {
            listeners.forEach(NOTIFY_ALL_LISTENER.apply(config));
        }

    }

    public void updateConfig(Object config) {
        log.info("update {} configuration", config.getClass());
        // replace the old version
        configMap.computeIfPresent(config.getClass(), (key, oldVal) -> config);
        listeners.forEach(NOTIFY_ALL_LISTENER.apply(config));
    }

    @SuppressWarnings("unchecked") public <T> T getConfigByClass(Class<T> clazz) {
        return (T)configMap.get(clazz);
    }

}
