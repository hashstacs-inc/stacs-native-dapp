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
     * @param height
     * @return
     */
    TxCallbackPO queryByBlockHeight(@Param("height") Long height);
}
