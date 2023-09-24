package features.demo15_patterned;

import org.junit.jupiter.api.Test;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;
import org.noear.dami.bus.impl.RoutingTag;
import org.noear.dami.bus.impl.TopicRouterPatterned;

import java.util.concurrent.atomic.AtomicInteger;

public class Demo15_tag {

    @Test
    public void test3_1() {
        //定义实例，避免单测干扰 //开发时用：Dami.bus()
        DamiBus<String, String> busStr = new DamiBusImpl<>(new TopicRouterPatterned<>(RoutingTag::new));

        AtomicInteger testObserver = new AtomicInteger();

        //拦截1
        busStr.listen("demo:a,b", (payload) -> {
            System.err.println(payload);
            testObserver.incrementAndGet();
        });
        //拦截2
        busStr.listen("demo:b,c", (payload) -> {
            System.err.println(payload);
            testObserver.incrementAndGet();
        });

        //发送事件
        //发送的事件的tag只有有一个存在于监听的tag里面则会匹配
        //2
        busStr.send("demo", "world");
        //1
        busStr.send("demo:a", "world1");
        //2
        busStr.send("demo:b", "world2");
        //1
        busStr.send("demo:c", "world3");
        //2
        busStr.send("demo:c,a", "world4");

        assert testObserver.get() == 8;
    }
}
