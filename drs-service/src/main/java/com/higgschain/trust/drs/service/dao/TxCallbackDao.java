package com.higgschain.trust.drs.service.dao;

import com.higgschain.trust.drs.service.dao.po.TxCallbackPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * callback
 *
 * @author liuyu
 */
@Mapper public interface TxCallbackDao {
    /**
     * save
     *
     * @param po
     * @return
     */
    int save(TxCallbackPO po);

    /**
     * get max height
     *
     * @return
     */
    Long maxHeight();

    /**
     * @param height
     * @return
     */
    TxCallbackPO queryByHeightAndStatus(@Param("height") Long height,@Param("status")String status);

    /**
     * update status
     *
     * @param height
     * @param from
     * @param to
     * @return
     */
    int updateStatus(@Param("height") Long height, @Param("from") String from, @Param("to") String to);

}
