package io.stacs.nav.drs.boot.dao;

import io.stacs.nav.drs.boot.dao.po.AppUpgradeHistoryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  app upgrade history
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

    /**
     * query by name
     *
     * @param name
     * @return
     */
    List<AppUpgradeHistoryPO> queryByName(@Param("name") String name);
}
