package features.demo11_send;

import org.junit.jupiter.api.Test;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Demo11_async {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.bus()
    DamiBus<String, String> busStr = new DamiBusImpl<>();

    @Test
    public void main() throws Exception {
        CountDownLatch testObserver = new CountDownLatch(1);

        //监听事件
        busStr.listen(topic, message -> {
            CompletableFuture.runAsync(()-> {
                System.err.println(message);
                testObserver.countDown();
            });
        });


        //发送事件
        busStr.send(topic, "world");

        assert testObserver.await(1, TimeUnit.SECONDS);
    }
}
