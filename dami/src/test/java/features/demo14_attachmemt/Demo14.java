package features.demo14_attachmemt;

import org.junit.jupiter.api.Test;
import org.noear.dami2.Dami;
import org.noear.dami2.bus.DamiBus;

import java.util.concurrent.atomic.AtomicInteger;

public class Demo14 {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.bus()
    DamiBus busStr = Dami.newBus();

    @Test
    public void main() {
        AtomicInteger testObserver = new AtomicInteger();

        //监听事件
        busStr.<String>listen(topic, 1, event -> {
            System.err.println(event);
            event.setAttachment("name", "noear");
            testObserver.incrementAndGet();
        });

        //监听事件
        busStr.<String>listen(topic, 2, payload -> {
            System.err.println(payload);
            testObserver.incrementAndGet();
        });


        //发送事件
        busStr.send(topic, "world");

        assert testObserver.get() == 2;
    }
}
