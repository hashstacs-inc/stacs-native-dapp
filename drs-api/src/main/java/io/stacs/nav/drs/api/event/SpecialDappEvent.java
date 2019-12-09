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
    // 发送给指定的DApp
    private String name;
    // 区块数据类型
    private SpecialDappEventTypeEnum eventType;
    // 事件携带的数据信息
    private T payload;

    @Override public String getTopic() {
        return "CALL_BACK";
    }
}
