package io.stacs.nav.drs.service.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.stacs.nav.drs.api.model.Policy;
import io.stacs.nav.drs.api.model.RespData;
import io.stacs.nav.drs.api.model.RsDomain;
import io.stacs.nav.drs.api.model.block.BlockHeaderVO;
import io.stacs.nav.drs.api.model.permission.PermissionInfoVO;
import io.stacs.nav.drs.api.model.query.*;
import io.stacs.nav.drs.api.model.tx.CoreTransactionVO;
import io.stacs.nav.drs.service.dao.PolicyDao;
import io.stacs.nav.drs.service.dao.po.BusinessDefinePO;
import io.stacs.nav.drs.service.dao.po.PolicyPO;
import io.stacs.nav.drs.service.network.BlockChainFacade;
import io.stacs.nav.drs.service.network.BlockChainHelper;
import io.stacs.nav.drs.service.vo.MethodParamVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.stacs.nav.drs.api.enums.ApiConstants.QueryApiEnum.CONTRACT_METHOD;
import static io.stacs.nav.drs.api.exception.DappError.DRS_NETWORK_COMMON_ERROR;
import static io.stacs.nav.drs.api.exception.DappException.newError;

/**
 * @author liuyu
 * @date 2019-11-25
 */
@Service @Slf4j public class BlockChainService {
    @Autowired BlockChainFacade blockChainFacade;
    @Autowired BlockChainHelper blockChainHelper;
    @Autowired PolicyDao policyDao;
    //TODO:use cache

    /**
     * query bd info by code
     */
    public BusinessDefinePO queryBDByCode(String bdCode) {
        return blockChainFacade.queryBDInfoByCode(bdCode).orElseThrow(newError(DRS_NETWORK_COMMON_ERROR));
    }

    /**
     * query all bd info
     */
    public List<BusinessDefinePO> queryAllBDInfo(String bdCode) {
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
     * query policy
     */
    public PolicyPO queryPolicy(String policyId) {
        return policyDao.queryByPolicyId(policyId);
    }

    /**
     * query all permission list
     */
    public List<PermissionInfoVO> queryPermissionList() {
        return blockChainFacade.queryPermissionList().orElseThrow(newError(DRS_NETWORK_COMMON_ERROR));
    }

    public Integer queryCurrentHeight() {
        return blockChainFacade.queryCurrentHeight().orElseThrow(newError(DRS_NETWORK_COMMON_ERROR));
    }

    public List<CoreTransactionVO> queryCoreTxListByPage(QueryTxListVO vo) {
        return blockChainFacade.queryCoreTxListByPage(vo).orElseThrow(newError(DRS_NETWORK_COMMON_ERROR));
    }

    public CoreTransactionVO queryCoreTxById(QueryTxVO vo) {
        return blockChainFacade.queryCoreTxById(vo).orElseThrow(newError(DRS_NETWORK_COMMON_ERROR));
    }

    public List<BlockHeaderVO> queryBlockListByPage(QueryBlockVO vo) {
        return blockChainFacade.queryBlockListByPage(vo).orElseThrow(newError(DRS_NETWORK_COMMON_ERROR));
    }

    public List<JSONObject> queryBlocks(long startHeight, long endHeight) {
        return blockChainFacade.queryBlocks(startHeight, endHeight).orElseThrow(newError(DRS_NETWORK_COMMON_ERROR));
    }

    public BlockHeaderVO queryBlockByHeight(QueryBlockByHeightVO vo) {
        return blockChainFacade.queryBlockByHeight(vo).orElseThrow(newError(DRS_NETWORK_COMMON_ERROR));
    }

    public JSONArray queryContract(ContractQueryRequest vo) {
        return blockChainFacade.queryContract(vo).orElseThrow(newError(DRS_NETWORK_COMMON_ERROR));
    }

    public JSONObject queryMethodParam(MethodParamVO vo) {
        RespData<String> respData = blockChainHelper.post(CONTRACT_METHOD.getApi(), vo, String.class);
        if (respData.isSuccessful() && respData.getData() != null) {
            String json = respData.getData();
            log.info("[queryMethodParam]json:{}",json);
            return JSONObject.parseObject(json);
        }
        return null;
    }
}
