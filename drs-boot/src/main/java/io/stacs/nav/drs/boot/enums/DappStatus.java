package io.stacs.nav.drs.boot.enums;

/**
 * @author suimi
 * @date 2019/10/31
 */
public enum DappStatus {
    /**
     * downloaded
     */
    DOWNLOADED,
    /**
     * initialized
     */
    INITIALIZED,
    /**
     * running
     */
    RUNNING,
    /**
     * stopped
     */
    STOPPED;

    /**
     *  from status code
     *
     * @param code
     * @return
     */
    public static DappStatus fromCode(String code){
        for(DappStatus dappStatus : values()){
            if(dappStatus.name().equals(code)){
                return dappStatus;
            }
        }
        return null;
    }

}
