package features.demo13_subscribe;

import org.junit.jupiter.api.Test;
import org.noear.dami.Dami;
import org.noear.dami.api.DamiApi;
import org.noear.solon.rx.SimpleSubscriber;
import org.noear.solon.rx.SimpleSubscription;

/**
 *
 * @author noear 2025/9/16 created
 *
 */
public class Demo13 {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.bus()
    DamiApi api = Dami.newApi();


    @Test
    public void main() throws Exception {
        //监听事件
        api.<String, String>feed(topic, (req, subs) -> {
            System.out.println(Thread.currentThread());
            System.err.println(req);

            subs.onSubscribe(new SimpleSubscription()
                    .onRequest((s, l) -> {
                        for (int i = 0; i < l; i++) {
                            subs.onNext("test");
                        }
                    }));
        });

        System.out.println(Thread.currentThread());

        //发送事件
        api.<String, String>stream(topic, "world").subscribe(new SimpleSubscriber<>()
                .doOnNext(item -> {
                    System.out.println(item);
                }));
    }
}
