package io.stacs.nav.drs.service.scheduler;

import com.alibaba.fastjson.JSONObject;
import io.stacs.nav.drs.service.network.BlockChainFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author dekuofa <br>
 * @date 2019-11-12 <br>
 */
@Component @Slf4j @EnableScheduling public class CallbackRegisterSchedule {
    private static final long REGISTER_RATE = 5 * 60 * 1_000;
    @Autowired private BlockChainFacade facade;
    @Value("${drs.domain.callbackUrl}") private String callbackUrl;

    @Scheduled(fixedRate = REGISTER_RATE) public void exe() {
        try {
            JSONObject message = new JSONObject();
            message.put("callbackUrl", callbackUrl);
            facade.send("callback/register", message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
