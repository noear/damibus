package features.demo12_request;

import org.junit.jupiter.api.Test;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Demo12_async {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.bus()
    DamiBus<String, String> busStr = new DamiBusImpl<>();

    @Test
    public void main() throws Exception{
        CountDownLatch testObserver = new CountDownLatch(3);


        //监听事件
        busStr.listen(topic, payload -> {
            CompletableFuture.runAsync(()->{
                System.out.println(Thread.currentThread());
                System.err.println(payload);

                if (payload.isRequest() || payload.isSubscribe()) {
                    payload.reply("hi!"); // sendAndRequest 只接收第一个
                    payload.reply("* hi nihao!");
                    payload.reply("** hi nihao!");
                }
            });
        });

        System.out.println(Thread.currentThread());

        //发送事件
        String rst1 = busStr.sendAndRequest(topic, "world");
        System.out.println(rst1);
        assert "hi!".equals(rst1);

        busStr.sendAndSubscribe(topic, "world", rst2 -> {
            System.out.println(Thread.currentThread());
            System.out.println(rst2); //subscribe 不限回调次数
            testObserver.countDown();
        });

        assert testObserver.await(1, TimeUnit.SECONDS);
    }
}
