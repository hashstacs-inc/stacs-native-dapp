package io.stacs.nav.drs.service.dao;

import io.stacs.nav.drs.service.dao.po.BlockCallbackPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.annotation.Nullable;

/**
 * callback
 *
 * @author liuyu
 */
@Mapper public interface BlockCallbackDao {
    /**
     * save
     *
     * @param po
     * @return
     */
    int save(BlockCallbackPO po);

    /**
     * get max height
     *
     * @return
     */
    Long maxHeight();

    /**
     * get exist block
     * @return max height of call back
     */
    Long maxExistHeight();

    @Nullable Long initCallbackMinHeight();

    /**
     * @param height
     * @return
     */
    BlockCallbackPO queryByHeightAndStatus(@Param("height") Long height, @Param("status") String status);

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
