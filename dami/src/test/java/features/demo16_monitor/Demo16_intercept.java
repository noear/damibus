package features.demo16_monitor;

import org.junit.jupiter.api.Test;
import org.noear.dami.bus.*;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author noear 2023/10/13 created
 */
public class Demo16_intercept {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.bus()
    DamiBus<String, String> busStr = new DamiBusImpl<>();

    @Test
    public void main() throws Exception {
        busStr.intercept((payload, chain) -> {
            System.out.println("开始监视...");
            chain.doIntercept(payload);
            System.out.println("结速监视...");
        });

        AtomicInteger testObserver = new AtomicInteger();

        //监听事件
        busStr.listen(topic, payload -> {
            System.err.println(payload);
            testObserver.incrementAndGet();
        });


        //发送事件
        busStr.send(topic, "world");

        assert testObserver.get() == 1;
    }
}