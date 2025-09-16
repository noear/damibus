package features.demo15_patterned;

import org.junit.jupiter.api.Test;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;
import org.noear.dami.bus.impl.RoutingPath;
import org.noear.dami.bus.impl.TopicRouterPatterned;

import java.util.concurrent.atomic.AtomicInteger;

public class Demo15_path {
    @Test
    public void test1_a() {
        //定义实例，避免单测干扰 //开发时用：Dami.bus()
        DamiBus<String, String> busStr = new DamiBusImpl<>(new TopicRouterPatterned<>(RoutingPath::new));

        AtomicInteger testObserver = new AtomicInteger();

        //拦截
        busStr.listen("demo.a.*", (message) -> {
            System.err.println(message);
            testObserver.incrementAndGet();
        });

        //发送事件
        busStr.send("demo.a.1", "world1");
        busStr.send("demo.a.2", "world2");
        busStr.send("demo.b.1.2", "world3");

        assert testObserver.get() == 2;
    }

    @Test
    public void test1_b() {
        //定义实例，避免单测干扰 //开发时用：Dami.bus()
        DamiBus<String, String> busStr = new DamiBusImpl<>(new TopicRouterPatterned<>(RoutingPath::new));

        AtomicInteger testObserver = new AtomicInteger();

        //拦截
        busStr.listen("demo.**.*", (payload) -> {
            System.err.println(payload);
            testObserver.incrementAndGet();
        });

        //发送事件
        busStr.send("demo.a.1", "world1");
        busStr.send("demo.a.2", "world2");
        busStr.send("demo.b.1.2", "world3");

        assert testObserver.get() == 3;
    }

    @Test
    public void test2_a() {
        //定义实例，避免单测干扰 //开发时用：Dami.bus()
        DamiBus<String, String> busStr = new DamiBusImpl<>(new TopicRouterPatterned<>(RoutingPath::new));

        AtomicInteger testObserver = new AtomicInteger();

        //拦截
        busStr.listen("demo/a/*", (payload) -> {
            System.err.println(payload);
            testObserver.incrementAndGet();
        });

        //发送事件
        busStr.send("demo/a/1", "world1");
        busStr.send("demo/a/2", "world2");
        busStr.send("demo/b/1/2", "world3");

        assert testObserver.get() == 2;
    }

    @Test
    public void test2_b() {
        //定义实例，避免单测干扰 //开发时用：Dami.bus()
        DamiBus<String, String> busStr = new DamiBusImpl<>(new TopicRouterPatterned<>(RoutingPath::new));

        AtomicInteger testObserver = new AtomicInteger();

        //拦截
        busStr.listen("demo/*/**", (payload) -> {
            System.err.println(payload);
            testObserver.incrementAndGet();
        });

        //发送事件
        busStr.send("demo/a/1", "world1");
        busStr.send("demo/a/2", "world2");
        busStr.send("demo/b/1/2", "world3");

        assert testObserver.get() == 3;
    }

    @Test
    public void test2_c() {
        //定义实例，避免单测干扰 //开发时用：Dami.bus()
        DamiBus<String, String> busStr = new DamiBusImpl<>(new TopicRouterPatterned<>(RoutingPath::new));

        AtomicInteger testObserver = new AtomicInteger();

        //拦截
        busStr.listen("demo/*/**", (payload) -> {
            System.err.println(payload);
            testObserver.incrementAndGet();
        });

        //发送事件
        busStr.send("demo/a/1", "world1");
        busStr.send("demo/a/2", "world2");
        busStr.send("Demo/b/1/2", "world3");

        assert testObserver.get() == 2;
    }
}
