package features.demo22_call;

import org.junit.jupiter.api.Test;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

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

                if (payload.requiredReply()) {
                    payload.reply("hi!");
                }
            });
        });


        //发送事件
        String rst1 = bus.call(topic, 2L);
        System.out.println(rst1);
        assert "hi!".equals(rst1);
    }
}
