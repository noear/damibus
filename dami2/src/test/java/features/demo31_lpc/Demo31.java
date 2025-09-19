package features.demo31_lpc;

import features.demo31_lpc.module1.UserServiceImpl;
import features.demo31_lpc.module2.UserService;

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
        //注册服务提供者
        UserServiceImpl userEventListener = new UserServiceImpl();
        lpc.registerProvider(topicMapping, userEventListener);

        //创建服务消费者（接口代理）
        UserService eventUser = lpc.createConsumer(topicMapping, UserService.class);

        //发送测试
        eventUser.onCreated(1L, "noear");
        Long userId = eventUser.getUserId("dami");
        System.err.println("收到：响应：userId：" + userId);

        //注销监听器
        lpc.unregisterProvider(topicMapping, userEventListener);

        assert userId != null;
        assert userId == 99L;
    }
}
