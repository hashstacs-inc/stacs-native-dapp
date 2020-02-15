package io.stacs.nav.dapp.core.upgrade;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.List;

/**
 * @author liuyu
 * @description
 * @date 2020-02-14
 */
@Slf4j public class UpgradeScanner {
    private final static String UPGRADE_FOLDER_PATH = "classpath:upgrade/%s/*.sql";

    /**
     * scan upgrade file info
     *
     * @return
     * @throws IOException
     */
    public static List<UpgradeInfo> scan(SQLTypeEnum typeEnum) throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(String.format(UPGRADE_FOLDER_PATH,typeEnum.name()));
        if (resources.length == 0) {
            return null;
        }
        List<UpgradeInfo> upgradeInfoList = Lists.newArrayList();
        for (Resource res : resources) {
            UpgradeInfo info = new UpgradeInfo();
            String fileName = res.getFilename();
            if (fileName != null) {
                int dotIndex = fileName.indexOf(".");
                int versionCode = Integer.parseInt(fileName.substring(0, dotIndex));
                info.setVersionCode(versionCode);
            }
            info.setResource(res);
            upgradeInfoList.add(info);
        }
        return upgradeInfoList;
    }
}
