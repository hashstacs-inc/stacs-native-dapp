package io.stacs.nav.drs.boot.service.dappstore;

import com.alibaba.fastjson.JSON;
import com.alipay.sofa.common.utils.StringUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.stacs.nav.drs.boot.bo.Dapp;
import io.stacs.nav.drs.boot.service.dapp.IDappService;
import io.stacs.nav.drs.boot.vo.AppProfileVO;
import io.stacs.nav.drs.service.config.DrsConfig;
import io.stacs.nav.drs.service.network.BlockChainFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author suimi
 * @date 2019/11/1
 */
@Service @Slf4j public class DappStoreService {
    @Autowired private DrsConfig drsConfig;
    @Autowired private BlockChainFacade blockChainFacade;
    @Autowired private IDappService dappService;

    private static final Cache<String, List<AppProfileVO>> DAPP_CACHE =
        CacheBuilder.newBuilder().initialCapacity(100).maximumSize(10000).expireAfterWrite(60 * 60, TimeUnit.SECONDS)
            .build();

    /**
     * 获取app 信息
     *
     * @return
     * @throws IOException
     */
    public List<AppProfileVO> queryApps() throws IOException {
        List<AppProfileVO> list = queryFromAppStore();
        list.forEach(v -> {
            Dapp dapp = dappService.findByAppName(v.getName());
            if (dapp != null) {
                v.setStatus(dapp.getStatus().name());
            }
        });
        return list;
    }

    private List<AppProfileVO> queryFromAppStore() throws IOException {
        String storePath = drsConfig.getDappStorePath();
        log.info("[queryApps]storePath:{}", storePath);
        List<AppProfileVO> appProfileVOList = DAPP_CACHE.getIfPresent(storePath);
        if (CollectionUtils.isNotEmpty(appProfileVOList)) {
            return appProfileVOList;
        }
        List<AppProfileVO> list = null;
        if (!StringUtil.isEmpty(storePath)) {
            Optional<String> optional = blockChainFacade.sendGet(storePath,
                resp -> resp.isSuccessful() ? Optional.of(String.valueOf(resp.getData())) :
                    Optional.empty());
            String json = optional.orElse(null);
            list = JSON.parseArray(json,AppProfileVO.class);
        }
        if (CollectionUtils.isEmpty(list)) {
            log.warn("[queryApps]list is empty");
            return null;
        }
        DAPP_CACHE.put(storePath, list);
        return list;
    }

    /**
     * 根据name 获取dapp信息
     *
     * @param name
     * @return
     * @throws IOException
     */
    public AppProfileVO queryAppByName(String name) throws IOException {
        List<AppProfileVO> appProfileVOList = queryFromAppStore();
        if (CollectionUtils.isEmpty(appProfileVOList)) {
            appProfileVOList = queryApps();
        }
        if (CollectionUtils.isEmpty(appProfileVOList)) {
            return null;
        }
        for (AppProfileVO vo : appProfileVOList) {
            if (vo.getName().equals(name)) {
                return vo;
            }
        }
        return null;
    }
}
