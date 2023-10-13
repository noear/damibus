package features.demo22_rquest;

import org.junit.jupiter.api.Test;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Demo22_async {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.<Long,String>bus()
    DamiBus<Long, String> bus = new DamiBusImpl<>();

    @Test
    public void main() throws Exception{
        CountDownLatch testObserver = new CountDownLatch(3);

        System.out.println(Thread.currentThread());

        //监听事件
        bus.listen(topic, payload -> {
            CompletableFuture.runAsync(()->{
                System.out.println(Thread.currentThread());
                System.err.println(payload);

                if (payload.isRequest()) {
                    payload.reply("hi!"); // sendAndRequest 只接收第一个
                    payload.reply("* hi nihao!");
                    payload.reply("** hi nihao!");
                }
            });
        });


        //发送事件
        String rst1 = bus.sendAndRequest(topic, 2L);
        System.out.println(rst1);
        assert "hi!".equals(rst1);

        bus.sendAndSubscribe(topic, 3L, rst2 -> {
            System.out.println(rst2); //callback 不限回调次数
            testObserver.countDown();
        });

        assert testObserver.await(1, TimeUnit.SECONDS);
    }
}
