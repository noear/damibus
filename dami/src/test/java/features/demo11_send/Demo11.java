package features.demo11_send;

import org.junit.jupiter.api.Test;
import org.noear.dami.Dami;
import org.noear.dami.bus.DamiBus;

import java.util.concurrent.atomic.AtomicInteger;

public class Demo11 {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.bus()
    DamiBus<String> busStr = Dami.newBus();

    @Test
    public void main() throws Exception {
        AtomicInteger testObserver = new AtomicInteger();

        //监听事件
        busStr.listen(topic, message -> {
            System.err.println(message);
            testObserver.incrementAndGet();
        });


        //发送事件
        busStr.send(topic, "world");

        assert testObserver.get() == 1;
    }
}
