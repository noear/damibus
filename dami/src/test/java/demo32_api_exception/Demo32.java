package demo32_api_exception;

import demo32_api_exception.module1.UserEventListenerOfModule1;
import demo32_api_exception.module2.UserEventSender;
import org.noear.dami.Dami;
import org.noear.dami.exception.DamiException;

public class Demo32 {
    public static void main(String[] args) {
        //注册监听器
        UserEventListenerOfModule1 userEventListener = new UserEventListenerOfModule1();
        Dami.api().registerListener("demo.user", userEventListener);

        //生成发送器
        UserEventSender userEventSender = Dami.api().createSender("demo.user", UserEventSender.class);

        //发送测试
        try {
            userEventSender.onCreated(1L, "noear");
        }catch (DamiException e){
            Throwable rootCause = e.getCause();
            System.out.println("捕获到异常：");
            rootCause.printStackTrace();
        }

        //注销监听器
        Dami.api().unregisterListener("demo.user", userEventListener);
    }
}
