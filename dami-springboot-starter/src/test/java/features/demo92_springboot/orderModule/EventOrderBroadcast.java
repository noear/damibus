package features.demo92_springboot.orderModule;

import org.noear.dami.spring.boot.annotation.DamiTopic;
import org.springframework.stereotype.Component;

/**
 * 基于事件的，订单广播（要告知别人的）
 * */
@DamiTopic("event.order")
@Component
public interface EventOrderBroadcast {
    void onCreated(long orderId);
}
