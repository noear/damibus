package features.demo95_springboot.orderModule;

import org.noear.dami2.spring.boot.annotation.DamiTopic;

/**
 * 基于事件的，订单广播（要告知别人的）
 * */
@DamiTopic("event.order")
public interface EventOrderBroadcast {
    void onCreated(long orderId);
}
