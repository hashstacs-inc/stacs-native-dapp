package io.stacs.nav.drs.api.enums;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

/**
 * @author dekuofa <br>
 * @date 2019-11-28 <br>
 */
public interface ApiConstants {

    List<String> ENCRYPT_WHITE_LIST = Lists.newArrayList("callback/register");

    @Getter enum TransactionApiEnum implements ApiInterface {
        //@formatter:off
        IDENTITY_SETTING("IDENTITY_SETTING", "/identity/setting"),
        IDENTITY_BD_MANAGE("IDENTITY_BD_MANAGE", "/identity/bdManage"),
        BD_PUBLISH("BD_PUBLISH", "/bd/publish"),
        PERMISSION_REGISTER("PERMISSION_REGISTER", "/permission/register"),
        AUTHORIZE_PERMISSION("AUTHORIZE_PERMISSION", "/permission/authorize"),
        CANCEL_PERMISSION("CANCEL_PERMISSION", "/permission/cancel"),
        REGISTER_POLICY("REGISTER_POLICY", "/policy/register"),
        MODIFY_POLICY("MODIFY_POLICY", "/policy/modify"),
        SYSTEM_PROPERTY("SYSTEM_PROPERTY", "/systemProperty/config"),
        KYC_SETTING("KYC_SETTING", "/kyc/setting"),
        SET_FEE_RULE("SET_FEE_RULE", "/fee/setRule"),
        SAVE_ATTESTATION("SAVE_ATTESTATION", "/attestation/save"),
        BUILD_SNAPSHOT("BUILD_SNAPSHOT", "/snapshot/build"),
        CREATE_CONTRACT("CREATE_CONTRACT", "/contract/deploy"),
        CONTRACT_INVOKER("CONTRACT_INVOKER", "/contract/invoke"),
        ;
        //@formatter:on
        private String functionName;
        private String api;

        TransactionApiEnum(String funcName, String api) {
            this.functionName = funcName;
            this.api = api;
        }

        public static TransactionApiEnum fromTxName(String txName) {
            if (txName == null) {
                return null;
            }
            for (TransactionApiEnum tx : values()) {
                if (tx.getFunctionName().equals(txName)) {
                    return tx;
                }
            }
            return null;
        }

        @Override public String getName() {
            return functionName;
        }
    }

    @Getter enum QueryApiEnum implements ApiInterface {
        //@formatter:off
        BD_QUERY("bdQuery", "/bd/query"),
        CHECK_PERMISSION("checkPermission", "/identity/checkPermission"),
        QUERY_MAX_BLOCK_HEIGHT("getMaxBlockHeight", "/block/currentHeight/query"),
        QUERY_BLOCK_VO("queryBlockVO", "/block/queryBlockVO"),
        QUERY_BLOCKS_BY_PAGE("queryBlocksByPage", "/explorer/queryBlocksByPage"),
        QUERY_BLOCK_BY_HEIGHT("queryBlockByHeight", "/explorer/queryBlockByHeight"),
        QUERY_TX_LIST_BY_PAGE("queryTxsByPage", "/explorer/queryTxsByPage"),
        QUERY_TX_BY_ID("queryTxById", "/explorer/queryTxById"),
        QUERY_ALL_DOMAIN("queryDomainAll", "/domain/queryAll"),
        QUERY_POLICY_LIST("queryPolicyList", "/policy/queryAll"),
        QUERY_PERMISSION_LIST("queryPermissionList", "/permission/queryAll"),
        QUERY_CONTRACT("queryContract", "/contract/query"),
        ;
        //@formatter:on

        private String name;
        private String api;

        QueryApiEnum(String queryName, String api) {
            this.name = queryName;
            this.api = api;
        }

        public static QueryApiEnum fromName(String name) {
            if (name == null) {
                return null;
            }
            for (QueryApiEnum e : values()) {
                if (e.getName().equals(name)) {
                    return e;
                }
            }
            return null;
        }
    }

    interface ApiInterface {
        String getName();

        String getApi();
    }
}
