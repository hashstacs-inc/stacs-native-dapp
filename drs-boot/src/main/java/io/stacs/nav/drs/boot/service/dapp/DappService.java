package io.stacs.nav.drs.boot.service.dapp;

import com.google.common.collect.Lists;
import io.stacs.nav.drs.api.exception.DappError;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.boot.bo.Dapp;
import io.stacs.nav.drs.boot.dao.AppInfoDao;
import io.stacs.nav.drs.boot.dao.po.AppInfoPO;
import io.stacs.nav.drs.boot.enums.DappStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author liuyu
 * @description
 * @date 2019-11-05
 */
@Service @Slf4j public class DappService implements IDappService {
    @Autowired private AppInfoDao appInfoDao;

    @Override public Dapp save(Dapp app) {
        AppInfoPO po = new AppInfoPO();
        BeanUtils.copyProperties(app, po);
        po.setStatus(app.getStatus().name());
        try {
            appInfoDao.save(po);
            return app;
        } catch (DuplicateKeyException e) {
            log.error("save app info has duplicate key error", e);
            throw new DappException(DappError.IDEMPOTENT_ERROR);
        } catch (Throwable e) {
            log.error("save app info has other error", e);
            throw new DappException(DappError.DB_ERROR);
        }
    }

    @Override public Dapp findByAppName(String appName) {
        AppInfoPO po = appInfoDao.queryByAppName(appName);
        if (po == null) {
            return null;
        }
        Dapp dapp = new Dapp();
        BeanUtils.copyProperties(po, dapp);
        dapp.setStatus(DappStatus.fromCode(po.getStatus()));
        return dapp;
    }

    @Override public boolean isExists(String appName) {
        return appInfoDao.queryByAppName(appName) != null;
    }

    @Override public List<Dapp> findAll() {
        List<AppInfoPO> list = appInfoDao.queryAll();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<Dapp> dappList = Lists.newArrayList();
        list.forEach(v -> {
            Dapp dapp = new Dapp();
            BeanUtils.copyProperties(v, dapp);
            dapp.setStatus(DappStatus.fromCode(v.getStatus()));
            dappList.add(dapp);
        });
        return dappList;
    }

    @Override public void updateStatus(String appName, DappStatus toStatus, String runError) {
        int r = appInfoDao.updateStatus(appName, toStatus.name(), runError);
        if (r != 1) {
            log.error("update status is error,appName:{}", appName);
            throw new DappException(DappError.DAPP_UPDATE_STATUS_ERROR);
        }
    }

    @Override public int unInstall(String name) {
        return appInfoDao.unInstall(name);
    }

    @Override public List<Dapp> queryByNames(List<String> names) {
        List<AppInfoPO> appInfoPOList = appInfoDao.queryByNames(names);
        if(CollectionUtils.isEmpty(appInfoPOList)){
            return null;
        }
        List<Dapp> dappList = Lists.newArrayList();
        appInfoPOList.stream().forEach(v->{
            Dapp dapp = new Dapp();
            BeanUtils.copyProperties(v, dapp);
            dapp.setStatus(DappStatus.fromCode(v.getStatus()));
            dappList.add(dapp);
        });
        return dappList;
    }
}
