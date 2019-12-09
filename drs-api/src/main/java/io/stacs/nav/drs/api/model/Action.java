package io.stacs.nav.drs.api.model;

import io.stacs.nav.drs.api.enums.ActionTypeEnum;
import io.stacs.nav.drs.api.enums.VersionEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * The type Action.
 *
 * @Description: abstract action class
 * @author: pengdi
 */
@Getter @Setter public abstract class Action {
    private static final long serialVersionUID = -9206591383343379207L;

    /**
     * action type
     */
    @NotNull private ActionTypeEnum type;

    /**
     * action index
     */
    @NotNull private Integer index;

    /**
     * the function name
     */
    @NotNull private String functionName;

    /**
     * version
     */
    private String version = VersionEnum.V1.getCode();

}
