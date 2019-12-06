package io.stacs.nav.drs.service.model.action;

import io.stacs.nav.drs.service.enums.ActionTypeEnum;
import io.stacs.nav.drs.service.enums.VersionEnum;
import io.stacs.nav.drs.service.model.bo.BaseBO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * The type Action.
 *
 * @Description: abstract action class
 * @author: pengdi
 */
@Getter @Setter public abstract class Action implements BaseBO {
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
