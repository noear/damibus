package features.demo11_send;

import org.junit.jupiter.api.Test;
import org.noear.dami2.Dami;
import org.noear.dami2.bus.DamiBus;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Demo11_async {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.bus()
    DamiBus busStr = Dami.newBus();

    @Test
    public void main() throws Exception {
        CountDownLatch testObserver = new CountDownLatch(1);

        //监听事件
        busStr.<String>listen(topic, event -> {
            CompletableFuture.runAsync(()-> {
                System.err.println(event);
                testObserver.countDown();
            });
        });


        //发送事件
        busStr.send(topic, "world");

        assert testObserver.await(1, TimeUnit.SECONDS);
    }
}
