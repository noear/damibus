package features.demo22_rquest;

import org.junit.jupiter.api.Test;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;

import java.util.concurrent.atomic.AtomicInteger;

public class Demo22 {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.<Long,String>bus()
    DamiBus<Long, String> bus = new DamiBusImpl<>();

    @Test
    public void main() {
        AtomicInteger testObserver = new AtomicInteger();

        //监听事件
        bus.listen(topic, payload -> {
            System.err.println(payload);

            if (payload.isRequest()) {
                bus.response(payload, "hi!");
                bus.response(payload, "* hi nihao!");
                bus.response(payload, "** hi nihao!");
            }
        });


        //发送事件
        String rst1 = bus.requestAndResponse(topic, 2L);
        System.out.println(rst1);
        assert "hi!".equals(rst1);

        bus.requestAndCallback(topic, 3L, rst2 -> {
            System.out.println(rst2); //callback 可不限返回
            testObserver.incrementAndGet();
        });

        assert testObserver.get() == 3;
    }
}
