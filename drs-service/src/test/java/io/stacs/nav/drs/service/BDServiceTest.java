package io.stacs.nav.drs.service;

import com.alibaba.fastjson.JSON;
import com.alipay.sofa.ark.springboot.runner.ArkBootRunner;
import com.google.common.collect.Lists;
import io.stacs.nav.drs.ConfigWithoutDataSource;
import io.stacs.nav.drs.api.model.RespData;
import io.stacs.nav.drs.api.model.bd.BusinessDefine;
import io.stacs.nav.drs.api.model.bd.FunctionDefine;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author liuyu
 * @description
 * @date 2019-11-28
 */
@Slf4j @RunWith(ArkBootRunner.class) @SpringBootTest(classes = ConfigWithoutDataSource.class)
public class BDServiceTest {

    @Test public void test() {
        RespData<List<BusinessDefine>> respData = new RespData<>();
        BusinessDefine bd = new BusinessDefine();
        bd.setBdCode("SysBD");
        bd.setBdType("system");
        bd.setBdVersion("1.0");
        bd.setInitPermission("audit");
        bd.setInitPolicy("INIT_SYS");
        List<FunctionDefine> functionDefineList = Lists.newArrayList();
        String[] names = new String[] {"IDENTITY_SETTING", "BD_PUBLISH", "PERMISSION_REGISTER", "AUTHORIZE_PERMISSION",
            "CANCEL_PERMISSION", "REGISTER_POLICY", "MODIFY_POLICY", "REGISTER_RS", "CANCEL_RS", "CA_AUTH", "CA_CANCEL",
            "CA_UPDATE", "NODE_JOIN", "NODE_LEAVE", "SYSTEM_PROPERTY", "IDENTITY_BD_MANAGE", "KYC_SETTING",
            "SET_FEE_CONFIG", "SET_FEE_RULE", "SAVE_ATTESTATION", "BUILD_SNAPSHOT"};
        String[] descs =
            new String[] {"Identity设置", "BD发布", "Permission注册", "Permission授权", "Permission撤销授权", "注册policy",
                "修改policy", "注册RS", "撤销RS", "CA认证", "CA撤销", "CA更新", "节点加入", "节点退出", "系统属性配置",
                "Identity BD 管理（froze/unfroze）", "identity kyc 设置", "手续费设置：合约地址 & 收取地址", "手续费费率配置", "保存存证", "打快照"};

        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            FunctionDefine fd = new FunctionDefine();
            fd.setName(name);
            fd.setMethodSign(name);
            fd.setExecPolicy(name);
            fd.setType("SyetemAction");
            fd.setExecPermission("RS");
            fd.setDesc(descs[i]);
            functionDefineList.add(fd);
        }
        // bd.setFunctions(functionDefineList);
        List<BusinessDefine> list = Lists.newArrayList(bd);
        respData.setData(list);
        System.out.println(JSON.toJSONString(respData));
    }
}
