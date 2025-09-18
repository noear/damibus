package features.demo12_call;

import org.junit.jupiter.api.Test;
import org.noear.dami2.Dami;
import org.noear.dami2.bus.DamiBus;

public class Demo12 {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.req()
    DamiBus bus = Dami.newBus();

    @Test
    public void main() throws Exception {
        //监听事件
        bus.<String, String>onCall(topic, (event,content, sink) -> {
            System.out.println(Thread.currentThread());
            System.err.println(content);

            sink.complete("hi!");
        });

        System.out.println(Thread.currentThread());

        //发送事件
        String rst1 = bus.<String, String>call(topic, "world").get();

        System.out.println(rst1);
        assert "hi!".equals(rst1);
    }
}