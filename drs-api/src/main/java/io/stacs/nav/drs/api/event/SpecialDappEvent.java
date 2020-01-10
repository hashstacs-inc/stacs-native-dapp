package io.stacs.nav.drs.api.event;

import com.alipay.sofa.ark.spi.event.ArkEvent;
import io.stacs.nav.drs.api.enums.SpecialDappEventTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author suimi
 * @date 2019/10/30
 */
@Getter @Setter @NoArgsConstructor @ToString public class SpecialDappEvent<T> implements ArkEvent {
    private String name;
    private SpecialDappEventTypeEnum eventType;
    private T payload;

    @Override public String getTopic() {
        return "CALL_BACK";
    }
}
