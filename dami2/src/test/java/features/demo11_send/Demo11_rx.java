package features.demo11_send;

import org.junit.jupiter.api.Test;
import org.noear.dami2.Dami;
import org.noear.dami2.bus.DamiBus;
import org.noear.dami2.bus.receivable.StreamPayload;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicInteger;

public class Demo11_rx {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.bus()

    @Test
    public void main() throws Exception {
        DamiBus bus = Dami.newBus();

        AtomicInteger testObserver = new AtomicInteger();

        //监听事件
        bus.<StreamPayload<String, String>>listen(topic, event -> {
            System.err.println(event);
            event.getPayload().getSink().onNext("hello");
            event.getPayload().getSink().onComplete();
            testObserver.incrementAndGet();
        });


        //发送事件
        bus.send(topic, new StreamPayload<>("world", new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription subscription) {

            }

            @Override
            public void onNext(String s) {
                System.err.println(s);
                testObserver.incrementAndGet();
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        }));

        assert testObserver.get() == 2;
    }
}
