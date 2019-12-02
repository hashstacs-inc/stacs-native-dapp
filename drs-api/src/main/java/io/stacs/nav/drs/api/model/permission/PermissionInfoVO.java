package io.stacs.nav.drs.api.model.permission;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangkun
 * @date 2019-10-15
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor public class PermissionInfoVO {
    private String permissionName;
    public static void main(String[] args){
        List<PermissionInfoVO> list = new ArrayList<>();
        for(int i=0;i<3;i++){
            PermissionInfoVO vo = new PermissionInfoVO();
            vo.setPermissionName("name-" + i);
            list.add(vo);
        }
        System.out.println(JSON.toJSONString(list));
    }
}
