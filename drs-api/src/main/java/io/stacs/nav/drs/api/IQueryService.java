package io.stacs.nav.drs.api;

import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.BaseTxVO;
import io.stacs.nav.drs.api.model.block.BlockVO;
import io.stacs.nav.drs.api.model.query.QueryBlockByHeightVO;
import io.stacs.nav.drs.api.model.query.QueryBlockVO;
import io.stacs.nav.drs.api.model.query.QueryTxListVO;
import io.stacs.nav.drs.api.model.query.QueryTxVO;
import io.stacs.nav.drs.api.model.tx.CoreTransactionVO;

import java.util.List;

/**
 * @author dekuofa1995
 * @date 2019/12/2
 */
public interface IQueryService {

    /**
     * generate sign value
     */
    String generateSignature(BaseTxVO vo) throws DappException;

    Long queryCurrentHeight();

    List<CoreTransactionVO> queryCoreTxListByPage(QueryTxListVO vo);

    CoreTransactionVO queryCoreTxById(QueryTxVO vo);

    List<BlockVO> queryBlockListByPage(QueryBlockVO vo);

    BlockVO queryBlockByHeight(QueryBlockByHeightVO vo);

}
