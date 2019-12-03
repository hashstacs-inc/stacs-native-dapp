package io.stacs.nav.dapp.explorer.controller;

import com.alipay.sofa.ark.spi.service.ArkInject;
import io.stacs.nav.drs.api.IQueryService;
import io.stacs.nav.drs.api.model.RespData;
import io.stacs.nav.drs.api.model.block.BlockVO;
import io.stacs.nav.drs.api.model.query.QueryBlockByHeightVO;
import io.stacs.nav.drs.api.model.query.QueryBlockVO;
import io.stacs.nav.drs.api.model.query.QueryTxListVO;
import io.stacs.nav.drs.api.model.query.QueryTxVO;
import io.stacs.nav.drs.api.model.tx.CoreTransactionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static io.stacs.nav.drs.api.model.RespData.success;

/**
 * @author dekuofa <br>
 * @date 2019-12-02 <br>
 */
@SuppressWarnings("unchecked") @RestController @RequestMapping("/explorer") @Slf4j public class ExplorerController {

    @ArkInject private IQueryService queryService;

    public RespData<Long> queryCurrentHeight() {
        return success(queryService.queryCurrentHeight());
    }

    public RespData<List<CoreTransactionVO>> queryCoreTxListByPage(QueryTxListVO vo) {
        return success(queryService.queryCoreTxListByPage(vo));
    }

    public RespData<CoreTransactionVO> queryCoreTxById(QueryTxVO vo) {
        return success(queryService.queryCoreTxById(vo));
    }

    public RespData<List<BlockVO>> queryBlockListByPage(QueryBlockVO vo) {
        return success(queryService.queryBlockListByPage(vo));
    }

    public RespData<BlockVO> queryBlockByHeight(QueryBlockByHeightVO vo) {
        return success(queryService.queryBlockByHeight(vo));
    }
}
