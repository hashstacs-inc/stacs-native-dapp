package io.stacs.nav.drs.service;

import com.alipay.sofa.ark.springboot.runner.ArkBootRunner;
import io.stacs.nav.drs.ConfigWithoutDataSource;
import io.stacs.nav.drs.api.ISignatureService;
import io.stacs.nav.drs.api.model.PageInfo;
import io.stacs.nav.drs.api.model.TransactionVO;
import io.stacs.nav.drs.api.model.bd.BusinessDefine;
import io.stacs.nav.drs.api.model.query.*;
import io.stacs.nav.drs.service.scheduler.BlockCallbackProcessSchedule;
import io.stacs.nav.drs.service.scheduler.FailoverSchedule;
import io.stacs.nav.drs.service.service.BlockChainService;
import io.stacs.nav.drs.service.service.QueryService;
import io.stacs.nav.drs.service.service.SignatureService;
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
    @Autowired private BlockChainService blockChainService;
    @Autowired private FailoverSchedule failoverSchedule;
    @Autowired private BlockCallbackProcessSchedule blockCallbackProcessSchedule;

    @Test public void test1() {
        QueryBlockVO vo = new QueryBlockVO();
        vo.setHeight(1L);
        queryService.queryBlocks(vo);

    }

    @Test public void test2() {
        QueryTxListVO vo = new QueryTxListVO();
        vo.setPageSize(10);
        vo.setPageNum(1);
        PageInfo<TransactionVO> transactionPOPageInfo = queryService.queryTx(vo);
        System.out.println(transactionPOPageInfo.getTotal());
    }

    @Test public void testTxd() {
        QueryTxVO vo = new QueryTxVO();
        vo.setTxId("1da25d2d-92c9-4544-84e6-735fc7c61d08");
        TransactionVO resp = queryService.queryTxById(vo);
        System.out.println(resp);
    }

    @Test public void testBalanace() {
        QueryBalanceVO vo = new QueryBalanceVO();
        vo.setIdentity("e7f048dc7dfadb43338b2b292c2a26a1b374bb2c");
        vo.setContract("4b4679921c2951dab306efaa15e16bac97e80556");
        System.out.println(queryService.queryBalance(vo));
    }

    @Test public void testContract() {
        QueryContractVO vo = new QueryContractVO();
        vo.setBdType("asserts");
        System.out.println(queryService.queryContracts(vo));
    }

    @Test public void test3() {
        Integer height = blockChainService.queryCurrentHeight();
        System.out.println(height);
    }

    @Test public void test4() {
        failoverSchedule.exe();
    }

    @Test public void test5() {
        blockCallbackProcessSchedule.exe();
    }

    @Test public void test() {
        ISignatureService queryService = new SignatureService();
        BusinessDefine bd = new BusinessDefine();
        bd.setTxId("tx_id");
        bd.setSubmitter("aaa");
        String str = queryService.generateSignature(bd);
        System.out.println(str);
    }
}
