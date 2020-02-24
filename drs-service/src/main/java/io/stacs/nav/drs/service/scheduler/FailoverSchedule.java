package io.stacs.nav.drs.service.scheduler;

import io.stacs.nav.drs.service.config.DomainConfig;
import io.stacs.nav.drs.service.config.DrsRuntimeData;
import io.stacs.nav.drs.service.dao.BlockCallbackDao;
import io.stacs.nav.drs.service.dao.BlockVO;
import io.stacs.nav.drs.service.service.BlockCallbackService;
import io.stacs.nav.drs.service.service.BlockChainService;
import io.stacs.nav.drs.service.utils.JSONHelper;
import io.stacs.nav.drs.service.utils.Pair;
import io.stacs.nav.drs.service.utils.config.ConfigListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static io.stacs.nav.drs.service.model.ConvertHelper.*;

/**
 * @author dekuofa <br>
 * @date 2019-12-09 <br>
 */
@Service @Slf4j public class FailoverSchedule implements ConfigListener {

    @Autowired BlockCallbackDao txCallbackDao;
    @Autowired BlockCallbackService txCallbackService;
    @Autowired BlockChainService blockChainService;
    @Autowired private DrsRuntimeData runtimeData;
    //@Autowired private BlockCallbackProcessSchedule blockCallbackProcessSchedule;

    @Scheduled(fixedDelayString = "${drs.schedule.failover:60000}") public void schedule() {
        exe();
    }

    /**
     * peer max of SYNCHRONIZED block height
     */
    private final static int MAX_SYNCHRONIZED_BLOCK_HEIGHT = 10;
    /**
     * execute failover
     */
    private void exe() {
        try {
            final AtomicBoolean remain = new AtomicBoolean(true);
            while (remain.get()) {
                long nextHeight = runtimeData.getNextHeight();
                log.info("nextHeight:{}",nextHeight);
                long chainMaxHeight = blockChainService.queryCurrentHeight();
                log.info("chainMaxHeight:{}",chainMaxHeight);
                Long optCallbackHeight = txCallbackDao.initCallbackMinHeight();
                log.info("optCallbackHeight:{}",optCallbackHeight);
                Optional<Pair<Long, Long>>  synchronize = HeightChecker.of(nextHeight, chainMaxHeight,optCallbackHeight).countMissBlocksInterval();

                synchronize.ifPresent(interval -> {
                    log.info("left:{},right:{}",interval.getLeft(),interval.getRight());
                    Long endHeight = interval.right();
                    if (interval.getRight() - interval.getLeft() > MAX_SYNCHRONIZED_BLOCK_HEIGHT) {
                        endHeight = interval.getLeft() + MAX_SYNCHRONIZED_BLOCK_HEIGHT;
                        remain.set(true);
                    } else {
                        remain.set(false);
                    }
                    List<BlockVO> blocks = blockChainService.queryBlocks(interval.left(), endHeight).stream()
                        .map(json -> JSONHelper.toJavaObject(json, BlockVO.class)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
                    if (blocks.isEmpty()) {
                        return;
                    }
                    // bo -> po & setPO state = INIT
                    blocks.stream().map(block2CallbackBOConvert.andThen(block2CallbackPOConvert).andThen(setPOInitState))
                        .forEach(item -> {
                            try{
                                txCallbackDao.save(item);
                            }catch (Exception ex) {
                                log.error("save call back block error:{}",ex);
                            };
                            });

                    log.info("peer failover schedule executed success startHeight:{}, endHeight:{}",interval.getLeft(),
                        endHeight);
                });

                if(!synchronize.isPresent()){
                    remain.set(false);
                }
                log.info("peer failover schedule executed success remain data :{}", remain.get());
            }
            log.info("failover schedule executed success ");
        } catch (Throwable e) {
            log.error("failover execute has error:{}", e);
        }
    }

    @Override public <T> void updateNotify(T config) {
        if (config instanceof DomainConfig) {
            exe();
        }
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
