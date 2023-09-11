package features.demo21_send;

import org.junit.jupiter.api.Test;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;

import java.util.concurrent.atomic.AtomicInteger;

public class Demo21 {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.<String,String>bus()
    DamiBus<String, String> bus = new DamiBusImpl<>();

    @Test
    public void main() {
        AtomicInteger testObserver = new AtomicInteger();

        //监听事件
        bus.listen(topic, payload -> {
            System.err.println(payload);
            testObserver.incrementAndGet();
        });


        //发送事件
        bus.send(topic, "world");

        assert testObserver.get() == 1;
    }
}
