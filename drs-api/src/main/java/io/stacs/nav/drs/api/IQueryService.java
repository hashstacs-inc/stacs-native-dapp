package io.stacs.nav.drs.api;

import com.github.pagehelper.PageInfo;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.BaseTxVO;
import io.stacs.nav.drs.api.model.TransactionPO;
import io.stacs.nav.drs.api.model.block.BlockHeaderVO;
import io.stacs.nav.drs.api.model.block.BlockVO;
import io.stacs.nav.drs.api.model.query.QueryBlockByHeightVO;
import io.stacs.nav.drs.api.model.query.QueryBlockVO;
import io.stacs.nav.drs.api.model.query.QueryTxListVO;
import io.stacs.nav.drs.api.model.query.QueryTxVO;

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

    TransactionPO queryTxById(QueryTxVO vo);

    PageInfo<TransactionPO> queryTx(QueryTxListVO vo);

    PageInfo<BlockVO> queryBlocks(QueryBlockVO vo);

    BlockHeaderVO queryBlockByHeight(QueryBlockByHeightVO vo);

}
