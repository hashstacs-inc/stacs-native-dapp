package com.higgschain.trust.drs.service.dao;

import com.higgschain.trust.drs.service.dao.po.TxRequestPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * tx request
 *
 * @author liuyu
 */
@Mapper public interface TxRequestDao {
    /**
     * save po
     *
     * @param po
     * @return
     */
    int save(TxRequestPO po);

    /**
     * query by tx id
     *
     * @param txId
     * @return
     */
    TxRequestPO queryByTxId(@Param("txId") String txId);

    /**
     * update status
     *
     * @param txId
     * @param from
     * @param to
     * @return
     */
    int updateStatus(@Param("txId") String txId, @Param("from") String from, @Param("to") String to);

    /**
     * update status for receipt
     *
     * @param txId
     * @param from
     * @param to
     * @param receipt
     * @return
     */
    int updateStatusAndReceipt(@Param("txId") String txId, @Param("from") String from, @Param("to") String to,
        @Param("receipt") String receipt);

    /**
     * update receipt
     *
     * @param txId
     * @param receipt
     * @return
     */
    int updateReceipt(@Param("txId") String txId,@Param("receipt") String receipt);

    /**
     * query by status
     *
     * @param status
     * @param row
     * @param count
     * @return
     */
    List<TxRequestPO> queryByStatus(@Param("status") String status, @Param("row") int row, @Param("count") int count);
}
