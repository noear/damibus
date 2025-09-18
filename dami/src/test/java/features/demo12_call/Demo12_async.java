package features.demo12_call;

import org.junit.jupiter.api.Test;
import org.noear.dami2.Dami;
import org.noear.dami2.bus.DamiBus;

import java.util.concurrent.CompletableFuture;

public class Demo12_async {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.bus()
    DamiBus bus = Dami.newBus();

    @Test
    public void main() throws Exception {

        //监听事件
        bus.<String, String>onCall(topic, (content, sink) -> {
            CompletableFuture.runAsync(() -> {
                System.out.println(Thread.currentThread());
                System.err.println(content);

                sink.complete("hi!");
            });
        });

        System.out.println(Thread.currentThread());

        //发送事件
        String rst1 = bus.<String, String>call(topic, "world").get();

        System.out.println(rst1);
        assert "hi!".equals(rst1);
    }
}
