package io.stacs.nav.drs.service;

import com.alibaba.fastjson.JSON;
import io.stacs.nav.crypto.StacsECKey;
import io.stacs.nav.crypto.utils.IdGenerator;
import io.stacs.nav.drs.api.model.fee.FeeConfigVO;
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

    @Test public void testFeeConfig() throws IOException {
        //需要先做permission
        String priKey = "bbb43be030237c818bea2a5b808e872f432d1e83e6776f88b66a30d00956188c";
        StacsECKey ecKey = StacsECKey.fromPrivate(Hex.decode(priKey));
        FeeConfigVO feeConfigVO = new FeeConfigVO();
        feeConfigVO.setTxId(IdGenerator.generate64TxId("fee_config_id_" + System.currentTimeMillis()));
        feeConfigVO.setBdCode("SystemBD");
        feeConfigVO.setSubmitter(ecKey.getHexAddress());

        //合约地址--需要提前发布合约
        feeConfigVO.setCurrency("FEE_COIN");
        //收取手续费的地址--需要做identity setting
        feeConfigVO.setReceiveAddr("ef1d8935b0fb015242b024e05220c72f44ff5e6e");

        Map<String, Object> bdvo = new HashMap<>();
        bdvo.put("functionName", feeConfigVO.getFunctionName());
        bdvo.put("param", feeConfigVO);
        bdvo.put("privateKey", priKey);
        String json = JSON.toJSONString(bdvo);
        System.out.println("request:\n" + json);
        String res = OkHttpClientManager.postAsString(DRS_URL + "bd/submit", json, 60 * 1000L);
        System.out.println("response:\n" + res);
    }
}
