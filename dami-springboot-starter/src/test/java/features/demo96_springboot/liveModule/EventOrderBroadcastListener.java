package features.demo96_springboot.liveModule;

import org.noear.dami2.spring.boot.annotation.DamiTopic;

/**
 * 基于事件的，订单广播监听器
 * */
@DamiTopic("event.order2")
public class EventOrderBroadcastListener {
    public void onCreated(long orderId) {
        System.err.println("Live:Order:onCreated: orderId=" + orderId);
    }
}
