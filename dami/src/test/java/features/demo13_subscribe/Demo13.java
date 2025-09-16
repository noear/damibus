package features.demo13_subscribe;

import org.junit.jupiter.api.Test;
import org.noear.dami.Dami;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.payload.ReceivePayload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author noear 2025/9/16 created
 *
 */
public class Demo13 {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.bus()
    DamiBus<ReceivePayload<String, FluxSink<String>>> busStr = Dami.newBus();

    @Test
    public void main() throws Exception {
        AtomicInteger testObserver = new AtomicInteger();

        //监听事件
        busStr.listen(topic, message -> {
            System.out.println(Thread.currentThread());
            System.err.println(message);

            message.getPayload().getReceiver().next("hi!");
            message.getPayload().getReceiver().complete();
        });

        System.out.println(Thread.currentThread());

        //发送事件

        Flux.<String>create(sink -> {
            busStr.send(topic, new ReceivePayload<>("world", sink), r -> {
                r.getReceiver().isCancelled();
            });
        });
    }
}
