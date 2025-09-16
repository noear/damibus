package features.demo11_send;

import org.junit.jupiter.api.Test;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;
import org.noear.dami.bus.impl.IdGeneratorGuid;
import org.noear.solon.rx.SimpleSubscriber;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicInteger;

public class Demo11_rx {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.bus()

    @Test
    public void main() throws Exception {
        DamiBus<Subscriber<String>, ?> bus = new DamiBusImpl<Subscriber<String>, String>();

        AtomicInteger testObserver = new AtomicInteger();

        //监听事件
        bus.listen(topic, payload -> {
            System.err.println(payload);
            payload.getContent().onNext("hello");
            testObserver.incrementAndGet();
        });


        //发送事件
        bus.send(topic, new SimpleSubscriber<String>().doOnNext(item -> {
            System.err.println(item);
        }));

        assert testObserver.get() == 1;
    }
}
