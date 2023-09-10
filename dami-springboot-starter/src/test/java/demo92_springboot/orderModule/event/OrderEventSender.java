package demo92_springboot.orderModule.event;

import org.noear.dami.spring.boot.annotation.DamiTopic;

@DamiTopic("demo.order")
public interface OrderEventSender {
    void onCreated(long orderId);
}
