package features.demo14_intercept;

import org.junit.Test;
import org.noear.dami.Dami;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;

import java.util.concurrent.atomic.AtomicInteger;

public class Demo13 {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.busStr()
    DamiBus<String, String> busStr = new DamiBusImpl<>();

    @Test
    public void main() {
        AtomicInteger testObserver = new AtomicInteger();

        //拦截
        busStr.intercept(0, (payload, chain) -> {
            System.err.println(payload);
            chain.doIntercept(payload);
            testObserver.incrementAndGet();
        });

        //发送事件
        busStr.send(topic, "world1");
        busStr.send(topic, "world2");
        busStr.send(topic, "world3");

        assert testObserver.get() == 3;
    }
}
