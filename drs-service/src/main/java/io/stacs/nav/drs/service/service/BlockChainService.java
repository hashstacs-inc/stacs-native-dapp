package io.stacs.nav.drs.service.service;

import io.stacs.nav.drs.api.model.Policy;
import io.stacs.nav.drs.api.model.RsDomain;
import io.stacs.nav.drs.api.model.bd.BusinessDefine;
import io.stacs.nav.drs.api.model.permission.PermissionInfoVO;
import io.stacs.nav.drs.api.model.query.QueryTxListVO;
import io.stacs.nav.drs.api.model.tx.CoreTransactionVO;
import io.stacs.nav.drs.service.network.BlockChainFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static io.stacs.nav.drs.api.exception.DappError.BD_NOT_FIND_ERROR;
import static io.stacs.nav.drs.api.exception.DappError.DRS_NET_WORK_COMMON_ERROR;
import static io.stacs.nav.drs.api.exception.DappException.newError;

/**
 * @author liuyu
 * @description
 * @date 2019-11-25
 */
@Service public class BlockChainService {
    @Autowired BlockChainFacade blockChainFacade;
    //TODO:use cache

    /**
     * query bd info by code
     */
    public BusinessDefine queryBDByCode(String bdCode) {
        return blockChainFacade.queryBDInfoByCode(bdCode).orElseThrow(newError(BD_NOT_FIND_ERROR));
    }

    /**
     * query all bd info
     */
    public List<BusinessDefine> queryAllBDInfo(String bdCode) {
        return blockChainFacade.queryBDInfo(bdCode).orElseThrow(newError(BD_NOT_FIND_ERROR));
    }

    /**
     * query all domain
     */
    public List<RsDomain> queryAllDomains() {
        Optional<List<RsDomain>> domains = blockChainFacade.queryAllDomains();
        return domains.orElse(null);
    }

    /**
     * query all policy
     */
    public List<Policy> queryAllPolicy() {
        Optional<List<Policy>> policies = blockChainFacade.queryAllPolicyList();
        return policies.orElse(null);
    }

    /**
     * query all permission list
     */
    public List<PermissionInfoVO> queryPermissionList() {
        Optional<List<PermissionInfoVO>> policies = blockChainFacade.queryPermissionList();
        return policies.orElse(null);
    }

    public Long queryCurrentHeight() {
        return blockChainFacade.queryCurrentHeight().orElseThrow(newError(DRS_NET_WORK_COMMON_ERROR));
    }

    public List<CoreTransactionVO> queryCoreTxList(QueryTxListVO vo) {
        return blockChainFacade.queryCoreTxListByPage(vo).orElseThrow(newError(DRS_NET_WORK_COMMON_ERROR));
    }
}
