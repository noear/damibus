package features.demo12_call;

import org.junit.jupiter.api.Test;
import org.noear.dami2.Dami;
import org.noear.dami2.bus.DamiBus;

import java.util.HashMap;
import java.util.Map;

public class Demo12 {
    static String topic = "demo.hello";
    //定义实例，避免单测干扰 //开发时用：Dami.req()
    DamiBus bus = Dami.newBus();

    @Test
    public void main() throws Exception {
        //监听事件
        bus.<String, String>listen(topic, (event, data, sink) -> {
            System.out.println(Thread.currentThread());
            System.err.println(data);

            sink.complete("hi!");
        });

        System.out.println(Thread.currentThread());

        //发送事件
        String rst1 = bus.<String, String>call(topic, "world").get();

        System.out.println(rst1);
        assert "hi!".equals(rst1);
    }

    @Test
    public void fallback() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("content", "demo");
        String str = Dami.bus().<Map<String, String>, String>call("mp.msg", map).get();

        System.out.println(str);
    }
}