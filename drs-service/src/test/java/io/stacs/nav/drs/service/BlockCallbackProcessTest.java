package io.stacs.nav.drs.service;

import com.alibaba.fastjson.JSON;
import io.stacs.nav.drs.api.model.TransactionPO;
import io.stacs.nav.drs.api.model.block.BlockVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

/**
 * @author dekuofa <br>
 * @date 2019-12-09 <br>
 */
// @Slf4j @RunWith(ArkBootRunner.class) @SpringBootTest(classes = ConfigWithoutDataSource.class)
@Slf4j public class BlockCallbackProcessTest {
    String message = "{\n" + "      \"blockHeader\": {\n"
        + "        \"blockHash\": \"233bae1379706d4e3996db72b69d5124fd90d40a212cc374a99615ae5774853e\",\n"
        + "        \"blockTime\": 1561939200000,\n" + "        \"height\": 1,\n"
        + "        \"previousHash\": \"D40114568F302846D8CFEDD04C87F30ABC9DBFE568F89E44F97FD581BA97BBC4\",\n"
        + "        \"stateRootHash\": {\n" + "          \"accountRootHash\": \"STACS_NO_TREE\",\n"
        + "          \"caRootHash\": \"2716364f7fb9484d8af035248443fef09a4edfec54605de64441a12aa9654c25\",\n"
        + "          \"contractRootHash\": \"STACS_NO_TREE\",\n"
        + "          \"policyRootHash\": \"6e844a64fd8f656bc6749f3213fa4939ad8072a51c08d3aae9718efccd7d104a\",\n"
        + "          \"rsRootHash\": \"57ecda40ae129ce91189d1a2855c67917b0629fbac82b9bef1d7b0d41a51caed\",\n"
        + "          \"txReceiptRootHash\": \"STACS_NO_TREE\",\n"
        + "          \"txRootHash\": \"ee70294721cadec2e841099033f0ea131fe3206ae95705f51186743315a6289d\"\n"
        + "        },\n" + "        \"totalTxNum\": 5,\n" + "        \"version\": \"v1.0\"\n" + "      },\n"
        + "      \"genesis\": true,\n" + "      \"transactionList\": [\n" + "        {\n"
        + "          \"actionDatas\": \"[{\\\"functionName\\\":null,\\\"index\\\":0,\\\"permissionName\\\":\\\"DEFAULT\\\",\\\"type\\\":\\\"PERMISSION_REGISTER\\\",\\\"version\\\":\\\"1.0.0\\\"},{\\\"functionName\\\":null,\\\"index\\\":1,\\\"permissionName\\\":\\\"RS\\\",\\\"type\\\":\\\"PERMISSION_REGISTER\\\",\\\"version\\\":\\\"1.0.0\\\"}]\",\n"
        + "          \"bizModel\": \"{\\\"Copyright\\\":\\\"STACS © 2019. All Rights Reserved\\\",\\\"GeniusData\\\":\\\"535441435320c2a920323031392e20416c6c20526967687473205265736572766564\\\"}\",\n"
        + "          \"blockHeight\": 1,\n" + "          \"blockTime\": 1561939200000,\n"
        + "          \"dealCount\": 1,\n" + "          \"executeResult\": \"1\",\n"
        + "          \"policyId\": \"IS_NULL\",\n" + "          \"policyVersion\": 0,\n"
        + "          \"receiptData\": \"{\\\"errorCode\\\":null,\\\"errorMessage\\\":null,\\\"receiptData\\\":null,\\\"result\\\":true,\\\"txId\\\":\\\"4ccc3753b6ee34a2a48a6d85526acdff60ef94e58fef12a55c47eaa9d932e9a5\\\",\\\"version\\\":null}\",\n"
        + "          \"sendTime\": 1561939200000,\n" + "          \"sender\": \"StacsGeniusBlock\",\n"
        + "          \"signDatas\": \"null\",\n"
        + "          \"txId\": \"4ccc3753b6ee34a2a48a6d85526acdff60ef94e58fef12a55c47eaa9d932e9a5\",\n"
        + "          \"txType\": \"DEFAULT\",\n" + "          \"version\": \"2.0.0\"\n" + "        },\n" + "        {\n"
        + "          \"actionDatas\": \"[{\\\"callbackType\\\":\\\"ALL\\\",\\\"decisionType\\\":\\\"FULL_VOTE\\\",\\\"domainIds\\\":[\\\"Domain\\\"],\\\"expression\\\":null,\\\"functionName\\\":null,\\\"index\\\":0,\\\"mustDomainIds\\\":null,\\\"policyId\\\":\\\"AUTHORIZE_PERMISSION\\\",\\\"policyName\\\":\\\"AUTHORIZE_PERMISSION\\\",\\\"requireAuthIds\\\":null,\\\"type\\\":\\\"REGISTER_POLICY\\\",\\\"verifyNum\\\":0,\\\"version\\\":\\\"1.0.0\\\",\\\"votePattern\\\":\\\"ASYNC\\\"},{\\\"callbackType\\\":\\\"ALL\\\",\\\"decisionType\\\":\\\"FULL_VOTE\\\",\\\"domainIds\\\":[\\\"Domain\\\"],\\\"expression\\\":null,\\\"functionName\\\":null,\\\"index\\\":1,\\\"mustDomainIds\\\":null,\\\"policyId\\\":\\\"BD_PUBLISH\\\",\\\"policyName\\\":\\\"BD_PUBLISH\\\",\\\"requireAuthIds\\\":null,\\\"type\\\":\\\"REGISTER_POLICY\\\",\\\"verifyNum\\\":0,\\\"version\\\":\\\"1.0.0\\\",\\\"votePattern\\\":\\\"ASYNC\\\"},{\\\"callbackType\\\":\\\"ALL\\\",\\\"decisionType\\\":\\\"FULL_VOTE\\\",\\\"domainIds\\\":[\\\"Domain\\\"],\\\"expression\\\":null,\\\"functionName\\\":null,\\\"index\\\":2,\\\"mustDomainIds\\\":null,\\\"policyId\\\":\\\"BUILD_SNAPSHOT\\\",\\\"policyName\\\":\\\"BUILD_SNAPSHOT\\\",\\\"requireAuthIds\\\":null,\\\"type\\\":\\\"REGISTER_POLICY\\\",\\\"verifyNum\\\":0,\\\"version\\\":\\\"1.0.0\\\",\\\"votePattern\\\":\\\"ASYNC\\\"},{\\\"callbackType\\\":\\\"ALL\\\",\\\"decisionType\\\":\\\"FULL_VOTE\\\",\\\"domainIds\\\":[\\\"Domain\\\"],\\\"expression\\\":null,\\\"functionName\\\":null,\\\"index\\\":3,\\\"mustDomainIds\\\":null,\\\"policyId\\\":\\\"CANCEL_PERMISSION\\\",\\\"policyName\\\":\\\"CANCEL_PERMISSION\\\",\\\"requireAuthIds\\\":null,\\\"type\\\":\\\"REGISTER_POLICY\\\",\\\"verifyNum\\\":0,\\\"version\\\":\\\"1.0.0\\\",\\\"votePattern\\\":\\\"ASYNC\\\"},{\\\"callbackType\\\":\\\"ALL\\\",\\\"decisionType\\\":\\\"FULL_VOTE\\\",\\\"domainIds\\\":[\\\"Domain\\\"],\\\"expression\\\":null,\\\"functionName\\\":null,\\\"index\\\":4,\\\"mustDomainIds\\\":null,\\\"policyId\\\":\\\"IDENTITY_BD_MANAGE\\\",\\\"policyName\\\":\\\"IDENTITY_BD_MANAGE\\\",\\\"requireAuthIds\\\":null,\\\"type\\\":\\\"REGISTER_POLICY\\\",\\\"verifyNum\\\":0,\\\"version\\\":\\\"1.0.0\\\",\\\"votePattern\\\":\\\"ASYNC\\\"},{\\\"callbackType\\\":\\\"ALL\\\",\\\"decisionType\\\":\\\"FULL_VOTE\\\",\\\"domainIds\\\":[\\\"Domain\\\"],\\\"expression\\\":null,\\\"functionName\\\":null,\\\"index\\\":5,\\\"mustDomainIds\\\":null,\\\"policyId\\\":\\\"IDENTITY_SETTING\\\",\\\"policyName\\\":\\\"IDENTITY_SETTING\\\",\\\"requireAuthIds\\\":null,\\\"type\\\":\\\"REGISTER_POLICY\\\",\\\"verifyNum\\\":0,\\\"version\\\":\\\"1.0.0\\\",\\\"votePattern\\\":\\\"ASYNC\\\"},{\\\"callbackType\\\":\\\"ALL\\\",\\\"decisionType\\\":\\\"FULL_VOTE\\\",\\\"domainIds\\\":[\\\"Domain\\\"],\\\"expression\\\":null,\\\"functionName\\\":null,\\\"index\\\":6,\\\"mustDomainIds\\\":null,\\\"policyId\\\":\\\"KYC_SETTING\\\",\\\"policyName\\\":\\\"KYC_SETTING\\\",\\\"requireAuthIds\\\":null,\\\"type\\\":\\\"REGISTER_POLICY\\\",\\\"verifyNum\\\":0,\\\"version\\\":\\\"1.0.0\\\",\\\"votePattern\\\":\\\"ASYNC\\\"},{\\\"callbackType\\\":\\\"ALL\\\",\\\"decisionType\\\":\\\"FULL_VOTE\\\",\\\"domainIds\\\":[\\\"Domain\\\"],\\\"expression\\\":null,\\\"functionName\\\":null,\\\"index\\\":7,\\\"mustDomainIds\\\":null,\\\"policyId\\\":\\\"PERMISSION_REGISTER\\\",\\\"policyName\\\":\\\"PERMISSION_REGISTER\\\",\\\"requireAuthIds\\\":null,\\\"type\\\":\\\"REGISTER_POLICY\\\",\\\"verifyNum\\\":0,\\\"version\\\":\\\"1.0.0\\\",\\\"votePattern\\\":\\\"ASYNC\\\"},{\\\"callbackType\\\":\\\"ALL\\\",\\\"decisionType\\\":\\\"FULL_VOTE\\\",\\\"domainIds\\\":[\\\"Domain\\\"],\\\"expression\\\":null,\\\"functionName\\\":null,\\\"index\\\":8,\\\"mustDomainIds\\\":null,\\\"policyId\\\":\\\"SAVE_ATTESTATION\\\",\\\"policyName\\\":\\\"SAVE_ATTESTATION\\\",\\\"requireAuthIds\\\":null,\\\"type\\\":\\\"REGISTER_POLICY\\\",\\\"verifyNum\\\":0,\\\"version\\\":\\\"1.0.0\\\",\\\"votePattern\\\":\\\"ASYNC\\\"},{\\\"callbackType\\\":\\\"ALL\\\",\\\"decisionType\\\":\\\"FULL_VOTE\\\",\\\"domainIds\\\":[\\\"Domain\\\"],\\\"expression\\\":null,\\\"functionName\\\":null,\\\"index\\\":9,\\\"mustDomainIds\\\":null,\\\"policyId\\\":\\\"SET_FEE_RULE\\\",\\\"policyName\\\":\\\"SET_FEE_RULE\\\",\\\"requireAuthIds\\\":null,\\\"type\\\":\\\"REGISTER_POLICY\\\",\\\"verifyNum\\\":0,\\\"version\\\":\\\"1.0.0\\\",\\\"votePattern\\\":\\\"ASYNC\\\"},{\\\"callbackType\\\":\\\"ALL\\\",\\\"decisionType\\\":\\\"FULL_VOTE\\\",\\\"domainIds\\\":[\\\"Domain\\\"],\\\"expression\\\":null,\\\"functionName\\\":null,\\\"index\\\":10,\\\"mustDomainIds\\\":null,\\\"policyId\\\":\\\"SYSTEM_PROPERTY\\\",\\\"policyName\\\":\\\"SYSTEM_PROPERTY\\\",\\\"requireAuthIds\\\":null,\\\"type\\\":\\\"REGISTER_POLICY\\\",\\\"verifyNum\\\":0,\\\"version\\\":\\\"1.0.0\\\",\\\"votePattern\\\":\\\"ASYNC\\\"}]\",\n"
        + "          \"bizModel\": \"{\\\"Copyright\\\":\\\"STACS © 2019. All Rights Reserved\\\",\\\"GeniusData\\\":\\\"535441435320c2a920323031392e20416c6c20526967687473205265736572766564\\\"}\",\n"
        + "          \"blockHeight\": 1,\n" + "          \"blockTime\": 1561939200000,\n"
        + "          \"dealCount\": 1,\n" + "          \"executeResult\": \"1\",\n"
        + "          \"policyId\": \"IS_NULL\",\n" + "          \"policyVersion\": 0,\n"
        + "          \"receiptData\": \"{\\\"errorCode\\\":null,\\\"errorMessage\\\":null,\\\"receiptData\\\":null,\\\"result\\\":true,\\\"txId\\\":\\\"6e844a64fd8f656bc6749f3213fa4939ad8072a51c08d3aae9718efccd7d104a\\\",\\\"version\\\":null}\",\n"
        + "          \"sendTime\": 1561939200000,\n" + "          \"sender\": \"StacsGeniusBlock\",\n"
        + "          \"signDatas\": \"null\",\n"
        + "          \"txId\": \"6e844a64fd8f656bc6749f3213fa4939ad8072a51c08d3aae9718efccd7d104a\",\n"
        + "          \"txType\": \"DEFAULT\",\n" + "          \"version\": \"2.0.0\"\n" + "        },\n" + "        {\n"
        + "          \"actionDatas\": \"[{\\\"bdType\\\":\\\"system\\\",\\\"bdVersion\\\":\\\"1.0\\\",\\\"code\\\":\\\"SystemBD\\\",\\\"desc\\\":null,\\\"functionName\\\":null,\\\"functions\\\":[{\\\"desc\\\":\\\"Identity设置\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"IDENTITY_SETTING\\\",\\\"methodSign\\\":\\\"IDENTITY_SETTING\\\",\\\"name\\\":\\\"IDENTITY_SETTING\\\",\\\"signValue\\\":\\\"IDENTITY_SETTINGSystemActionIdentity设置IDENTITY_SETTINGDEFAULTIDENTITY_SETTING\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"BD发布\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"BD_PUBLISH\\\",\\\"methodSign\\\":\\\"BD_PUBLISH\\\",\\\"name\\\":\\\"BD_PUBLISH\\\",\\\"signValue\\\":\\\"BD_PUBLISHSystemActionBD发布BD_PUBLISHDEFAULTBD_PUBLISH\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"Permission注册\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"PERMISSION_REGISTER\\\",\\\"methodSign\\\":\\\"PERMISSION_REGISTER\\\",\\\"name\\\":\\\"PERMISSION_REGISTER\\\",\\\"signValue\\\":\\\"PERMISSION_REGISTERSystemActionPermission注册PERMISSION_REGISTERDEFAULTPERMISSION_REGISTER\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"Permission授权\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"AUTHORIZE_PERMISSION\\\",\\\"methodSign\\\":\\\"AUTHORIZE_PERMISSION\\\",\\\"name\\\":\\\"AUTHORIZE_PERMISSION\\\",\\\"signValue\\\":\\\"AUTHORIZE_PERMISSIONSystemActionPermission授权AUTHORIZE_PERMISSIONDEFAULTAUTHORIZE_PERMISSION\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"Permission撤销授权\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"CANCEL_PERMISSION\\\",\\\"methodSign\\\":\\\"CANCEL_PERMISSION\\\",\\\"name\\\":\\\"CANCEL_PERMISSION\\\",\\\"signValue\\\":\\\"CANCEL_PERMISSIONSystemActionPermission撤销授权CANCEL_PERMISSIONDEFAULTCANCEL_PERMISSION\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"Policy注册\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"REGISTER_POLICY\\\",\\\"methodSign\\\":\\\"REGISTER_POLICY\\\",\\\"name\\\":\\\"REGISTER_POLICY\\\",\\\"signValue\\\":\\\"REGISTER_POLICYSystemActionPolicy注册REGISTER_POLICYDEFAULTREGISTER_POLICY\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"Policy修改\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"MODIFY_POLICY\\\",\\\"methodSign\\\":\\\"MODIFY_POLICY\\\",\\\"name\\\":\\\"MODIFY_POLICY\\\",\\\"signValue\\\":\\\"MODIFY_POLICYSystemActionPolicy修改MODIFY_POLICYDEFAULTMODIFY_POLICY\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"RS注册\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"REGISTER_RS\\\",\\\"methodSign\\\":\\\"REGISTER_RS\\\",\\\"name\\\":\\\"REGISTER_RS\\\",\\\"signValue\\\":\\\"REGISTER_RSSystemActionRS注册REGISTER_RSDEFAULTREGISTER_RS\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"RS撤销\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"CANCEL_RS\\\",\\\"methodSign\\\":\\\"CANCEL_RS\\\",\\\"name\\\":\\\"CANCEL_RS\\\",\\\"signValue\\\":\\\"CANCEL_RSSystemActionRS撤销CANCEL_RSDEFAULTCANCEL_RS\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"CA认证\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"CA_AUTH\\\",\\\"methodSign\\\":\\\"CA_AUTH\\\",\\\"name\\\":\\\"CA_AUTH\\\",\\\"signValue\\\":\\\"CA_AUTHSystemActionCA认证CA_AUTHDEFAULTCA_AUTH\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"CA撤销\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"CA_CANCEL\\\",\\\"methodSign\\\":\\\"CA_CANCEL\\\",\\\"name\\\":\\\"CA_CANCEL\\\",\\\"signValue\\\":\\\"CA_CANCELSystemActionCA撤销CA_CANCELDEFAULTCA_CANCEL\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"CA更新\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"CA_UPDATE\\\",\\\"methodSign\\\":\\\"CA_UPDATE\\\",\\\"name\\\":\\\"CA_UPDATE\\\",\\\"signValue\\\":\\\"CA_UPDATESystemActionCA更新CA_UPDATEDEFAULTCA_UPDATE\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"节点加入\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"NODE_JOIN\\\",\\\"methodSign\\\":\\\"NODE_JOIN\\\",\\\"name\\\":\\\"NODE_JOIN\\\",\\\"signValue\\\":\\\"NODE_JOINSystemAction节点加入NODE_JOINDEFAULTNODE_JOIN\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"节点退出\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"NODE_LEAVE\\\",\\\"methodSign\\\":\\\"NODE_LEAVE\\\",\\\"name\\\":\\\"NODE_LEAVE\\\",\\\"signValue\\\":\\\"NODE_LEAVESystemAction节点退出NODE_LEAVEDEFAULTNODE_LEAVE\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"系统属性配置\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"SYSTEM_PROPERTY\\\",\\\"methodSign\\\":\\\"SYSTEM_PROPERTY\\\",\\\"name\\\":\\\"SYSTEM_PROPERTY\\\",\\\"signValue\\\":\\\"SYSTEM_PROPERTYSystemAction系统属性配置SYSTEM_PROPERTYDEFAULTSYSTEM_PROPERTY\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"Identity BD 管理（froze/unfroze）\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"IDENTITY_BD_MANAGE\\\",\\\"methodSign\\\":\\\"IDENTITY_BD_MANAGE\\\",\\\"name\\\":\\\"IDENTITY_BD_MANAGE\\\",\\\"signValue\\\":\\\"IDENTITY_BD_MANAGESystemActionIdentity BD 管理（froze/unfroze）IDENTITY_BD_MANAGEDEFAULTIDENTITY_BD_MANAGE\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"identity kyc 设置\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"KYC_SETTING\\\",\\\"methodSign\\\":\\\"KYC_SETTING\\\",\\\"name\\\":\\\"KYC_SETTING\\\",\\\"signValue\\\":\\\"KYC_SETTINGSystemActionidentity kyc 设置KYC_SETTINGDEFAULTKYC_SETTING\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"手续费设置：合约地址 \\u0026 收取地址\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"SET_FEE_CONFIG\\\",\\\"methodSign\\\":\\\"SET_FEE_CONFIG\\\",\\\"name\\\":\\\"SET_FEE_CONFIG\\\",\\\"signValue\\\":\\\"SET_FEE_CONFIGSystemAction手续费设置：合约地址 \\u0026 收取地址SET_FEE_CONFIGDEFAULTSET_FEE_CONFIG\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"手续费费率配置\\\",\\\"execPermission\\\":\\\"RS\\\",\\\"execPolicy\\\":\\\"SET_FEE_RULE\\\",\\\"methodSign\\\":\\\"SET_FEE_RULE\\\",\\\"name\\\":\\\"SET_FEE_RULE\\\",\\\"signValue\\\":\\\"SET_FEE_RULESystemAction手续费费率配置SET_FEE_RULERSSET_FEE_RULE\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"保存存证\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"SAVE_ATTESTATION\\\",\\\"methodSign\\\":\\\"SAVE_ATTESTATION\\\",\\\"name\\\":\\\"SAVE_ATTESTATION\\\",\\\"signValue\\\":\\\"SAVE_ATTESTATIONSystemAction保存存证SAVE_ATTESTATIONDEFAULTSAVE_ATTESTATION\\\",\\\"type\\\":\\\"SystemAction\\\"},{\\\"desc\\\":\\\"打快照\\\",\\\"execPermission\\\":\\\"DEFAULT\\\",\\\"execPolicy\\\":\\\"BUILD_SNAPSHOT\\\",\\\"methodSign\\\":\\\"BUILD_SNAPSHOT\\\",\\\"name\\\":\\\"BUILD_SNAPSHOT\\\",\\\"signValue\\\":\\\"BUILD_SNAPSHOTSystemAction打快照BUILD_SNAPSHOTDEFAULTBUILD_SNAPSHOT\\\",\\\"type\\\":\\\"SystemAction\\\"}],\\\"functionsMap\\\":null,\\\"index\\\":0,\\\"initPermission\\\":null,\\\"initPolicy\\\":null,\\\"name\\\":\\\"SystemBD\\\",\\\"type\\\":\\\"BD_PUBLISH\\\",\\\"version\\\":\\\"1.0.0\\\"}]\",\n"
        + "          \"bizModel\": \"{\\\"Copyright\\\":\\\"STACS © 2019. All Rights Reserved\\\",\\\"GeniusData\\\":\\\"535441435320c2a920323031392e20416c6c20526967687473205265736572766564\\\"}\",\n"
        + "          \"blockHeight\": 1,\n" + "          \"blockTime\": 1561939200000,\n"
        + "          \"dealCount\": 1,\n" + "          \"executeResult\": \"1\",\n"
        + "          \"policyId\": \"IS_NULL\",\n" + "          \"policyVersion\": 0,\n"
        + "          \"receiptData\": \"{\\\"errorCode\\\":null,\\\"errorMessage\\\":null,\\\"receiptData\\\":null,\\\"result\\\":true,\\\"txId\\\":\\\"4707c5e268bca6bcd66b36b85f34cb7e97ef1c1d60a5730f8174a8fef3935486\\\",\\\"version\\\":null}\",\n"
        + "          \"sendTime\": 1561939200000,\n" + "          \"sender\": \"StacsGeniusBlock\",\n"
        + "          \"signDatas\": \"null\",\n"
        + "          \"txId\": \"4707c5e268bca6bcd66b36b85f34cb7e97ef1c1d60a5730f8174a8fef3935486\",\n"
        + "          \"txType\": \"DEFAULT\",\n" + "          \"version\": \"2.0.0\"\n" + "        },\n" + "        {\n"
        + "          \"actionDatas\": \"[{\\\"data\\\":null,\\\"domainId\\\":\\\"Domain\\\",\\\"functionName\\\":null,\\\"index\\\":null,\\\"period\\\":null,\\\"pubKey\\\":\\\"04ba98bf34af47145cf552b710570538b37bf3eff124e51c3361d02ea128c0447737be86077667feaca6dbc0679ae0653c4887d328a2b9d6d7f777599c287bf054\\\",\\\"type\\\":\\\"CA_INIT\\\",\\\"usage\\\":\\\"biz\\\",\\\"user\\\":\\\"STACS-TEST0\\\",\\\"valid\\\":false,\\\"version\\\":\\\"1.0.0\\\"},{\\\"data\\\":null,\\\"domainId\\\":\\\"Domain\\\",\\\"functionName\\\":null,\\\"index\\\":null,\\\"period\\\":null,\\\"pubKey\\\":\\\"04eb1a1d24b2456b600b5ba594f9783a6d51bec678ef57cdb5c7127d107a956c0240e89d41bfe164e8ef7a21f43f4c16e6a46874d9835929422b3264602186c79c\\\",\\\"type\\\":\\\"CA_INIT\\\",\\\"usage\\\":\\\"consensus\\\",\\\"user\\\":\\\"STACS-TEST0\\\",\\\"valid\\\":false,\\\"version\\\":\\\"1.0.0\\\"}]\",\n"
        + "          \"bizModel\": \"{\\\"Copyright\\\":\\\"STACS © 2019. All Rights Reserved\\\",\\\"GeniusData\\\":\\\"535441435320c2a920323031392e20416c6c20526967687473205265736572766564\\\"}\",\n"
        + "          \"blockHeight\": 1,\n" + "          \"blockTime\": 1561939200000,\n"
        + "          \"dealCount\": 1,\n" + "          \"executeResult\": \"1\",\n"
        + "          \"policyId\": \"IS_NULL\",\n" + "          \"policyVersion\": 0,\n"
        + "          \"receiptData\": \"{\\\"errorCode\\\":null,\\\"errorMessage\\\":null,\\\"receiptData\\\":null,\\\"result\\\":true,\\\"txId\\\":\\\"2716364f7fb9484d8af035248443fef09a4edfec54605de64441a12aa9654c25\\\",\\\"version\\\":null}\",\n"
        + "          \"sendTime\": 1561939200000,\n" + "          \"sender\": \"StacsGeniusBlock\",\n"
        + "          \"signDatas\": \"null\",\n"
        + "          \"txId\": \"2716364f7fb9484d8af035248443fef09a4edfec54605de64441a12aa9654c25\",\n"
        + "          \"txType\": \"DEFAULT\",\n" + "          \"version\": \"2.0.0\"\n" + "        },\n" + "        {\n"
        + "          \"actionDatas\": \"[{\\\"desc\\\":\\\"by stacs init\\\",\\\"domainDesc\\\":null,\\\"domainId\\\":\\\"Domain\\\",\\\"functionName\\\":null,\\\"index\\\":0,\\\"maxNodeSize\\\":1,\\\"rsId\\\":\\\"STACS-TEST0\\\",\\\"type\\\":\\\"REGISTER_RS\\\",\\\"version\\\":\\\"1.0.0\\\"}]\",\n"
        + "          \"bizModel\": \"{\\\"Copyright\\\":\\\"STACS © 2019. All Rights Reserved\\\",\\\"GeniusData\\\":\\\"535441435320c2a920323031392e20416c6c20526967687473205265736572766564\\\"}\",\n"
        + "          \"blockHeight\": 1,\n" + "          \"blockTime\": 1561939200000,\n"
        + "          \"dealCount\": 1,\n" + "          \"executeResult\": \"1\",\n"
        + "          \"policyId\": \"IS_NULL\",\n" + "          \"policyVersion\": 0,\n"
        + "          \"receiptData\": \"{\\\"errorCode\\\":null,\\\"errorMessage\\\":null,\\\"receiptData\\\":null,\\\"result\\\":true,\\\"txId\\\":\\\"57ecda40ae129ce91189d1a2855c67917b0629fbac82b9bef1d7b0d41a51caed\\\",\\\"version\\\":null}\",\n"
        + "          \"sendTime\": 1561939200000,\n" + "          \"sender\": \"StacsGeniusBlock\",\n"
        + "          \"signDatas\": \"null\",\n"
        + "          \"txId\": \"57ecda40ae129ce91189d1a2855c67917b0629fbac82b9bef1d7b0d41a51caed\",\n"
        + "          \"txType\": \"DEFAULT\",\n" + "          \"version\": \"2.0.0\"\n" + "        }\n" + "      ]\n"
        + "    }";

    String message2 = "{\n" + "      \"blockHeader\": {\n"
        + "        \"blockHash\": \"4a9f0461e62c525ab3929a8ac29b48a8cd6a7bb528837b34dd28b4d44dc01f5a\",\n"
        + "        \"blockTime\": 1575960516425,\n" + "        \"height\": 2,\n"
        + "        \"previousHash\": \"233bae1379706d4e3996db72b69d5124fd90d40a212cc374a99615ae5774853e\",\n"
        + "        \"stateRootHash\": {\n" + "          \"accountRootHash\": \"NO_TREE\",\n"
        + "          \"caRootHash\": \"NO_TREE\",\n" + "          \"contractRootHash\": \"NO_TREE\",\n"
        + "          \"policyRootHash\": \"NO_TREE\",\n" + "          \"rsRootHash\": \"NO_TREE\",\n"
        + "          \"stateRoot\": \"56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421\",\n"
        + "          \"txReceiptRootHash\": \"537e18db91b274eb43d84515323ef2ca60412affb56ef75e5226e17eb2e2b616\",\n"
        + "          \"txRootHash\": \"134b96a660118f971e2832bbdfe87d09c981e38632b2666aa8ca96778088cc60\"\n"
        + "        },\n" + "        \"totalTxNum\": 6,\n" + "        \"version\": \"v1.0\"\n" + "      },\n"
        + "      \"genesis\": false,\n" + "      \"transactionList\": [\n" + "        {\n"
        + "          \"actionDatas\": \"[{\\\"attestation\\\":\\\"test attestation\\\",\\\"attestationVersion\\\":\\\"test\\\",\\\"functionName\\\":\\\"SAVE_ATTESTATION\\\",\\\"index\\\":0,\\\"objective\\\":\\\"test_address\\\",\\\"remark\\\":\\\"remarktest\\\",\\\"type\\\":\\\"SAVE_ATTESTATION\\\",\\\"version\\\":\\\"1.0.0\\\"}]\",\n"
        + "          \"bdCode\": \"SystemBD\",\n" + "          \"bizModel\": \"{}\",\n"
        + "          \"blockHeight\": 2,\n" + "          \"blockTime\": 1575960516425,\n"
        + "          \"dealCount\": 1,\n" + "          \"errorCode\": \"150101\",\n"
        + "          \"errorMessage\": \"exception decoding Hex string: String index out of range: 3\",\n"
        + "          \"executeResult\": \"0\",\n" + "          \"policyId\": \"SAVE_ATTESTATION\",\n"
        + "          \"policyVersion\": 0,\n"
        + "          \"receiptData\": \"{\\\"errorCode\\\":\\\"150101\\\",\\\"errorMessage\\\":\\\"exception decoding Hex string: String index out of range: 3\\\",\\\"receiptData\\\":{\\\"actionResults\\\":null,\\\"feeInfo\\\":null},\\\"result\\\":false,\\\"txId\\\":\\\"test_attestation_id\\\",\\\"version\\\":\\\"3.0.0\\\"}\",\n"
        + "          \"sendTime\": 1575960515691,\n" + "          \"sender\": \"STACS-TEST0\",\n"
        + "          \"signDatas\": \"[{\\\"domainId\\\":\\\"Domain\\\",\\\"owner\\\":\\\"STACS-TEST0\\\",\\\"sign\\\":\\\"0132fbdd04485471dbc5a6d7d935b71f8f450099c374c51e71a30a5eec5f66fac66284a2f26d1d9b2327379fc334adf671a9887514013750de9c8cdcfbf1a4f024\\\",\\\"signType\\\":\\\"BIZ\\\"}]\",\n"
        + "          \"submitter\": \"test_address\",\n" + "          \"submitterSign\": \"123\",\n"
        + "          \"txId\": \"test_attestation_id\",\n" + "          \"txType\": \"DEFAULT\",\n"
        + "          \"version\": \"3.0.0\"\n" + "        }\n" + "      ]\n" + "    }";

    @Test public void test() {
        BlockVO block = JSON.parseObject(message2, BlockVO.class);
        List<TransactionPO> txList = block.getTransactionList();
        log.info(block.toString());
    }
}
