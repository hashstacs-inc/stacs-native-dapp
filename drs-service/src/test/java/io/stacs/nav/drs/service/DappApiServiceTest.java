package io.stacs.nav.drs.service;

import com.alipay.sofa.ark.springboot.runner.ArkBootRunner;
import io.stacs.nav.drs.ConfigWithoutDataSource;
import io.stacs.nav.drs.api.model.bd.BusinessDefine;
import io.stacs.nav.drs.api.model.query.QueryBlockVO;
import io.stacs.nav.drs.api.model.query.QueryTxListVO;
import io.stacs.nav.drs.service.service.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author liuyu
 * @description
 * @date 2019-11-25
 */
@Slf4j @RunWith(ArkBootRunner.class) @SpringBootTest(classes = ConfigWithoutDataSource.class)
public class DappApiServiceTest {

    @Autowired private QueryService queryService;

    @Test public void test1() {
        QueryBlockVO vo = new QueryBlockVO();
        vo.setHeight(1L);
        queryService.queryBlocks(vo);

    }

    @Test public void test2() {
        QueryTxListVO vo = new QueryTxListVO();
        vo.setPageSize(10);
        vo.setPageNum(1);
        queryService.queryTx(vo);
    }

    @Test public void test() {
        QueryService queryService = new QueryService();
        BusinessDefine bd = new BusinessDefine();
        bd.setTxId("tx_id");
        bd.setSubmitter("aaa");
        String str = queryService.generateSignature(bd);
        System.out.println(str);
    }
}
