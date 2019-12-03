package io.stacs.nav.drs.service;

import io.stacs.nav.drs.api.model.bd.BusinessDefine;
import io.stacs.nav.drs.service.service.QueryService;
import org.junit.Test;

/**
 * @author liuyu
 * @description
 * @date 2019-11-25
 */
public class DappApiServiceTest {

    @Test
    public void test(){
        QueryService queryService = new QueryService();
        BusinessDefine bd = new BusinessDefine();
        bd.setTxId("tx_id");
        bd.setSubmitter("aaa");
        String str = queryService.generateSignature(bd);
        System.out.println(str);
    }
}
