package io.stacs.nav.drs.service;

import com.alipay.sofa.ark.springboot.runner.ArkBootRunner;
import io.stacs.nav.drs.ConfigWithoutDataSource;
import io.stacs.nav.drs.api.ISignatureService;
import io.stacs.nav.drs.api.model.PageInfo;
import io.stacs.nav.drs.api.model.TransactionVO;
import io.stacs.nav.drs.api.model.bd.BusinessDefine;
import io.stacs.nav.drs.api.model.query.*;
import io.stacs.nav.drs.service.dao.po.BusinessDefinePO;
import io.stacs.nav.drs.service.scheduler.BlockCallbackProcessSchedule;
import io.stacs.nav.drs.service.scheduler.FailoverSchedule;
import io.stacs.nav.drs.service.service.BDService;
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
    @Autowired private BDService bdService;
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

    @Test public void test6() {
        BusinessDefinePO systemBD = blockChainService.queryBDByCode("SystemBD");
        System.out.println(systemBD);
    }

    @Test public void test7() {
        // String str = "[\n" + "  {\n" + "    \"code\": \"SystemBD\",\n"
        //     + "    \"functions\": \"[{\\\"desc\\\":\\\"Identity Setting\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"IDENTITY_SETTING\\\",\\\"methodSign\\\":\\\"IDENTITY_SETTING\\\",\\\"name\\\":\\\"IDENTITY_SETTING\\\",\\\"signValue\\\":\\\"IDENTITY_SETTINGSystemActionIdentity SettingIDENTITY_SETTINGDEFAULTIDENTITY_SETTING\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"BD Specification\\\",\\\"execPermission\\\":\\\"RS\\\",\\\"execPolicy\\\":\\\"BD_PUBLISH\\\",\\\"methodSign\\\":\\\"BD_PUBLISH\\\",\\\"name\\\":\\\"BD_PUBLISH\\\",\\\"signValue\\\":\\\"BD_PUBLISHSystemActionBD SpecificationBD_PUBLISHRSBD_PUBLISH\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"Permission Register\\\",\\\"execPermission\\\":\\\"RS\\\",\\\"execPolicy\\\":\\\"PERMISSION_REGISTER\\\",\\\"methodSign\\\":\\\"PERMISSION_REGISTER\\\",\\\"name\\\":\\\"PERMISSION_REGISTER\\\",\\\"signValue\\\":\\\"PERMISSION_REGISTERSystemActionPermission RegisterPERMISSION_REGISTERRSPERMISSION_REGISTER\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"Authorize Permission\\\",\\\"execPermission\\\":\\\"RS\\\",\\\"execPolicy\\\":\\\"AUTHORIZE_PERMISSION\\\",\\\"methodSign\\\":\\\"AUTHORIZE_PERMISSION\\\",\\\"name\\\":\\\"AUTHORIZE_PERMISSION\\\",\\\"signValue\\\":\\\"AUTHORIZE_PERMISSIONSystemActionAuthorize PermissionAUTHORIZE_PERMISSIONRSAUTHORIZE_PERMISSION\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"Cancel Permission\\\",\\\"execPermission\\\":\\\"RS\\\",\\\"execPolicy\\\":\\\"CANCEL_PERMISSION\\\",\\\"methodSign\\\":\\\"CANCEL_PERMISSION\\\",\\\"name\\\":\\\"CANCEL_PERMISSION\\\",\\\"signValue\\\":\\\"CANCEL_PERMISSIONSystemActionCancel PermissionCANCEL_PERMISSIONRSCANCEL_PERMISSION\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"Register Policy\\\",\\\"execPermission\\\":\\\"RS\\\",\\\"execPolicy\\\":\\\"REGISTER_POLICY\\\",\\\"methodSign\\\":\\\"REGISTER_POLICY\\\",\\\"name\\\":\\\"REGISTER_POLICY\\\",\\\"signValue\\\":\\\"REGISTER_POLICYSystemActionRegister PolicyREGISTER_POLICYRSREGISTER_POLICY\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"Modify Policy\\\",\\\"execPermission\\\":\\\"RS\\\",\\\"execPolicy\\\":\\\"MODIFY_POLICY\\\",\\\"methodSign\\\":\\\"MODIFY_POLICY\\\",\\\"name\\\":\\\"MODIFY_POLICY\\\",\\\"signValue\\\":\\\"MODIFY_POLICYSystemActionModify PolicyMODIFY_POLICYRSMODIFY_POLICY\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"Register Rs\\\",\\\"execPermission\\\":\\\"RS\\\",\\\"execPolicy\\\":\\\"REGISTER_RS\\\",\\\"methodSign\\\":\\\"REGISTER_RS\\\",\\\"name\\\":\\\"REGISTER_RS\\\",\\\"signValue\\\":\\\"REGISTER_RSSystemActionRegister RsREGISTER_RSRSREGISTER_RS\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"Cancel Rs\\\",\\\"execPermission\\\":\\\"RS\\\",\\\"execPolicy\\\":\\\"CANCEL_RS\\\",\\\"methodSign\\\":\\\"RS_CANCEL\\\",\\\"name\\\":\\\"CANCEL_RS\\\",\\\"signValue\\\":\\\"CANCEL_RSSystemActionCancel RsRS_CANCELRSCANCEL_RS\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"CA Auth\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"CA_AUTH\\\",\\\"methodSign\\\":\\\"CA_AUTH\\\",\\\"name\\\":\\\"CA_AUTH\\\",\\\"signValue\\\":\\\"CA_AUTHSystemActionCA AuthCA_AUTHDEFAULTCA_AUTH\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"CA Cancel\\\",\\\"execPermission\\\":\\\"RS\\\",\\\"execPolicy\\\":\\\"CA_CANCEL\\\",\\\"methodSign\\\":\\\"CA_CANCEL\\\",\\\"name\\\":\\\"CA_CANCEL\\\",\\\"signValue\\\":\\\"CA_CANCELSystemActionCA CancelCA_CANCELRSCA_CANCEL\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"CA Update\\\",\\\"execPermission\\\":\\\"RS\\\",\\\"execPolicy\\\":\\\"CA_UPDATE\\\",\\\"methodSign\\\":\\\"CA_UPDATE\\\",\\\"name\\\":\\\"CA_UPDATE\\\",\\\"signValue\\\":\\\"CA_UPDATESystemActionCA UpdateCA_UPDATERSCA_UPDATE\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"Node Join\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"NODE_JOIN\\\",\\\"methodSign\\\":\\\"NODE_JOIN\\\",\\\"name\\\":\\\"NODE_JOIN\\\",\\\"signValue\\\":\\\"NODE_JOINSystemActionNode JoinNODE_JOINDEFAULTNODE_JOIN\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"Node Leave\\\",\\\"execPermission\\\":\\\"RS\\\",\\\"execPolicy\\\":\\\"NODE_LEAVE\\\",\\\"methodSign\\\":\\\"NODE_LEAVE\\\",\\\"name\\\":\\\"NODE_LEAVE\\\",\\\"signValue\\\":\\\"NODE_LEAVESystemActionNode LeaveNODE_LEAVERSNODE_LEAVE\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"System Property\\\",\\\"execPermission\\\":\\\"RS\\\",\\\"execPolicy\\\":\\\"SYSTEM_PROPERTY\\\",\\\"methodSign\\\":\\\"SYSTEM_PROPERTY\\\",\\\"name\\\":\\\"SYSTEM_PROPERTY\\\",\\\"signValue\\\":\\\"SYSTEM_PROPERTYSystemActionSystem PropertySYSTEM_PROPERTYRSSYSTEM_PROPERTY\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"Identity BD Management\\\",\\\"execPermission\\\":\\\"RS\\\",\\\"execPolicy\\\":\\\"IDENTITY_BD_MANAGE\\\",\\\"methodSign\\\":\\\"IDENTITY_BD_MANAGE\\\",\\\"name\\\":\\\"IDENTITY_BD_MANAGE\\\",\\\"signValue\\\":\\\"IDENTITY_BD_MANAGESystemActionIdentity BD ManagementIDENTITY_BD_MANAGERSIDENTITY_BD_MANAGE\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"KYC Setting\\\",\\\"execPermission\\\":\\\"RS\\\",\\\"execPolicy\\\":\\\"KYC_SETTING\\\",\\\"methodSign\\\":\\\"KYC_SETTING\\\",\\\"name\\\":\\\"KYC_SETTING\\\",\\\"signValue\\\":\\\"KYC_SETTINGSystemActionKYC SettingKYC_SETTINGRSKYC_SETTING\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"Config Fee\\\",\\\"execPermission\\\":\\\"RS\\\",\\\"execPolicy\\\":\\\"SET_FEE_CONFIG\\\",\\\"methodSign\\\":\\\"SET_FEE_CONFIG\\\",\\\"name\\\":\\\"SET_FEE_CONFIG\\\",\\\"signValue\\\":\\\"SET_FEE_CONFIGSystemActionConfig FeeSET_FEE_CONFIGRSSET_FEE_CONFIG\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"Set Fee Rule\\\",\\\"execPermission\\\":\\\"RS\\\",\\\"execPolicy\\\":\\\"SET_FEE_RULE\\\",\\\"methodSign\\\":\\\"SET_FEE_RULE\\\",\\\"name\\\":\\\"SET_FEE_RULE\\\",\\\"signValue\\\":\\\"SET_FEE_RULESystemActionSet Fee RuleSET_FEE_RULERSSET_FEE_RULE\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"Save Attestation\\\",\\\"execPermission\\\":\\\"RS\\\",\\\"execPolicy\\\":\\\"SAVE_ATTESTATION\\\",\\\"methodSign\\\":\\\"SAVE_ATTESTATION\\\",\\\"name\\\":\\\"SAVE_ATTESTATION\\\",\\\"signValue\\\":\\\"SAVE_ATTESTATIONSystemActionSave AttestationSAVE_ATTESTATIONRSSAVE_ATTESTATION\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"Build Snapshot\\\",\\\"execPermission\\\":\\\"RS\\\",\\\"execPolicy\\\":\\\"BUILD_SNAPSHOT\\\",\\\"methodSign\\\":\\\"BUILD_SNAPSHOT\\\",\\\"name\\\":\\\"BUILD_SNAPSHOT\\\",\\\"signValue\\\":\\\"BUILD_SNAPSHOTSystemActionBuild SnapshotBUILD_SNAPSHOTRSBUILD_SNAPSHOT\\\",\\\"type\\\":\\\"SystemAction\\\"}]\",\n"
        //     + "    \"createTime\": 1576142728967,\n" + "    \"bdType\": \"system\",\n" + "    \"name\": \"SystemBD\"\n"
        //     + "  }\n" + "]\n";
        // Gson gson = new Gson();
        // List<Object> objects = gson.fromJson(str, new TypeToken<List<BusinessDefine>>(){}.getType());
        // System.out.println(objects);
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
