package io.stacs.nav.drs.service.vo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author dekuofa <br>
 * @date 2019-11-07 <br>
 */
@Getter @Setter public class PermissionCheckVO {

    @NotBlank private String address;

    @NotNull private List<String> permissionNames;

}
