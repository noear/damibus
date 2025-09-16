package features.demo16_monitor;

import org.junit.jupiter.api.Test;
import org.noear.dami.Dami;
import org.noear.dami.DamiConfig;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author noear 2023/10/13 created
 */
public class Demo16_dispatcher {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.bus()
    DamiBus<String> busStr = new DamiBusImpl<String>()
            .topicDispatcher(new TopicDispatcherMonitor<>());

    @Test
    public void main() throws Exception {
        AtomicInteger testObserver = new AtomicInteger();

        //监听事件
        busStr.listen(topic, message -> {
            System.out.println(message);
            testObserver.incrementAndGet();
        });


        //发送事件
        busStr.send(topic, "world");

        assert testObserver.get() == 1;
    }
}
