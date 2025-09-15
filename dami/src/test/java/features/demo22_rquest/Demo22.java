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

            if (payload.requiredReply()) {
                payload.reply("hi!");
            }
        });


        //发送事件
        String rst1 = bus.call(topic, 2L);
        System.out.println(rst1);
        assert "hi!".equals(rst1);
    }
}
