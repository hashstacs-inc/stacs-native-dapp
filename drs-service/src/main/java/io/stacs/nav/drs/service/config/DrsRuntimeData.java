package io.stacs.nav.drs.service.config;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author dekuofa <br>
 * @date 2019-12-09 <br>
 */
@Component public class DrsRuntimeData {

    private AtomicLong nextHeight = new AtomicLong();

    public long getNextHeight() {
        return nextHeight.get();
    }

    public void setNextHeight(long height) {
        this.nextHeight.lazySet(height);
    }
}
