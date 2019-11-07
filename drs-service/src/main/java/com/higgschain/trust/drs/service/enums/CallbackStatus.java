package com.higgschain.trust.drs.service.enums;

/**
 * @author liuyu
 * @description
 * @date 2019-11-07
 */
public enum CallbackStatus {
    INIT,PROCESSED;

    /**
     * get by code
     * 
     * @param code
     * @return
     */
    public static CallbackStatus fromCode(String code){
        if(code == null){
            return null;
        }
        for(CallbackStatus status : values()){
            if(status.name().equals(code)){
                return status;
            }
        }
        return null;
    }

}
