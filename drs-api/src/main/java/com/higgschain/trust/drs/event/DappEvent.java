package com.higgschain.trust.drs.event;

import com.alipay.sofa.ark.spi.event.ArkEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author suimi
 * @date 2019/10/30
 */
@Getter @Setter @NoArgsConstructor @ToString public class DappEvent implements ArkEvent {
    private long height;
    private String txId;
    private String value;

    @Override public String getTopic() {
        return value;
    }
}
