package io.stacs.nav.drs.boot.service.dapp;

import io.stacs.nav.drs.boot.dao.AppUpgradeHistoryDao;
import io.stacs.nav.drs.boot.vo.AppUpgradeHistoryVO;
import io.stacs.nav.drs.service.utils.BeanConvertor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuyu
 * @description
 * @date 2020-02-14
 */
@Service @Slf4j public class UpgradeHistoryService {
    @Autowired private AppUpgradeHistoryDao appUpgradeHistoryDao;

    /**
     * query upgrade history
     *
     * @param name
     * @return
     */
    public List<AppUpgradeHistoryVO> queryHistory(String name) {
        return BeanConvertor.convertList(appUpgradeHistoryDao.queryByName(name), AppUpgradeHistoryVO.class);
    }
}
