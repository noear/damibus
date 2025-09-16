package features.demo16_patterned;

import org.noear.dami.Dami;
import org.noear.dami.DamiConfig;
import org.noear.dami.bus.DamiBusImpl;
import org.noear.dami.bus.impl.RoutingPath;
import org.noear.dami.bus.impl.TopicRouterPatterned;


public class Demo16_custom {
    public static void main(String[] args){
        //::切换为模式匹配路由器（支持 * 和 ** 占位符；支持 / 或 . 做为间隔）

        DamiConfig.configure(new DamiBusImpl(new TopicRouterPatterned(RoutingPath::new)));

        //::应用

        //拦截
        Dami.bus().listen("demo/*/**", System.err::println);

        //发送事件
        Dami.bus().send("demo/a/1", "world1");
        Dami.bus().send("demo/a/2", "world2");
        Dami.bus().send("Demo/b/1/2", "world3"); //大小写敏感
    }
}
