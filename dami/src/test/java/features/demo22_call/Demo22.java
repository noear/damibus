package features.demo22_call;

import org.junit.jupiter.api.Test;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;
import org.noear.dami.bus.payload.RequestPayload;

import java.util.concurrent.atomic.AtomicInteger;

public class Demo22 {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.<Long,String>bus()
    DamiBus<RequestPayload<Long, String>> bus = new DamiBusImpl<>();

    @Test
    public void main() throws Exception {
        AtomicInteger testObserver = new AtomicInteger();

        //监听事件
        bus.listen(topic, message -> {
            System.err.println(message);

            message.getPayload().getReceiver().complete("hi!");
        });


        //发送事件
        String rst1 = bus.send(topic, new RequestPayload<>(2L))
                .getPayload()
                .getReceiver()
                .get();

        System.out.println(rst1);
        assert "hi!".equals(rst1);
    }
}