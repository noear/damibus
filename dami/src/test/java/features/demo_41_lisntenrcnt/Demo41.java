package features.demo_41_lisntenrcnt;

import org.junit.jupiter.api.Test;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;

import java.util.concurrent.atomic.AtomicInteger;

public class Demo41 {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.bus()
    DamiBus<String> busStr = new DamiBusImpl<>();

    @Test
    public void main() throws Exception {
        AtomicInteger testObserver = new AtomicInteger();

        //监听事件
        busStr.listen(topic, payload -> {
            System.err.println(payload);
            testObserver.incrementAndGet();
        });


        //发送事件
        busStr.send(topic, "world");
        assert testObserver.get() == 1;

        assert busStr.router().count(topic) == 1;

        busStr.unlisten(topic);
        assert busStr.router().count(topic) == 0;
    }
}
