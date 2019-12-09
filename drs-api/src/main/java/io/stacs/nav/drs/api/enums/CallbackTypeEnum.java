package io.stacs.nav.drs.api.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * The enum Callback type enum.
 *
 * @author liuyu
 * @description
 * @date 2018 -06-06
 */
public enum CallbackTypeEnum {
    /**
     * The All.
     */
    ALL("ALL", "callback all"),
    /**
     * The Self.
     */
    SELF("SELF", "callback self");

    private String code;
    private String desc;

    CallbackTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * From code callback type enum.
     *
     * @param code the code
     * @return the callback type enum
     */
    public static CallbackTypeEnum fromCode(String code) {
        for (CallbackTypeEnum typeEnum : values()) {
            if (StringUtils.equals(code, typeEnum.getCode())) {
                return typeEnum;
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
