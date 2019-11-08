package com.higgschain.trust.drs.service.scheduler;

import com.alibaba.fastjson.JSON;
import com.higgschain.trust.drs.service.dao.TxRequestDao;
import com.higgschain.trust.drs.service.dao.po.TxRequestPO;
import com.higgschain.trust.drs.service.enums.RequestStatus;
import com.higgschain.trust.drs.service.model.TxRequestBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * The type Tx process init schedule.
 *
 * @author liuyu
 */
@Service @Slf4j @EnableScheduling public class TxProcessInitSchedule {
    @Autowired TxRequestDao txRequestDao;

    @Autowired private InitTxDisruptor initTxDisruptor;

    private int pageNo = 1;
    private int maxPageNo = 1000;

    @Value("${drs.schedule.initSize:200}") private int pageSize = 200;

    /**
     * Exe.
     */
    @Scheduled(fixedDelayString = "${drs.schedule.processInit:5000}") public void exe() {
        List<TxRequestPO> list =
            txRequestDao.queryByStatus(RequestStatus.INIT.name(), (pageNo - 1) * pageSize, pageSize);
        if (CollectionUtils.isEmpty(list) || pageNo == maxPageNo) {
            pageNo = 1;
            return;
        }
        list.forEach(po -> {
            TxRequestBO bo = new TxRequestBO();
            BeanUtils.copyProperties(po, bo);
            bo.setTxData(JSON.parse(po.getTxData()));
            bo.setStatus(RequestStatus.fromCode(po.getStatus()));
            initTxDisruptor.publish(po.getTxId(), bo);
        });
        pageNo++;
    }
}
