package features.demo16_patterned;

import org.noear.dami2.Dami;
import org.noear.dami2.DamiConfig;
import org.noear.dami2.bus.DamiBusImpl;
import org.noear.dami2.bus.route.PathTopicEventRouter;

import java.util.concurrent.atomic.AtomicInteger;


public class Demo16_custom {
    public static void main(String[] args) {
        //::切换为模式匹配路由器（支持 * 和 ** 占位符；支持 / 或 . 做为间隔）

        DamiConfig.configure(new DamiBusImpl(new PathTopicEventRouter()));

        //::应用
        AtomicInteger testObserver = new AtomicInteger();

        //拦截
        Dami.bus().<String>listen("demo/*/**", e -> {
            System.err.println(e.getPayload());
            testObserver.incrementAndGet();
        });

        //发送事件
        Dami.bus().send("demo/a/1", "world1");
        Dami.bus().send("demo/a/2", "world2");
        Dami.bus().send("Demo/b/1/2", "world3"); //大小写敏感

        assert testObserver.get() == 3;
    }
}
