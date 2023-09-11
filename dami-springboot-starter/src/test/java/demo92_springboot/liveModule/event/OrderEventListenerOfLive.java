package demo92_springboot.liveModule.event;

import org.noear.dami.spring.boot.annotation.DamiTopic;
import org.springframework.stereotype.Component;

@DamiTopic("demo.order")
@Component
public class OrderEventListenerOfLive {
    public void onCreated(long orderId) {
        System.err.println("LIve:Order:onCreated: orderId=" + orderId);
    }
}
