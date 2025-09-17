package features.demo17_monitor;

import org.junit.jupiter.api.Test;
import org.noear.dami.bus.*;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author noear 2023/10/13 created
 */
public class Demo17_intercept {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.bus()
    DamiBus busStr = new DamiBusImpl();

    @Test
    public void main() throws Exception {
        busStr.<String>intercept((message, chain) -> {
            System.out.println("开始监视...");
            chain.getTargets().forEach(e->System.out.println(e.getListener()));
            chain.doIntercept(message);
            System.out.println("结速监视...");
        });

        AtomicInteger testObserver = new AtomicInteger();

        //监听事件
        busStr.<String>listen(topic, message -> {
            System.err.println(message);
            testObserver.incrementAndGet();
        });


        //发送事件
        busStr.send(topic, "world");

        assert testObserver.get() == 1;
    }
}