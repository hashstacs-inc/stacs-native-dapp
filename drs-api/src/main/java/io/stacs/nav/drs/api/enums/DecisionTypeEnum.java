package io.stacs.nav.drs.api.enums;

/**
 * The enum Decision type enum.
 *
 * @author liuyu
 * @description
 * @date 2018 -06-06
 */
public enum DecisionTypeEnum {
    /**
     * The Full vote.
     */
    FULL_VOTE("FULL_VOTE", "require all vote"),
    /**
     * The One vote.
     */
    ONE_VOTE("ONE_VOTE", "just one vote"),
    /**
     * user assign number
     */
    ASSIGN_NUM("ASSIGN_NUM", "user assign number");
    private String code;
    private String desc;

    DecisionTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Gets bycode.
     *
     * @param code the code
     * @return the bycode
     */
    public static DecisionTypeEnum getBycode(String code) {
        for (DecisionTypeEnum decisionTypeEnum : DecisionTypeEnum.values()) {
            if (decisionTypeEnum.getCode().equals(code)) {
                return decisionTypeEnum;
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
