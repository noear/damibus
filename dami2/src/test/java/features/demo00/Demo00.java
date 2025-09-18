package features.demo00;

import org.noear.dami2.Dami;
import reactor.core.publisher.Flux;

/**
 * @author noear 2023/10/7 created
 */
public class Demo00 {
    static String topic = "demo.hello";

    public void case_send() {
        //监听事件
        Dami.bus().listen(topic, event -> {
            System.err.println(event.getPayload());
        });

        //发送事件
        Dami.bus().send(topic, "hello");
    }

    public void case_call() throws Exception {
        //监听事件
        Dami.bus().listen(topic, (event, content, receiver) -> {
            System.err.println(event.getPayload());
            receiver.complete("hi!");
        });

        //发送事件
        String rst = Dami.bus().<String, String>call(topic, "hello").get();
    }

    public void case_stream() {
        //监听事件
        Dami.bus().listen(topic, (event, att, content, receiver) -> {
            System.err.println(event.getPayload());
            receiver.onNext("hi");
            receiver.onComplete();
        });

        //发送事件
        Flux.from(Dami.bus().stream(topic, "hello")).doOnNext(item -> {
            System.err.println(item);
        });
    }
}
