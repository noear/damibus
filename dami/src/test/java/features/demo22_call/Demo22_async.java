package features.demo22_call;

import org.junit.jupiter.api.Test;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;
import org.noear.dami.bus.payload.RequestPayload;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class Demo22_async {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.<Long,String>bus()
    DamiBus<RequestPayload<Long, String>> bus = new DamiBusImpl<>();

    @Test
    public void main() throws Exception {
        System.out.println(Thread.currentThread());

        //监听事件
        bus.listen(topic, message -> {
            CompletableFuture.runAsync(() -> {
                System.out.println(Thread.currentThread());
                System.err.println(message);

                message.getPayload().getResponse().complete("hi!");
            });
        });


        //发送事件
        String rst1 = bus.send(topic, new RequestPayload<>(2L))
                .getPayload()
                .getResponse()
                .get();

        System.out.println(rst1);
        assert "hi!".equals(rst1);
    }
}
