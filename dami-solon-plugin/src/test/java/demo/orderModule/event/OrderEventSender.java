package demo.orderModule.event;

import org.noear.dami.solon.annotation.DamiTopic;

@DamiTopic("demo.order")
public interface OrderEventSender {
    void onCreated(long orderId);
}
