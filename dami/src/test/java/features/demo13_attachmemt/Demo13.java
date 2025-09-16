package features.demo13_attachmemt;

import org.junit.jupiter.api.Test;
import org.noear.dami.Dami;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;

import java.util.concurrent.atomic.AtomicInteger;

public class Demo13 {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.bus()
    DamiBus<String> busStr = Dami.newBus();

    @Test
    public void main() {
        AtomicInteger testObserver = new AtomicInteger();

        //监听事件
        busStr.listen(topic, 1, message -> {
            System.err.println(message);
            message.setAttachment("name", "noear");
            testObserver.incrementAndGet();
        });

        //监听事件
        busStr.listen(topic, 2, payload -> {
            System.err.println(payload);
            testObserver.incrementAndGet();
        });


        //发送事件
        busStr.send(topic, "world");

        assert testObserver.get() == 2;
    }
}
