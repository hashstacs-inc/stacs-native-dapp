package io.stacs.nav.drs.service.service;

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
import io.stacs.nav.drs.api.model.block.BlockHeaderVO;
import io.stacs.nav.drs.api.model.block.BlockVO;
import io.stacs.nav.drs.api.model.query.*;
import io.stacs.nav.drs.service.constant.Constants;
import io.stacs.nav.drs.service.dao.BlockDao;
import io.stacs.nav.drs.service.dao.ContractDao;
import io.stacs.nav.drs.service.dao.TransactionDao;
import io.stacs.nav.drs.service.dao.po.BlockPO;
import io.stacs.nav.drs.service.utils.BeanConvertor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author suimi
 * @date 2019/10/30
 */
@Slf4j @Service public class QueryService implements IQueryService, InitializingBean {

    @ArkInject RegistryService registryService;

    @ArkInject PluginManagerService pluginManagerService;

    @Autowired BlockChainService blockService;
    @Autowired BlockDao blockDao;
    @Autowired TransactionDao txDao;
    @Autowired ContractDao contractDao;



    public Long queryCurrentHeight() {
        return Long.valueOf(blockService.queryCurrentHeight().toString());
    }

    @Override public TransactionVO queryTxById(QueryTxVO vo) {
        return txDao.queryByTxId(vo.getTxId());
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
        return pageInfo;
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
        return Optional.ofNullable(blockService.queryContract(request).get(0)).map(Object::toString).orElse("0");
    }

    @Override public String queryContract(ContractQueryRequest vo) {
        return blockService.queryContract(vo).toJSONString();
    }

    @Override public BlockHeaderVO queryBlockByHeight(QueryBlockByHeightVO vo) {
        return blockService.queryBlockByHeight(vo);
    }

    @Override public BusinessDefine queryBDByCode(String bdCode) {
        return blockService.queryBDByCode(bdCode);
    }

    @Override public List<BusinessDefine> queryAllBDInfo(String bdCode) {
        return blockService.queryAllBDInfo(bdCode);
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
