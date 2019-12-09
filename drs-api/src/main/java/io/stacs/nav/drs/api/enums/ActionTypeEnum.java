package io.stacs.nav.drs.api.enums;

/**
 * The enum Action type enum.
 *
 * @author WangQuanzhou
 * @desc action type enum class
 * @date 2018 /3/26 17:26
 */
public enum ActionTypeEnum {

    /**
     * The Open account.
     */
    OPEN_ACCOUNT("OPEN_ACCOUNT", "open account action"),
    /**
     * The Accounting.
     */
    ACCOUNTING("ACCOUNTING", "accounting action"),
    /**
     * The Freeze.
     */
    FREEZE("FREEZE", "freeze capital action"),
    /**
     * The Unfreeze.
     */
    UNFREEZE("UNFREEZE", "unfreeze capital action"),
    /**
     * The Utxo.
     */
    UTXO("UTXO", "UTXO action"),
    /**
     * The Register contract.
     */
    REGISTER_CONTRACT("REGISTER_CONTRACT", "register contract action"),
    /**
     * The Bind contract.
     */
    BIND_CONTRACT("BIND_CONTRACT", "bind contract action"),
    /**
     * The Trigger contract.
     */
    TRIGGER_CONTRACT("TRIGGER_CONTRACT", "trigger contract action"),
    /**
     * The Contract state migration.
     */
    CONTRACT_STATE_MIGRATION("CONTRACT_STATE_MIGRATION", "migration contract state"),
    /**
     * The Register rs.
     */
    REGISTER_RS("REGISTER_RS", "register RS action"),
    /**
     * The Register policy.
     */
    REGISTER_POLICY("REGISTER_POLICY", "register policy action"),
    /**
     * The Create data identity.
     */
    CREATE_DATA_IDENTITY("CREATE_DATA_IDENTITY", "create data identity"),
    /**
     * The Issue currency.
     */
    ISSUE_CURRENCY("ISSUE_CURRENCY", "issue new currency"),
    /**
     * The Ca auth.
     */
    CA_AUTH("CA_AUTH", "ca auth"),
    /**
     * The Ca init.
     */
    CA_INIT("CA_INIT", "ca init"),
    /**
     * The Ca cancel.
     */
    CA_CANCEL("CA_CANCEL", "ca cancel"),
    /**
     * The Ca update.
     */
    CA_UPDATE("CA_UPDATE", "ca update"),
    /**
     * The Node join.
     */
    NODE_JOIN("NODE_JOIN", "node join"),
    /**
     * The Node leave.
     */
    NODE_LEAVE("NODE_LEAVE", "node leave"),
    /**
     * The Rs cancel.
     */
    RS_CANCEL("RS_CANCEL", "cancel rs"),
    /**
     * The Contract creation.
     */
    CONTRACT_CREATION("CONTRACT_CREATION", "creation contract"),
    /**
     * The Contract invoked.
     */
    CONTRACT_INVOKED("CONTRACT_INVOKED", "invoked contract"),
    /**
     * modify policy
     */
    MODIFY_POLICY("MODIFY_POLICY", "modify policy"),

    /**
     * config fee
     */
    FEE_CONFIG("FEE_CONFIG", "fee config"),

    /**
     * config fee rule
     */
    FEE_RULE("FEE_RULE", "fee rule"),

    /**
     * config domain
     */
    DOMAIN_CONFIG("DOMAIN_CONFIG", "fee rule"),

    TASK_CONFIG("TASK_CONFIG", "config task"),
    /**
     * execute task
     */
    TASK_EXECUTE("TASK_EXECUTE", "execute task"),

    /**
     * system property config
     */
    SYSTEM_PROPERTY("SYSTEM_PROPERTY", "SYSTEM PROPERTY"),

    /**
     * permission register
     */
    PERMISSION_REGISTER("PERMISSION_REGISTER", "PERMISSION REGISTER"),
    /**
     * identity setting
     */
    IDENTITY_SETTING("IDENTITY_SETTING", "IDENTITY SETTING"),
    /**
     * authorize permission
     */
    AUTHORIZE_PERMISSION("AUTHORIZE_PERMISSION", "AUTHORIZE PERMISSION"),
    /**
     * cancel permission
     */
    CANCEL_PERMISSION("CANCEL_PERMISSION", "CANCEL PERMISSION"),
    /**
     * kyc setting
     */
    KYC_SETTING("KYC_SETTING", "KYC SETTING"),

    BD_PUBLISH("BD_PUBLISH", "publish business define"),
    /**
     * bd manage
     */
    IDENTITY_BD_MANAGE("IDENTITY_BD_MANAGE", "IDENTITY_BD_MANAGE, froze/unfroze user_identity BD"),
    /**
     * save attestation
     */
    SAVE_ATTESTATION("SAVE_ATTESTATION", "save attestation "),
    ;

    /**
     * The Code.
     */
    String code;
    /**
     * The Desc.
     */
    String desc;

    ActionTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Gets action type enum bycode.
     *
     * @param code the code
     * @return the action type enum bycode
     */
    public static ActionTypeEnum getActionTypeEnumBycode(String code) {
        for (ActionTypeEnum actionTypeEnum : ActionTypeEnum.values()) {
            if (actionTypeEnum.getCode().equals(code)) {
                return actionTypeEnum;
            }
        }
        return null;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }
}
