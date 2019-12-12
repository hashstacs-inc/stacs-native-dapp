package io.stacs.nav.drs.service.dao;

import io.stacs.nav.drs.api.model.TransactionPO;
import io.stacs.nav.drs.api.model.TransactionVO;
import io.stacs.nav.drs.api.model.query.QueryTxListVO;
import io.stacs.nav.drs.service.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TransactionPO data deal dao
 *
 * @author lingchao
 * @create 2018年03月27日20 :10
 */
@Mapper public interface TransactionDao extends BaseDao<TransactionPO> {

    /**
     * query transaction by transaction id
     *
     * @param txId the tx id
     * @return transaction po
     */
    TransactionPO queryByTxId(String txId);

    /**
     * batch insert
     *
     * @param list the list
     * @return int
     */
    int batchInsert(List<TransactionPO> list);

    /**
     * query by block height
     *
     * @param blockHeight the block height
     * @return list
     */
    List<TransactionPO> queryByBlockHeight(@Param("blockHeight") Long blockHeight);

    /**
     * query by tx ids
     *
     * @param txIds the tx ids
     * @return list
     */
    List<String> queryByTxIds(@Param("txIds") List<String> txIds);

    List<TransactionVO> queryTxWithCondition(@Param("cond") QueryTxListVO vo);

}
