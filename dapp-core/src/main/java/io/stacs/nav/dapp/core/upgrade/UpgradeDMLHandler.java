package io.stacs.nav.dapp.core.upgrade;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * @author liuyu
 * @description
 * @date 2020-02-14
 */
@Component @Slf4j public class UpgradeDMLHandler {
    @Autowired TransactionTemplate txRequired;
    @Autowired JdbcTemplate jdbcTemplate;

    /**
     * execute upgrade DML
     *
     * @param
     */
    public void handler(int originalVersionCode) throws IOException {
        log.info("[handlerDML]start upgrade,originalVersionCode:{}", originalVersionCode);
        if (jdbcTemplate == null) {
            log.warn("[handlerDML]JdbcTemplate is null");
            return;
        }
        if (txRequired == null) {
            log.warn("[handlerDML]TransactionTemplate is null");
            return;
        }
        List<UpgradeInfo> upgradeInfoList = null;
        try {
            upgradeInfoList = UpgradeScanner.scan(SQLTypeEnum.DML);
        } catch (Throwable e) {
            log.warn("[handlerDML]scan upgrade file has error", e);
        }
        if (CollectionUtils.isEmpty(upgradeInfoList)) {
            log.info("[handlerDML]upgrade info is empty");
            return;
        }
        //sort by version code
        upgradeInfoList.sort(Comparator.comparingInt(UpgradeInfo::getVersionCode));
        int size = upgradeInfoList.size();
        log.info("[handlerDML]start upgrade,file.size:{}", size);
        int index = originalVersionCode;
        log.info("[handlerDML]start upgrade,startIndex:{}", index);
        List<String> dmlSqlList = Lists.newArrayList();
        for (; index < size; index++) {
            UpgradeInfo upgradeInfo = upgradeInfoList.get(index);
            dmlSqlList.add(IOUtils.toString(upgradeInfo.getResource().getInputStream(), "UTF-8"));
        }
        log.info("[handlerDML]start execute sql");
        txRequired.execute(transactionStatus -> {
            dmlSqlList.forEach(sql -> {
                jdbcTemplate.execute(sql);
            });
            return null;
        });
        log.info("[handlerDML]upgrade is success");
    }
}
