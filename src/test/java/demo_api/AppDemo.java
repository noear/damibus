package demo_api;

import demo_api.mod1.UserEventListenerImpl;
import demo_api.mod2.UserEventSender;
import org.noear.dami.api.DamiApi;
import org.noear.dami.api.DamiApiImpl;

public class AppDemo {
    public static void main(String[] args) {
        DamiApi damiApi = new DamiApiImpl();

        //注册监听器
        damiApi.registerListener("demo.user", new UserEventListenerImpl());

        //创建发送器
        UserEventSender userEventSender = damiApi.createSender("demo.user", UserEventSender.class);

        //发送测试
        userEventSender.created(1,"noear");
        userEventSender.updated(2, "dami");
    }
}
