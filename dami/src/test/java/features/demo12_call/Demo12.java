package features.demo12_call;

import org.junit.jupiter.api.Test;
import org.noear.dami.Dami;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;
import org.noear.dami.bus.payload.RequestPayload;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class Demo12 {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.bus()
    DamiBus<RequestPayload<String, String>> busStr = Dami.newBus();

    @Test
    public void main() throws Exception {
        AtomicInteger testObserver = new AtomicInteger();

        //监听事件
        busStr.listen(topic, message -> {
            System.out.println(Thread.currentThread());
            System.err.println(message);

            if (message.getPayload().getResponse() != null) {
                message.getPayload().getResponse().complete("hi!");
            }
        });

        System.out.println(Thread.currentThread());

        //发送事件
        String rst1 = busStr.send(topic, new RequestPayload<>("world"))
                .getPayload()
                .getResponse()
                .get();

        System.out.println(rst1);
        assert "hi!".equals(rst1);
    }
}
