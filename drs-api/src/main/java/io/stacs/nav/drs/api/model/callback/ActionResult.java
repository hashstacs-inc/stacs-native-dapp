package io.stacs.nav.drs.api.model.callback;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liuyu
 * @description
 * @date 2019-09-04
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ActionResult{
    /**
     * the action index
     */
    private int index;
    /**
     * the result-data of action executed
     */
    private JSONObject data;
}
