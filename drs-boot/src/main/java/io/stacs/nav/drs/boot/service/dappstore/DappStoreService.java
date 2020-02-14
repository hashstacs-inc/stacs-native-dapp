package io.stacs.nav.drs.boot.service.dappstore;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sofa.common.utils.StringUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.stacs.nav.drs.boot.bo.Dapp;
import io.stacs.nav.drs.boot.service.dapp.IDappService;
import io.stacs.nav.drs.boot.vo.AppProfileVO;
import io.stacs.nav.drs.service.config.DrsConfig;
import io.stacs.nav.drs.service.network.BlockChainFacade;
import io.stacs.nav.drs.service.utils.BeanConvertor;
import io.stacs.nav.drs.service.utils.config.ConfigListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author suimi
 * @date 2019/11/1
 */
@Service @Slf4j public class DappStoreService implements ConfigListener {
    private DrsConfig drsConfig;
    @Autowired private BlockChainFacade blockChainFacade;
    @Autowired private IDappService dappService;

    private static final Cache<String, List<AppProfileVO>> DAPP_CACHE =
        CacheBuilder.newBuilder().initialCapacity(100).maximumSize(100).expireAfterWrite(5 * 60, TimeUnit.SECONDS)
            .build();

    /**
     * query all app from appstore
     *
     * @return
     * @throws IOException
     */
    public List<AppProfileVO> queryApps() throws IOException {
        List<AppProfileVO> list = queryFromAppStore();
        if (CollectionUtils.isEmpty(list)) {
            log.warn("[queryApps]list is empty");
            return null;
        }
        List<String> names = list.stream().map(AppProfileVO::getName).collect(Collectors.toList());
        List<Dapp> dappList = dappService.queryByNames(names);
        if (CollectionUtils.isNotEmpty(dappList)) {
            Map<String, Dapp> dappMap = dappList.stream().collect(Collectors.toMap(Dapp::getName, v -> v));
            list.forEach(v -> {
                Dapp dapp = dappMap.get(v.getName());
                if (dapp != null) {
                    v.setStatus(dapp.getStatus().name());
                    //set hasUpgrade by versionCode
                    if (v.getVersionCode() > dapp.getVersionCode()) {
                        v.setHasUpgrade(true);
                    }
                }
            });
        }
        return list;
    }

    private List<AppProfileVO> queryFromAppStore() throws IOException {
        String storePath = drsConfig.getDappStorePath();
        log.info("[queryFromAppStore]storePath:{}", storePath);
        List<AppProfileVO> appProfileVOList = DAPP_CACHE.getIfPresent(storePath);
        if (CollectionUtils.isNotEmpty(appProfileVOList)) {
            return BeanConvertor.convertList(appProfileVOList, AppProfileVO.class);
        }
        List<AppProfileVO> list = null;
        if (!StringUtil.isEmpty(storePath)) {
            if (storePath.contains("http")) {
                Optional<String> optional = blockChainFacade.sendGet(storePath,
                    resp -> resp.isSuccessful() ? Optional.of(String.valueOf(resp.getData())) : Optional.empty());
                String json = optional.orElse(null);
                list = JSON.parseArray(json, AppProfileVO.class);
            } else {
                //from file
                File f = new File(storePath);
                String json = FileUtils.readFileToString(f, "utf-8");
                log.info("json of file:{}", json);
                JSONObject jsonObject = JSONObject.parseObject(json);
                list = JSON.parseArray(jsonObject.getJSONArray("data").toJSONString(), AppProfileVO.class);
            }
        }
        if (CollectionUtils.isEmpty(list)) {
            log.warn("[queryFromAppStore]list is empty");
            return null;
        }
        DAPP_CACHE.put(storePath, list);
        return BeanConvertor.convertList(list, AppProfileVO.class);
    }

    /**
     * query by app name
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

    @Override public <T> void updateNotify(T config) {
        if (config instanceof DrsConfig) {
            this.drsConfig = (DrsConfig)config;
        }
    }

    @Nonnull @Override public Predicate<Object> filter() {
        return obj -> obj instanceof DrsConfig;
    }
}
