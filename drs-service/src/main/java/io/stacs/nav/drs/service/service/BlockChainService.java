package io.stacs.nav.drs.service.service;

import io.stacs.nav.drs.api.model.Policy;
import io.stacs.nav.drs.api.model.RsDomain;
import io.stacs.nav.drs.api.model.bd.BusinessDefine;
import io.stacs.nav.drs.api.model.block.BlockVO;
import io.stacs.nav.drs.api.model.permission.PermissionInfoVO;
import io.stacs.nav.drs.api.model.query.QueryBlockByHeightVO;
import io.stacs.nav.drs.api.model.query.QueryBlockVO;
import io.stacs.nav.drs.api.model.query.QueryTxListVO;
import io.stacs.nav.drs.api.model.query.QueryTxVO;
import io.stacs.nav.drs.api.model.tx.CoreTransactionVO;
import io.stacs.nav.drs.service.network.BlockChainFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.stacs.nav.drs.api.exception.DappError.DRS_NETWORK_COMMON_ERROR;
import static io.stacs.nav.drs.api.exception.DappException.newError;

/**
 * @author liuyu
 * @date 2019-11-25
 */
@Service public class BlockChainService {
    @Autowired BlockChainFacade blockChainFacade;
    //TODO:use cache

    /**
     * query bd info by code
     */
    public BusinessDefine queryBDByCode(String bdCode) {
        return blockChainFacade.queryBDInfoByCode(bdCode).orElseThrow(newError(DRS_NETWORK_COMMON_ERROR));
    }

    /**
     * query all bd info
     */
    public List<BusinessDefine> queryAllBDInfo(String bdCode) {
        return blockChainFacade.queryBDInfo(bdCode).orElseThrow(newError(DRS_NETWORK_COMMON_ERROR));
    }

    /**
     * query all domain
     */
    public List<RsDomain> queryAllDomains() {
        return blockChainFacade.queryAllDomains().orElseThrow(newError(DRS_NETWORK_COMMON_ERROR));
    }

    /**
     * query all policy
     */
    public List<Policy> queryAllPolicy() {
        return blockChainFacade.queryAllPolicyList().orElseThrow(newError(DRS_NETWORK_COMMON_ERROR));
    }

    /**
     * query all permission list
     */
    public List<PermissionInfoVO> queryPermissionList() {
        return blockChainFacade.queryPermissionList().orElseThrow(newError(DRS_NETWORK_COMMON_ERROR));
    }

    public Long queryCurrentHeight() {
        return blockChainFacade.queryCurrentHeight().orElseThrow(newError(DRS_NETWORK_COMMON_ERROR));
    }

    public List<CoreTransactionVO> queryCoreTxListByPage(QueryTxListVO vo) {
        return blockChainFacade.queryCoreTxListByPage(vo).orElseThrow(newError(DRS_NETWORK_COMMON_ERROR));
    }

    public CoreTransactionVO queryCoreTxById(QueryTxVO vo) {
        return blockChainFacade.queryCoreTxById(vo).orElseThrow(newError(DRS_NETWORK_COMMON_ERROR));
    }

    public List<BlockVO> queryBlockListByPage(QueryBlockVO vo) {
        return blockChainFacade.queryBlockListByPage(vo).orElseThrow(newError(DRS_NETWORK_COMMON_ERROR));
    }

    public BlockVO queryBlockByHeight(QueryBlockByHeightVO vo) {
        return blockChainFacade.queryBlockByHeight(vo).orElseThrow(newError(DRS_NETWORK_COMMON_ERROR));
    }
}