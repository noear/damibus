package demo_api;

import demo_api.mod1.UserEventListenerImpl;
import demo_api.mod2.UserEventSender;
import org.noear.dami.Dami;

public class ApiDemo {
    public static void main(String[] args) {
        UserEventListenerImpl userEventListener = new UserEventListenerImpl();
        //注册监听器
        Dami.api().registerListener("demo.user", userEventListener);

        //创建发送器
        UserEventSender userEventSender = Dami.api().createSender("demo.user", UserEventSender.class);

        //发送测试
        userEventSender.created(1, "noear");
        userEventSender.updated(2, "dami");

        //注销监听器
        Dami.api().unregisterListener("demo.user", userEventListener);
    }
}
