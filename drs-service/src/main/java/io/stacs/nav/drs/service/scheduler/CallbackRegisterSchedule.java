package io.stacs.nav.drs.service.scheduler;

import com.alibaba.fastjson.JSONObject;
import io.stacs.nav.drs.service.config.DomainConfig;
import io.stacs.nav.drs.service.network.BlockChainFacade;
import io.stacs.nav.drs.service.utils.config.ConfigListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.function.Predicate;

/**
 * @author dekuofa <br>
 * @date 2019-11-12 <br>
 */
@Component @Slf4j public class CallbackRegisterSchedule implements ConfigListener {
    private static final long REGISTER_RATE = 5 * 60 * 1_000;
    private String callbackUrl;

    @Autowired private BlockChainFacade facade;

    @Scheduled(fixedRate = REGISTER_RATE) public void exe() {
        try {
            JSONObject message = new JSONObject();
            message.put("callbackUrl", callbackUrl);
            facade.send("callback/register", message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public <T> void updateNotify(T config) {
        this.callbackUrl = ((DomainConfig)config).getCallbackUrl();
    }

    @Nonnull @Override public Predicate<Object> filter() {
        return obj -> obj instanceof DomainConfig;
    }
}
