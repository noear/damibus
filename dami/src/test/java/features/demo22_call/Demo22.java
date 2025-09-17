package features.demo22_call;

import org.junit.jupiter.api.Test;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;

import java.util.concurrent.atomic.AtomicInteger;

public class Demo22 {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.<Long,String>bus()
    DamiBus bus = new DamiBusImpl();

    @Test
    public void main() throws Exception {
        AtomicInteger testObserver = new AtomicInteger();

        //处理事件
        bus.<Long, String>onCall(topic, (req, resp) -> {
            System.err.println(req);

            resp.complete("hi!");
        });


        //调用事件
        String rst1 = bus.<Long, String>call(topic, 2L).get();

        System.out.println(rst1);
        assert "hi!".equals(rst1);
    }
}