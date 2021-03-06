package io.stacs.nav.drs.api.event;

import com.alipay.sofa.ark.spi.event.ArkEvent;
import io.stacs.nav.drs.api.model.callback.TransactionReceipt;
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
    private TransactionReceipt value;

    @Override public String getTopic() {
        return "CALL_BACK";
    }
}
