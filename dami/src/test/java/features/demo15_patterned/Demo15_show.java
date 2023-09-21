package features.demo15_patterned;

import org.noear.dami.Dami;
import org.noear.dami.DamiConfig;
import org.noear.dami.bus.DamiBusImpl;
import org.noear.dami.bus.impl.TopicRouterPatterned;

/**
 * @author noear 2023/9/21 created
 */
public class Demo15_show {
    public void demo(){
        //::切换为模式匹配路由器

        DamiConfig.configure(new DamiBusImpl(new TopicRouterPatterned()));

        //::应用

        //拦截
        Dami.bus().listen("demo/*/**", (payload) -> {
            System.err.println(payload);
        });

        //发送事件
        Dami.bus().send("demo/a/1", "world1");
        Dami.bus().send("demo/a/2", "world2");
        Dami.bus().send("Demo/b/1/2", "world3");
    }
}
