package features.demo82_solon.orderModule;

import org.noear.dami.solon.annotation.DamiTopic;

@DamiTopic("demo.order")
public interface OrderEventSender {
    void onCreated(long orderId);
}
