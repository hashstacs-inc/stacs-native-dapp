package io.stacs.nav.drs.service.dao;

import io.stacs.nav.drs.service.BaseDao;
import io.stacs.nav.drs.service.dao.po.BlockPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Block dao.
 */
@Mapper public interface BlockDao extends BaseDao<BlockPO> {
    /**
     * query block by block height
     *
     * @param height the height
     * @return block po
     */
    BlockPO queryByHeight(@Param("height") Long height);

    /**
     * query blocks by block height
     *
     * @param startHeight start height
     * @param size        size of blocks
     * @return list
     */
    List<BlockPO> queryBlocks(@Param("startHeight") Long startHeight, @Param("limit") int size);

    /**
     * get max height of block
     *
     * @return max height
     */
    Long getMaxHeight();

    /**
     * get max height of block
     *
     * @param limit the limit
     * @return limit height
     */
    List<Long> getLimitHeight(@Param("limit") int limit);

    /**
     * query blocks with condition
     *
     * @param height    the height
     * @param blockHash the block hash
     * @param start     the start
     * @param end       the end
     * @return list
     */
    List<BlockPO> queryBlocksWithCondition(@Param("height") Long height, @Param("blockHash") String blockHash,
                                           @Param("start") int start, @Param("end") int end);

    /**
     * count blocks with condition
     *
     * @param height    the height
     * @param blockHash the block hash
     * @return long
     */
    long countBlockWithCondition(@Param("height") Long height, @Param("blockHash") String blockHash);
}
