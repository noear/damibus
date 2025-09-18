package features.demo22_call;

import org.junit.jupiter.api.Test;
import org.noear.dami2.bus.DamiBus;
import org.noear.dami2.bus.DamiBusImpl;

import java.util.concurrent.CompletableFuture;

public class Demo22_async {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.<Long,String>bus()
    DamiBus bus = new DamiBusImpl();

    @Test
    public void main() throws Exception {
        System.out.println(Thread.currentThread());

        //监听事件
        bus.<Long, String>onCall(topic, (event, content, resp) -> {
            CompletableFuture.runAsync(() -> {
                System.out.println(Thread.currentThread());
                System.err.println(content);

                resp.complete("hi!");
            });
        });


        //发送事件
        String rst1 = bus.<Long, String>call(topic, 2L)
                .get();

        System.out.println(rst1);
        assert "hi!".equals(rst1);
    }
}
