package io.stacs.nav.drs.service.scheduler;

import io.stacs.nav.drs.service.config.DrsRuntimeData;
import io.stacs.nav.drs.service.dao.BlockCallbackDao;
import io.stacs.nav.drs.service.dao.BlockVO;
import io.stacs.nav.drs.service.service.BlockCallbackService;
import io.stacs.nav.drs.service.service.BlockChainService;
import io.stacs.nav.drs.service.utils.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static io.stacs.nav.drs.service.model.ConvertHelper.*;

/**
 * @author dekuofa <br>
 * @date 2019-12-09 <br>
 */
@Service @Slf4j public class FailoverSchedule {

    @Autowired BlockCallbackDao txCallbackDao;
    @Autowired BlockCallbackService txCallbackService;
    @Autowired BlockChainService blockChainService;
    @Autowired private DrsRuntimeData runtimeData;

    @Scheduled(fixedDelayString = "${drs.schedule.failover:5000}") public void exe() {
        long nextHeight = runtimeData.getNextHeight();
        long chainMaxHeight = blockChainService.queryCurrentHeight();
        Long optCallbackHeight = txCallbackDao.initCallbackMinHeight();
        HeightChecker.of(nextHeight, chainMaxHeight, optCallbackHeight).countMissBlocksInterval().ifPresent(
            interval -> {
                List<BlockVO> blocks = blockChainService.queryBlocks(interval.left(), interval.right());
                if (blocks.isEmpty())
                    return;
                // bo -> po & setPO state = INIT
                blocks.stream().map(block2CallbackBOConvert.andThen(block2CallbackPOConvert).andThen(setPOInitState))
                    .forEach(txCallbackDao::save);
            });
    }

    static class HeightChecker {
        // min(callbackHeight),if has not callback block well be empty
        private Optional<Long> optCallbackHeight;
        // drs next block height
        private long nextHeight;
        // chain max block height
        private long chainMaxHeight;

        private HeightChecker() {
        }

        private HeightChecker(long nextHeight, long chainMaxHeight, Optional<Long> optCallbackHeight) {
            this.optCallbackHeight = optCallbackHeight;
            this.nextHeight = nextHeight;
            this.chainMaxHeight = chainMaxHeight;
        }

        public static HeightChecker of(long nextHeight, long chainMaxHeight, @Nullable Long callbackHeight) {
            return new HeightChecker(nextHeight, chainMaxHeight, Optional.ofNullable(callbackHeight));
        }

        /**
         * if no missing block => empty
         * if has missing block => Pair(startHeight, endHeight)
         */
        public Optional<Pair<Long, Long>> countMissBlocksInterval() {
            // missing block (maybe)
            // 1. if exist callback block && nextHeight < callbackHeight
            // 2. if not exist callback block && chainMaxHeight > currentHeight = nextHeight - 1
            //@formatter:off
            return optCallbackHeight.map(callbackHeight -> nextHeight < callbackHeight
                    ? Optional.of(Pair.of(nextHeight, callbackHeight - 1))
                    : Optional.<Pair<Long, Long>>empty())
                .orElseGet(() -> chainMaxHeight > nextHeight - 1
                    ? Optional.of(Pair.of(nextHeight, chainMaxHeight))
                    : Optional.empty());
            //@formatter:on
        }

    }
}