package features.demo31_api;

import features.demo31_api.module1.UserEventListenerOfModule1;
import features.demo31_api.module2.UserEventSender;
import org.junit.Test;
import org.noear.dami.Dami;
import org.noear.dami.api.DamiApi;
import org.noear.dami.api.DamiApiImpl;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;

public class Demo31 {
    static String topicMapping = "demo.user";
    //定义实例，避免单测干扰 //开发时用：Dami.api()
    DamiBus bus = new DamiBusImpl();
    DamiApi api = new DamiApiImpl(bus);

    @Test
    public void main() {
        //注册监听器
        UserEventListenerOfModule1 userEventListener = new UserEventListenerOfModule1();
        api.registerListener(topicMapping, userEventListener);

        //生成发送器
        UserEventSender userEventSender = api.createSender(topicMapping, UserEventSender.class);

        //发送测试
        userEventSender.onCreated(1L, "noear");
        Long userId = userEventSender.getUserId("dami");
        System.err.println("收到：响应：userId：" + userId);

        //注销监听器
        api.unregisterListener(topicMapping, userEventListener);

        assert userId != null;
        assert userId == 99L;
    }
}
