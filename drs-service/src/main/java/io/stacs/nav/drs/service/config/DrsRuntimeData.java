package io.stacs.nav.drs.service.config;

import io.stacs.nav.drs.service.dao.BlockCallbackDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author dekuofa <br>
 * @date 2019-12-09 <br>
 */
@Slf4j
@Component public class DrsRuntimeData implements InitializingBean {

    @Autowired BlockCallbackDao txCallbackDao;

    private AtomicLong nextHeight = new AtomicLong();

    public long getNextHeight() {
        return nextHeight.get();
    }

    public void setNextHeight(long height) {
        this.nextHeight.lazySet(height);
    }

    @Override public void afterPropertiesSet() throws Exception {
        log.info("DrsRuntimeData afterPropertiesSet");
        Long nextHeight = txCallbackDao.maxHeight();
        if (nextHeight == null) {
            nextHeight = 1L;
        } else {
            nextHeight += 1L;
        }
        log.info("add log init nextHeight：{}", nextHeight);
        setNextHeight(nextHeight);
    }
}
