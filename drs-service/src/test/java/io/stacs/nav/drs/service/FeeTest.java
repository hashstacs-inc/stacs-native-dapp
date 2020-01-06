package io.stacs.nav.drs.service;

import com.alibaba.fastjson.JSON;
import io.stacs.nav.crypto.StacsECKey;
import io.stacs.nav.crypto.utils.IdGenerator;
import io.stacs.nav.drs.api.model.BaseTxVO;
import io.stacs.nav.drs.api.model.fee.FeeConfigVO;
import io.stacs.nav.drs.api.model.staking.StakingCurrencyConfigVO;
import io.stacs.nav.drs.api.model.staking.StakingDomainVO;
import io.stacs.nav.drs.api.model.task.TaskConfigVO;
import io.stacs.nav.drs.api.model.task.TaskTypeEnum;
import io.stacs.nav.drs.api.utils.OkHttpClientManager;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuyu
 * @description
 * @date 2019-12-31
 */
public class FeeTest {
    private static String DRS_URL = "http://10.200.174.52:30010/";
    //should auth-permission:RS
    //177f03aefabb6dfc07f189ddf6d0d48c2f60cdbf
    private static String priKey = "bbb43be030237c818bea2a5b808e872f432d1e83e6776f88b66a30d00956188c";
    private static StacsECKey ecKey = StacsECKey.fromPrivate(Hex.decode(priKey));

    private void submit(String priKey, BaseTxVO param) throws IOException {
        Map<String, Object> bdvo = new HashMap<>();
        bdvo.put("functionName", param.getMethodSign());
        bdvo.put("param", param);
        bdvo.put("privateKey", priKey);
        String json = JSON.toJSONString(bdvo);
        System.out.println("request:\n" + json);
        String res = OkHttpClientManager.postAsString(DRS_URL + "bd/submit", json, 60 * 1000L);
        System.out.println("response:\n" + res);
    }

    @Test public void testFeeConfig() throws IOException {
        FeeConfigVO vo = new FeeConfigVO();
        vo.setTxId(IdGenerator.generate64TxId("fee_config_id_" + System.currentTimeMillis()));
        vo.setBdCode("SystemBD");
        vo.setSubmitter(ecKey.getHexAddress());
        //contract address--require publish
        vo.setCurrency("FEE_COIN");
        //fee received address --require identity setting
        vo.setReceiveAddr("ef1d8935b0fb015242b024e05220c72f44ff5e6e");
        submit(priKey, vo);
    }

    @Test public void testConfigStakingDomain() throws IOException {
        StakingDomainVO vo = new StakingDomainVO();
        vo.setTxId(IdGenerator.generate64TxId("config_staking_domain_id_" + System.currentTimeMillis()));
        vo.setBdCode("SystemBD");
        vo.setSubmitter(ecKey.getHexAddress());
        //domain id
        vo.setDomainId("");
        vo.setStakingAddress("");
        vo.setParticipationAddress("xx");
        submit(priKey, vo);
    }

    @Test public void testConfigStakingCurrency() throws IOException {
        StakingCurrencyConfigVO vo = new StakingCurrencyConfigVO();
        vo.setTxId(IdGenerator.generate64TxId("config_staking_currency_id_" + System.currentTimeMillis()));
        vo.setBdCode("SystemBD");
        vo.setSubmitter(ecKey.getHexAddress());

        vo.setStakingCurrency("");
        submit(priKey, vo);
    }

    @Test public void testStakingTaskConfig() throws IOException {
        TaskConfigVO vo = new TaskConfigVO();
        vo.setTxId(IdGenerator.generate64TxId("config_staking_task_id_" + System.currentTimeMillis()));
        vo.setBdCode("SystemBD");
        vo.setSubmitter(ecKey.getHexAddress());

        vo.setType(TaskTypeEnum.BONUS_TYPE.getType());
        vo.setCronExpression("");
        submit(priKey, vo);
    }
}
