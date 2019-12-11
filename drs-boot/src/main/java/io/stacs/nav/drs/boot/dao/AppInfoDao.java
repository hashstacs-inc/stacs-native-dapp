package io.stacs.nav.drs.boot.dao;

import io.stacs.nav.drs.boot.dao.po.AppInfoPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * save app info
 * @author liuyu
 */
@Mapper
public interface AppInfoDao{
    /**
     * save po data
     *
     * @param appInfoPO
     * @return
     */
    int save(AppInfoPO appInfoPO);

    /**
     * query by name
     *
     * @param name
     * @return
     */
    AppInfoPO queryByAppName(@Param("name")String name);

    /**
     * update status
     *
     * @param name
     * @param to
     * @param errorMsg
     * @return
     */
    int updateStatus(@Param("name")String name,@Param("to") String to,@Param("runError") String errorMsg);

    /**
     * query all info
     *
     * @return
     */
    List<AppInfoPO> queryAll();

    /**
     * uninstall
     * @param name
     * @return
     */
    int unInstall(@Param("name")String name);
}
