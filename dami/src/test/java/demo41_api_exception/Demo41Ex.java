package demo41_api_exception;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import demo41_api_exception.module1.UserEventListenerOfModule1;
import demo41_api_exception.module2.UserEventSender;
import org.noear.dami.Dami;
import org.noear.dami.exception.DamiIllegalStateException;

public class Demo41Ex {
    private static Logger logger = LoggerFactory.getLogger(Demo41Ex.class);
    public static void main(String[] args) {
        //注册监听器
        UserEventListenerOfModule1 userEventListener = new UserEventListenerOfModule1();
        Dami.api().registerListener("demo.user", userEventListener);

        //生成发送器
        UserEventSender userEventSender = Dami.api().createSender("demo.user", UserEventSender.class);

        //发送测试
        try {
            userEventSender.onCreated(1L, "noear");
        }catch (DamiIllegalStateException e){
            Throwable rootCause = e.getRootCause();
            logger.error("捕获到异常：", rootCause);
        }

        //注销监听器
        Dami.api().unregisterListener("demo.user", userEventListener);
    }
}
