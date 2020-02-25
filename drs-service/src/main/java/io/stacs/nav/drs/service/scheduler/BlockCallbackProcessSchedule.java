package io.stacs.nav.drs.service.scheduler;

import io.stacs.nav.drs.service.config.DrsRuntimeData;
import io.stacs.nav.drs.service.dao.BlockCallbackDao;
import io.stacs.nav.drs.service.dao.po.BlockCallbackPO;
import io.stacs.nav.drs.service.enums.CallbackStatus;
import io.stacs.nav.drs.service.model.BlockCallbackBO;
import io.stacs.nav.drs.service.service.BlockCallbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * The type Tx process callback schedule.
 *
 * @author liuyu
 */
@Service @Slf4j public class BlockCallbackProcessSchedule  {
    @Autowired BlockCallbackDao txCallbackDao;
    @Autowired BlockCallbackService blockCallbackService;
    /**
     * next height
     */
    @Autowired private DrsRuntimeData runtimeData;

    /**
     * Exe.
     */
    @Scheduled(fixedDelayString = "${drs.schedule.processCallback:1000}") public void exe() {
        long nextHeight = runtimeData.getNextHeight();
        BlockCallbackPO po = txCallbackDao.queryByHeightAndStatus(nextHeight, CallbackStatus.INIT.name());
        if (po == null) {
            return;
        }
        BlockCallbackBO bo = new BlockCallbackBO();
        BeanUtils.copyProperties(po, bo);
        bo.setStatus(CallbackStatus.INIT);
        try {
            blockCallbackService.processCallbackBlock(bo);
            runtimeData.setNextHeight(nextHeight + 1);
        } catch (Throwable e) {
            log.error("process callback has error", e);
        }
    }
}
