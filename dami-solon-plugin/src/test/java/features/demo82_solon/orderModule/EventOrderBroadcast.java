package features.demo82_solon.orderModule;

import org.noear.dami.solon.annotation.DamiTopic;

/**
 * 基于事件的，订单广播（要告知别人的）
 * */
@DamiTopic("event.order")
public interface EventOrderBroadcast {
    void onCreated(long orderId);
}
