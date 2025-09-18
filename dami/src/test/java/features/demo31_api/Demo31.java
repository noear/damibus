package features.demo31_api;

import features.demo31_api.module1.EventUserListener1;
import features.demo31_api.module2.EventUser;

import org.junit.jupiter.api.Test;
import org.noear.dami.lpc.DamiLpc;
import org.noear.dami.lpc.DamiLpcImpl;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;

public class Demo31 {
    static String topicMapping = "demo.user";
    //定义实例，避免单测干扰 //开发时用：Dami.api()
    DamiBus bus = new DamiBusImpl();
    DamiLpc api = new DamiLpcImpl(bus);

    @Test
    public void main() {
        //注册监听器
        EventUserListener1 userEventListener = new EventUserListener1();
        api.registerService(topicMapping, userEventListener);

        //生成发送器
        EventUser eventUser = api.createConsumer(topicMapping, EventUser.class);

        //发送测试
        eventUser.onCreated(1L, "noear");
        Long userId = eventUser.getUserId("dami");
        System.err.println("收到：响应：userId：" + userId);

        //注销监听器
        api.unregisterService(topicMapping, userEventListener);

        assert userId != null;
        assert userId == 99L;
    }
}
