package demo92_springboot.liveModule.event;

import org.noear.dami.spring.boot.annotation.DamiTopic;

@DamiTopic("demo.order")
public class OrderEventListenerOfLive {
    public void onCreated(long orderId) {
        System.err.println("LIve:Order:onCreated: orderId=" + orderId);
    }
}
