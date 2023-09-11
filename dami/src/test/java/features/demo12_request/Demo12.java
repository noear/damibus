package features.demo12_request;

import org.junit.Test;
import org.noear.dami.Dami;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;

import java.util.concurrent.atomic.AtomicInteger;

public class Demo12 {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.busStr()
    DamiBus<String, String> busStr = new DamiBusImpl<>();

    @Test
    public void main() {
        AtomicInteger testObserver = new AtomicInteger();

        //监听事件
        busStr.listen(topic, payload -> {
            System.out.println(Thread.currentThread());
            System.err.println(payload);

            if (payload.isRequest()) {
                busStr.response(payload, "hi!");
                busStr.response(payload, "* hi nihao!");
                busStr.response(payload, "** hi nihao!");
            }
        });

        System.out.println(Thread.currentThread());

        //发送事件
        String rst1 = busStr.requestAndResponse(topic, "world");
        System.out.println(rst1);
        assert "hi!".equals(rst1);

        busStr.requestAndCallback(topic, "world", rst2 -> {
            System.out.println(Thread.currentThread());
            System.out.println(rst2); //callback 可不限返回
            testObserver.incrementAndGet();
        });

        assert testObserver.get() == 3;
    }
}
