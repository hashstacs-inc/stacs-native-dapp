package io.stacs.nav.drs.service.scheduler;

import io.stacs.nav.drs.service.dao.TxCallbackDao;
import io.stacs.nav.drs.service.dao.po.TxCallbackPO;
import io.stacs.nav.drs.service.enums.CallbackStatus;
import io.stacs.nav.drs.service.model.TxCallbackBO;
import io.stacs.nav.drs.service.service.TxCallbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * The type Tx process callback schedule.
 *
 * @author liuyu
 */
@Service @Slf4j @EnableScheduling public class TxCallbackProcessSchedule implements InitializingBean {
    @Autowired TxCallbackDao txCallbackDao;
    @Autowired TxCallbackService txCallbackService;
    /**
     * next height
     */
    private Long nextHeight;

    @Override public void afterPropertiesSet() throws Exception {
        nextHeight = txCallbackDao.maxHeight();
        if (nextHeight == null) {
            nextHeight = 1L;
        }else{
            nextHeight += 1L;
        }
        log.info("init nextHeight：{}", nextHeight);
    }

    /**
     * Exe.
     */
    @Scheduled(fixedDelayString = "${drs.schedule.processCallback:1000}") public void exe() {
        TxCallbackPO po = txCallbackDao.queryByHeightAndStatus(nextHeight, CallbackStatus.INIT.name());
        if (po == null) {
            return;
        }
        TxCallbackBO bo = new TxCallbackBO();
        BeanUtils.copyProperties(po, bo);
        bo.setStatus(CallbackStatus.INIT);
        txCallbackService.processCallbackTx(bo);
        nextHeight++;
    }
}
