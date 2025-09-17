package features.demo11_send;

import org.junit.jupiter.api.Test;
import org.noear.dami.Dami;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.payload.SubscribePayload;
import org.noear.solon.rx.SimpleSubscriber;

import java.util.concurrent.atomic.AtomicInteger;

public class Demo11_rx {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.bus()

    @Test
    public void main() throws Exception {
        DamiBus bus = Dami.newBus();

        AtomicInteger testObserver = new AtomicInteger();

        //监听事件
        bus.<SubscribePayload<String, String>>listen(topic, message -> {
            System.err.println(message);
            message.getPayload().getReceiver().onNext("hello");
            testObserver.incrementAndGet();
        });


        //发送事件
        bus.send(topic, new SubscribePayload<>("world", new SimpleSubscriber<String>()
                .doOnNext(item -> {
                    System.err.println(item);
                })));

        assert testObserver.get() == 1;
    }
}
