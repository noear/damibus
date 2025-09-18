package features.demo13_stream;

import org.junit.jupiter.api.Test;
import org.noear.dami2.Dami;
import org.noear.dami2.bus.DamiBus;
import reactor.core.publisher.Flux;

/**
 *
 * @author noear 2025/9/16 created
 *
 */
public class Demo13 {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.bus()
    DamiBus bus = Dami.newBus();


    @Test
    public void main() throws Exception {
        //监听事件
        bus.<String, String>listen(topic, (event, att,data, sink) -> {
            System.out.println(Thread.currentThread());
            System.err.println(data);

            sink.onNext("hello");
            sink.onComplete();

//            sink.onSubscribe(new SimpleSubscription()
//                    .onRequest((s, l) -> {
//                        for (int i = 0; i < l; i++) {
//                            sink.onNext("test");
//                        }
//                        s.cancel();
//                    }));
        });

        System.out.println(Thread.currentThread());

        //发送事件
//        bus.<String, String>stream(topic, "world")
//                .subscribe(new SimpleSubscriber<>()
//                        .doOnNext(item -> {
//                            System.out.println(Thread.currentThread());
//                            System.out.println(item);
//                        }));

        String rst = Flux.from(bus.<String, String>stream(topic, "world"))
                .doOnNext(item -> {
                    System.out.println(Thread.currentThread());
                    System.out.println(item);
                })
                .blockLast();

        assert "hello".equals(rst);
    }
}
