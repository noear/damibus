package features.demo96_springboot.orderModule;

import org.noear.dami.spring.boot.annotation.DamiTopic;

/**
 * 基于事件的，订单广播（要告知别人的）
 * */
@DamiTopic("event.order2")
public interface EventOrderBroadcast {
    void onCreated(long orderId);
}
