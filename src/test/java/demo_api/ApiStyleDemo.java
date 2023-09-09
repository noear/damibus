package demo_api;

import demo_api.mod1.UserEventListenerImpl;
import demo_api.mod2.UserEventSender;
import org.noear.dami.Dami;

public class ApiStyleDemo {
    public static void main(String[] args) {
        //设定编码器
        //Dami.api().setCoder(new CoderDefault());

        //添加拦截器
        Dami.intercept((payload, chain) -> {
            System.out.println("拦截：" + payload.toString());
            chain.doIntercept(payload);
        });

        UserEventListenerImpl userEventListener = new UserEventListenerImpl();
        //注册监听器
        Dami.api().registerListener("demo.user", userEventListener);

        //创建发送器
        UserEventSender userEventSender = Dami.api().createSender("demo.user", UserEventSender.class);

        //发送测试
        long rst = userEventSender.created(1, "noear");
        System.out.println("收到返回：" + rst);
        userEventSender.updated(2, "dami");

        //注销监听器
        Dami.api().unregisterListener("demo.user", userEventListener);
    }
}
