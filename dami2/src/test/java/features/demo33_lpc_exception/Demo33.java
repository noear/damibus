package features.demo33_lpc_exception;

import features.demo33_lpc_exception.module1.EventUserListenerOfModule1;
import features.demo33_lpc_exception.module2.EventUser;

import org.junit.jupiter.api.Test;
import org.noear.dami2.lpc.CoderForIndex;
import org.noear.dami2.lpc.DamiLpc;
import org.noear.dami2.lpc.DamiLpcImpl;
import org.noear.dami2.bus.DamiBus;
import org.noear.dami2.bus.DamiBusImpl;
import org.noear.dami2.exception.DamiException;

public class Demo33 {
    static String topicMapping = "demo.user";
    //定义实例，避免单测干扰 //开发时用：Dami.lpc()
    DamiBus bus = new DamiBusImpl();
    DamiLpc api = new DamiLpcImpl(bus).coder(new CoderForIndex());

    @Test
    public void main() {
        Throwable testObserver = null;

        //注册监听器
        EventUserListenerOfModule1 userEventListener = new EventUserListenerOfModule1();
        api.registerProvider(topicMapping, userEventListener);

        //生成发送器
        EventUser eventUser = api.createConsumer(topicMapping, EventUser.class);

        //发送测试
        try {
            eventUser.onCreated(1L, "noear");
        } catch (DamiException e) {
            testObserver = e.getCause();
            System.out.println("捕获到异常：");
            e.printStackTrace();
        }

        //注销监听器
        api.unregisterProvider(topicMapping, userEventListener);

        assert testObserver != null;
        assert testObserver.getClass() == RuntimeException.class;
    }
}
