package com.higgschain.trust.drs.service.scheduler;

import com.higgschain.trust.drs.service.dao.TxCallbackDao;
import com.higgschain.trust.drs.service.dao.po.TxCallbackPO;
import com.higgschain.trust.drs.service.enums.CallbackStatus;
import com.higgschain.trust.drs.service.model.TxCallbackBO;
import com.higgschain.trust.drs.service.service.TxCallbackService;
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
     * block height
     */
    private Long currentHeight = 1L;

    @Override public void afterPropertiesSet() throws Exception {
        currentHeight = txCallbackDao.maxHeight();
        log.info("currentHeight：{}", currentHeight);
    }

    /**
     * Exe.
     */
    @Scheduled(fixedDelayString = "${drs.schedule.processCallback:1000}") public void exe() {
        TxCallbackPO po = txCallbackDao.queryByBlockHeight(currentHeight);
        if (po == null) {
            return;
        }
        CallbackStatus status = CallbackStatus.fromCode(po.getStatus());
        if (status != CallbackStatus.INIT) {
            return;
        }
        TxCallbackBO bo = new TxCallbackBO();
        BeanUtils.copyProperties(po, bo);
        bo.setStatus(status);
        txCallbackService.processCallbackTx(bo);
        currentHeight++;
    }
}
