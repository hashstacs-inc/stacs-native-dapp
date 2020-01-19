package io.stacs.nav.drs.boot.dao;

import io.stacs.nav.drs.boot.dao.po.SysConfPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * system config
 *
 * @author liuyu
 */
@Mapper
public interface SysConfDao {
    /**
     * save po data
     *
     * @param po
     * @return
     */
    int save(SysConfPO po);

    /**
     * query by key
     *
     * @param key
     * @return
     */
    SysConfPO queryByKey(@Param("key") String key);

    /**
     * udpate by key
     *
     * @param key
     * @param value
     * @return
     */
    int updateByKey(@Param("key") String key,@Param("value") String value);
}
