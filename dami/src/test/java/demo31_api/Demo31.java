package demo31_api;

import demo31_api.module1.UserEventListenerOfModule1;
import demo31_api.module2.UserEventSender;
import org.noear.dami.Dami;

public class Demo31 {
    public static void main(String[] args) {
        //注册监听器
        UserEventListenerOfModule1 userEventListener = new UserEventListenerOfModule1();
        Dami.api().registerListener("demo.user", userEventListener);

        //生成发送器
        UserEventSender userEventSender = Dami.api().createSender("demo.user", UserEventSender.class);

        //发送测试
        userEventSender.onCreated(1L, "noear");
        Long userId = userEventSender.getUserId( "dami");
        System.err.println("收到：响应：userId：" + userId);

        //注销监听器
        Dami.api().unregisterListener("demo.user", userEventListener);
    }
}
