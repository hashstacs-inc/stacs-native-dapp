package io.stacs.nav.drs.service.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sofa.ark.container.registry.PluginServiceProvider;
import com.alipay.sofa.ark.spi.model.Plugin;
import com.alipay.sofa.ark.spi.service.ArkInject;
import com.alipay.sofa.ark.spi.service.plugin.PluginManagerService;
import com.alipay.sofa.ark.spi.service.registry.RegistryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.stacs.nav.drs.api.IQueryService;
import io.stacs.nav.drs.api.model.ContractVO;
import io.stacs.nav.drs.api.model.TransactionVO;
import io.stacs.nav.drs.api.model.bd.BusinessDefine;
import io.stacs.nav.drs.api.model.bd.FunctionDefine;
import io.stacs.nav.drs.api.model.block.BlockHeaderVO;
import io.stacs.nav.drs.api.model.block.BlockVO;
import io.stacs.nav.drs.api.model.query.*;
import io.stacs.nav.drs.service.constant.Constants;
import io.stacs.nav.drs.service.dao.BlockDao;
import io.stacs.nav.drs.service.dao.ContractDao;
import io.stacs.nav.drs.service.dao.TransactionDao;
import io.stacs.nav.drs.service.dao.po.BlockPO;
import io.stacs.nav.drs.service.dao.po.BusinessDefinePO;
import io.stacs.nav.drs.service.utils.AmountUtil;
import io.stacs.nav.drs.service.utils.BeanConvertor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.stacs.nav.drs.service.utils.AmountUtil.clearNoUseZeroForBigDecimal;
import static io.stacs.nav.drs.service.utils.AmountUtil.transContractAmount2RSAmount;

/**
 * @author suimi
 * @date 2019/10/30
 */
@Slf4j @Service public class QueryService implements IQueryService, InitializingBean {

    @ArkInject RegistryService registryService;

    @ArkInject PluginManagerService pluginManagerService;

    @Autowired BlockChainService blockService;

    @Autowired(required = false) BlockDao blockDao;
    @Autowired(required = false) TransactionDao txDao;
    @Autowired(required = false) ContractDao contractDao;



    @Override public Long queryCurrentHeight() {
        return Long.valueOf(blockService.queryCurrentHeight().toString());
    }

    @Override public TransactionVO queryTxById(QueryTxVO vo) {
        TransactionVO po = txDao.queryByTxId(vo.getTxId());
        //when tx bdType is contract or assets,show contractName and contractAddress
        if(po.getBdType().equals("contract") || po.getBdType().equals("assets")){
            JSONArray arr = JSONArray.parseArray(po.getActionDatas());
            JSONObject o = arr.getJSONObject(0);
            String str = o.getString("to");
            if(StringUtils.isNotEmpty(str)) {
                ContractVO contractVO = contractDao.queryByAddress(str);
                if(contractVO != null) {
                    po.setContractName(contractVO.getName());
                    po.setContractAddress(contractVO.getAddress());
                }
            }
        }
        return po;
    }

    @Override public BlockVO queryTxByHeight(Long height) {
        BlockPO po = blockDao.queryByHeight(height);
        BlockVO blockVO = BeanConvertor.convertBean(po, BlockVO.class);
        blockVO.setBlockTime(po.getBlockTime().toString());
        blockVO.setMaxHeight(blockDao.getMaxHeight());
        return blockVO;
    }

    @Override public io.stacs.nav.drs.api.model.PageInfo<TransactionVO> queryTx(QueryTxListVO vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        io.stacs.nav.drs.api.model.PageInfo<TransactionVO> pageInfo = BeanConvertor.convertBean(
            PageInfo.of(txDao.queryTxWithCondition(vo)), io.stacs.nav.drs.api.model.PageInfo.class);
        //handler functionNames
        pageInfo.getList().forEach(item -> {
            convertFunctionNames(item);
        });
        return pageInfo;
    }
    private void convertFunctionNames(TransactionVO po){
        List<JSONObject> actionList = JSONArray.parseArray(po.getActionDatas(),JSONObject.class);
        List<String> fns = actionList.stream().map(action -> action.getString("functionName")).collect(Collectors.toList());

    }
    @Override public io.stacs.nav.drs.api.model.PageInfo<BlockVO> queryBlocks(QueryBlockVO vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        io.stacs.nav.drs.api.model.PageInfo<BlockVO> pageInfo = BeanConvertor.convertBean(
            PageInfo.of(blockDao.queryByCond(vo)), io.stacs.nav.drs.api.model.PageInfo.class);
        return pageInfo;
    }

    @Override public List<ContractVO> queryContracts(QueryContractVO vo) {
        return contractDao.queryByCond(vo);
    }

    @Override public String queryBalance(QueryBalanceVO vo) {

        ContractQueryRequest request = new ContractQueryRequest();
        request.setAddress(vo.getContract());
        request.setMethodSignature("(uint256) balanceOf(address)");
        request.setParameters(new String[] {vo.getIdentity()});
        if (vo.getHeight() != null && vo.getHeight() != 0L) {
            request.setBlockHeight(vo.getHeight());
        }
        Object balance = blockService.queryContract(request).get(0);
        return Optional.ofNullable(balance).map(b ->{
            BigDecimal amount = clearNoUseZeroForBigDecimal(transContractAmount2RSAmount(b.toString(),AmountUtil.DEFAULT_DECIMALS)) ;
            return amount.toPlainString();
        }).orElse("0");
    }

    @Override public String queryContract(ContractQueryRequest vo) {
        return blockService.queryContract(vo).toJSONString();
    }

    @Override public BlockHeaderVO queryBlockByHeight(QueryBlockByHeightVO vo) {
        return blockService.queryBlockByHeight(vo);
    }

    @Override public BusinessDefine queryBDByCode(String bdCode) {
        BusinessDefinePO po = blockService.queryBDByCode(bdCode);
        BusinessDefine bd = BeanConvertor.convertBean(po, BusinessDefine.class);
        bd.setFunctions(JSONArray.parseArray(po.getFunctions(), FunctionDefine.class));
        return bd;
    }

    @Override public List<BusinessDefine> queryAllBDInfo(String bdCode) {
        List<BusinessDefinePO> businessDefinePOS = blockService.queryAllBDInfo(bdCode);
        List<BusinessDefine> collect = businessDefinePOS.stream().map(e -> {
            BusinessDefine vo = BeanConvertor.convertBean(e, BusinessDefine.class);
            vo.setFunctions(JSONArray.parseArray(e.getFunctions(), FunctionDefine.class));
            return vo;
        }).collect(Collectors.toList());
        return collect;
    }

    // @formatter:off
    @Override public void afterPropertiesSet() {
        Plugin plugin = pluginManagerService.getPluginByName(Constants.SERVICE_NAME);
        if (plugin == null) {
            log.warn("init plugin is fail,get plugin is null,name:{}", Constants.SERVICE_NAME);
            return;
        }
        registryService.publishService(
            IQueryService.class,
            this,
            new PluginServiceProvider(plugin));
    }
    // @formatter:on

}
