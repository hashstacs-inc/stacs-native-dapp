package io.stacs.nav.drs.boot.dao;

import io.stacs.nav.drs.boot.dao.po.AppUpgradeHistoryPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * save app upgrade history
 * @author liuyu
 */
@Mapper
public interface AppUpgradeHistoryDao {
    /**
     * save po data
     *
     * @param appUpgradeHistoryPO
     * @return
     */
    int save(AppUpgradeHistoryPO appUpgradeHistoryPO);
}
