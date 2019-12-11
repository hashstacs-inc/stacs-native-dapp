package io.stacs.nav.drs.service.enums;

import java.util.Optional;

/**
 * @author dekuofa <br>
 * @date 2019-12-11 <br>
 */
public enum ActionExecTypeEnum {
    //@formatter:off
    BD_ADD,
    POLICY_ADD,
    POLICY_MODIFY,
    CONTRACT_ADD,
    ;


    public static Optional<ActionExecTypeEnum> valueOfActionType(String actionType) {
        switch (actionType) {
            case "BD_PUBLISH": return Optional.of(BD_ADD);
            case "CONTRACT_CREATION": return Optional.of(CONTRACT_ADD);
            case "REGISTER_POLICY": return Optional.of(POLICY_ADD);
            case "MODIFY_POLICY": return Optional.of(POLICY_MODIFY);
            default: return Optional.empty();
        }
    }
    //@formatter:on
}
