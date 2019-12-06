package io.stacs.nav.drs.service.enums;

/**
 * The enum Tx type enum.
 *
 * @author liuyu
 * @desc the transaction type enum class
 * @date 2018 /09/04 17:26
 */
public enum TxTypeEnum {

    /**
     * The Default.
     */
    DEFAULT("DEFAULT", "default type"),
    /**
     * The Rs.
     */
    RS("RS", "rs register or cancel"),
    /**
     * The Policy.
     */
    POLICY("POLICY", "policy register"),
    /**
     * The Ca.
     */
    CA("CA", "ca auth、update or cancel"),
    /**
     * The Node.
     */
    NODE("NODE", "node join or leave"),
    /**
     * The Utxo.
     */
    UTXO("UTXO", "utxo issue or destroy"),
    /**
     * The Contract.
     */
    CONTRACT("CONTRACT", "contract issue or destroy"),

    /**
     * The Createcontract.
     */
    CREATECONTRACT("CREATECONTRACT", "create contract"),
    /**
     * Invokecontract tx type enum.
     */
    INVOKECONTRACT("INVOKECONTRACT", "InvokeContract");

    /**
     * The Code.
     */
    String code;
    /**
     * The Desc.
     */
    String desc;

    TxTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * get type by code
     *
     * @param code the code
     * @return bycode
     */
    public static TxTypeEnum getBycode(String code) {
        for (TxTypeEnum typeEnum : TxTypeEnum.values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }

    /**
     * is target type
     *
     * @param code   the code
     * @param target the target
     * @return boolean
     */
    public static boolean isTargetType(String code, TxTypeEnum target) {
        TxTypeEnum txTypeEnum = getBycode(code);
        if (txTypeEnum == null) {
            return false;
        }
        return txTypeEnum == target;
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
