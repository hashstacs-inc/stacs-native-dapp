package io.stacs.nav.drs.service.enums;

/**
 * @author liuyu
 * @description
 * @date 2019-11-07
 */
public enum RequestStatus {
    INIT,SUBMITTING,PROCESSING,END;

    /**
     * get by code
     *
     * @param code
     * @return
     */
    public static RequestStatus fromCode(String code){
        if(code == null){
            return null;
        }
        for(RequestStatus status : values()){
            if(status.name().equals(code)){
                return status;
            }
        }
        return null;
    }

}
