package features.demo15_intercept;

import org.junit.jupiter.api.Test;
import org.noear.dami.Dami;
import org.noear.dami.bus.DamiBus;

import java.util.concurrent.atomic.AtomicInteger;

public class Demo15 {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.bus()
    DamiBus busStr = Dami.newBus();

    @Test
    public void main() {
        AtomicInteger testObserver = new AtomicInteger();

        //拦截
        busStr.<String>intercept(0, (event, chain) -> {
            System.err.println(event);
            chain.doIntercept(event);
            testObserver.incrementAndGet();
        });

        //发送事件
        busStr.send(topic, "world1");
        busStr.send(topic, "world2");
        busStr.send(topic, "world3");

        assert testObserver.get() == 3;
    }
}
