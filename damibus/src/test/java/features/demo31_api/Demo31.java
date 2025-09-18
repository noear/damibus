package features.demo31_api;

import features.demo31_api.module1.EventUserListener1;
import features.demo31_api.module2.EventUser;

import org.junit.jupiter.api.Test;
import org.noear.dami2.lpc.DamiLpc;
import org.noear.dami2.lpc.DamiLpcImpl;
import org.noear.dami2.bus.DamiBus;
import org.noear.dami2.bus.DamiBusImpl;

public class Demo31 {
    static String topicMapping = "demo.user";
    //定义实例，避免单测干扰 //开发时用：Dami.lpc()
    DamiBus bus = new DamiBusImpl();
    DamiLpc lpc = new DamiLpcImpl(bus);

    @Test
    public void main() {
        //注册监听器
        EventUserListener1 userEventListener = new EventUserListener1();
        lpc.registerService(topicMapping, userEventListener);

        //生成发送器
        EventUser eventUser = lpc.createConsumer(topicMapping, EventUser.class);

        //发送测试
        eventUser.onCreated(1L, "noear");
        Long userId = eventUser.getUserId("dami");
        System.err.println("收到：响应：userId：" + userId);

        //注销监听器
        lpc.unregisterService(topicMapping, userEventListener);

        assert userId != null;
        assert userId == 99L;
    }
}
