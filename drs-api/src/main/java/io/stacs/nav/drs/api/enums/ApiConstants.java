package io.stacs.nav.drs.api.enums;

import lombok.Getter;

/**
 * @author dekuofa <br>
 * @date 2019-11-28 <br>
 */
public interface ApiConstants {
    @Getter enum TransactionApiEnum {
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
        CONTRACT_INVOKER("CONTRACT_INVOKER", "/contract/invoke");

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
    }

    @Getter enum QueryApiEnum {
        BD_QUERY("bdQuery", "/bd/query"),
        CHECK_PERMISSION("checkPermission", "/identity/checkPermission"),
        QUERY_MAX_BLOCK_HEIGHT("getMaxBlockHeight", "/block/currentHeight/query"),
        ;

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
}