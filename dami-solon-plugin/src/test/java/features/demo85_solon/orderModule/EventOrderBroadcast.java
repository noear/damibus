package features.demo85_solon.orderModule;

import org.noear.dami2.solon.annotation.DamiTopic;

/**
 * 基于事件的，订单广播（要告知别人的）
 * */
@DamiTopic("event.order")
public interface EventOrderBroadcast {
    void onCreated(long orderId);
}
