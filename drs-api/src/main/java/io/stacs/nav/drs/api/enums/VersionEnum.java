package io.stacs.nav.drs.api.enums;

/**
 * The enum Version enum.
 *
 * @author WangQuanzhou
 * @desc multi version enum class
 * @date 2018 /3/27 11:55
 */
public enum VersionEnum {
    /**
     * V 1 version enum.
     */
    V1("1.0.0", "V1版本"),
    /**
     * V 2 version enum.
     */
    V2("2.0.0", "V2版本"),
    /**
     * V 3 version enum.
     */
    V3("3.0.0", "V3版本"),
    /**
     * V 4 version enum.
     */
    V4("4.0.0", "V4版本"),
    /**
     * V 5 version enum.
     */
    V5("5.0.0", "V5版本"),
    ;

    /**
     * The Code.
     */
    String code;
    /**
     * The Desc.
     */
    String desc;

    VersionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Gets   enum by code.
     *
     * @param code the code
     * @return the version
     */
    public static VersionEnum getByCode(String code) {
        for (VersionEnum versionEnum : VersionEnum.values()) {
            if (versionEnum.getCode().equals(code)) {
                return versionEnum;
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
