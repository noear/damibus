package demo31_api;

import demo31_api.module1.UserEventListenerImpl;
import demo31_api.module2.UserEventSender;
import org.noear.dami.Dami;

public class Demo31 {
    public static void main(String[] args) {
        UserEventListenerImpl userEventListener = new UserEventListenerImpl();
        UserEventSender userEventSender = Dami.api().createSender("demo.user", UserEventSender.class);

        //注册监听器
        Dami.api().registerListener("demo.user", userEventListener);

        //发送测试
        userEventSender.onCreated(1L, "noear");
        Long userId = userEventSender.getUserId( "dami");
        System.err.println("收到：响应：userId：" + userId);

        //注销监听器
        Dami.api().unregisterListener("demo.user", userEventListener);
    }
}
