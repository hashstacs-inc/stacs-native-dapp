package io.stacs.nav.dapp.sample.service;

import com.alipay.sofa.ark.spi.service.ArkInject;
import io.stacs.nav.crypto.StacsECKey;
import io.stacs.nav.drs.api.ISignatureService;
import io.stacs.nav.drs.api.ISubmitterService;
import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.RespData;
import io.stacs.nav.drs.api.model.permission.AuthPermissionVO;
import lombok.extern.slf4j.Slf4j;
import org.spongycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import static io.stacs.nav.drs.api.model.RespData.fail;
import static io.stacs.nav.drs.api.model.RespData.success;

/**
 * @author suimi
 * @date 2019/10/30
 */
@Service @Slf4j public class SampleService {

    @ArkInject ISubmitterService dappService;
    @ArkInject ISignatureService signatureService;
    @Autowired TransactionTemplate txRequired;
    @Autowired JdbcTemplate jdbcTemplate;


    public RespData<?> authPermission(AuthPermissionVO vo) {
        try {
            StacsECKey ecKey = StacsECKey.fromPrivate(Hex.decode(""));
            String signValue = signatureService.generateSignature(vo);
            vo.setSubmitter(ecKey.getHexAddress());
            vo.setSubmitterSign(ecKey.signMessage(signValue));
            dappService.authPermission(vo);
            return success();
        } catch (DappException e) {
            log.error("has error", e);
            return fail(e);
        }
    }

    public void testJdbc(){
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO `sample` (`tx_id` ,`policy_id`,`create_time`)\n"
            + "   values('tx_1123','policy_111',now());");
        sb.append("ALTER TABLE `sample` ADD COLUMN `c_1` varchar(1) DEFAULT NULL;");
        txRequired.execute((TransactionStatus transactionStatus) -> {
            jdbcTemplate.execute(sb.toString());
            return transactionStatus;
        });
    }
}
