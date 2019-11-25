package io.stacs.nav.drs.api.model.bd;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dekuofa <br>
 * @date 2019-11-04 <br>
 */
@Getter @Setter public class FunctionDefine {
    private String name;
    private String type;
    private String desc;
    private String methodSign;
    private String execPermission;
    private String execPolicy;

    public String getSignValue(){
        return name + type + desc + methodSign + execPermission + execPolicy;
    }
}
