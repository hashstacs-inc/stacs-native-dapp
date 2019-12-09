package io.stacs.nav.drs.service.model;

import io.stacs.nav.drs.service.enums.CallbackStatus;
import lombok.Data;

/**
 * @author liuyu
 * @description
 * @date 2019-11-08
 */
@Data public class BlockCallbackBO {
    /**
     * block height
     */
    private Long blockHeight;
    /**
     * tx receipt json datas
     */
    private String blockData;
    /***
     * status,INIT、PROCESSED
     */
    private CallbackStatus status;
}
