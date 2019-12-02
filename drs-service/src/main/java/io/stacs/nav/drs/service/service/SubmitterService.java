package io.stacs.nav.drs.service.service;

import com.alipay.sofa.ark.container.registry.PluginServiceProvider;
import com.alipay.sofa.ark.spi.model.Plugin;
import com.alipay.sofa.ark.spi.service.ArkInject;
import com.alipay.sofa.ark.spi.service.plugin.PluginManagerService;
import com.alipay.sofa.ark.spi.service.registry.RegistryService;
import io.stacs.nav.drs.api.ISubmitterService;
import io.stacs.nav.drs.api.model.BaseTxVO;
import io.stacs.nav.drs.service.constant.Constants;
import io.stacs.nav.drs.service.utils.ReflectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * @author suimi
 * @date 2019/10/30
 */
@Slf4j @Service public class SubmitterService implements InitializingBean {

    @ArkInject RegistryService registryService;

    @ArkInject PluginManagerService pluginManagerService;

    @Autowired TestService testService;
    @Autowired TxRequestService requestService;

    // @formatter:off
    @Override public void afterPropertiesSet() {
        Plugin plugin = pluginManagerService.getPluginByName(Constants.SERVICE_NAME);
        if (plugin == null) {
            log.warn("init plugin is fail,get plugin is null,name:{}", Constants.SERVICE_NAME);
            return;
        }
        Consumer<Object[]> TX_SUBMITTER_HANDLER =  args -> {
           requestService.submitTx((BaseTxVO)args[0]);
        };

        BiPredicate<Method, Object[]> FILTER = (method, args) -> {
            if (!(args.length == 1 && args[0].getClass().isAssignableFrom(BaseTxVO.class)))
                throw new IllegalArgumentException(String.format("Illegal args for tx submitter: %s", Arrays.toString(args)));
            return true;
        };
        registryService.publishService(
            ISubmitterService.class,
            ReflectUtils.getInstance(
                ISubmitterService.class,
                ReflectUtils.newProxy(TX_SUBMITTER_HANDLER, FILTER)),
            new PluginServiceProvider(plugin));
    }
    // @formatter:on

}
