package io.stacs.nav.drs.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author tangkun
 * @date 2019-06-14
 */
@AllArgsConstructor public enum ContractStatusEnum {

    //@formatter:@off
    ENABLED("ENABLED", "ENABLED"), INIT("INIT", "INIT"), FAILED("FAILED", "FAILED"), DISABLED("DISABLED",
                                                                                              "DISABLED"), DESTROY(
        "DESTROY", "DESTROY"),
    ;
    //@formatter:@on

    @Getter private String code;
    @Getter private String desc;

    public static ContractStatusEnum valueOfBD(String bdCode) {
        // todo 待后续功能完善
        return INIT;
    }

}
