package io.stacs.nav.drs.api.model.task;

import org.apache.commons.lang3.StringUtils;

/**
 * @author liuyu
 * @description
 * @date 2019-09-06
 */
public enum TaskTypeEnum {
    BONUS_TYPE("BONUS_TYPE", "task type of bonus"),
    ;
    private String type;
    private String desc;
    TaskTypeEnum(String type,String desc){
        this.type = type;
        this.desc = desc;
    }

    /**
     * from type
     * 
     * @param type
     * @return
     */
    public static TaskTypeEnum fromType(String type){
        for(TaskTypeEnum typeEnum : values()){
            if(StringUtils.equals(typeEnum.getType(),type)){
                return typeEnum;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
