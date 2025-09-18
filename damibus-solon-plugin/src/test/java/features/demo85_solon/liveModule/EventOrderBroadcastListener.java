package features.demo85_solon.liveModule;

import org.noear.dami2.solon.annotation.DamiTopic;

/**
 * 基于事件的，订单广播监听器
 * */
@DamiTopic("event.order")
public class EventOrderBroadcastListener {
    public void onCreated(long orderId) {
        System.err.println("Live:Order:onCreated: orderId=" + orderId);
    }
}
