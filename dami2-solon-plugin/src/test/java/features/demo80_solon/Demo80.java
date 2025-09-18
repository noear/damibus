package features.demo80_solon;

import org.noear.dami2.Dami;

/**
 *
 * @author noear 2025/9/18 created
 *
 */
public class Demo80 {
    static String topic = "demo.hello";

    public static void main(String[] args) throws Exception {
        //监听事件（调用事件）
        Dami.bus().<String, String>listen(topic, (event, data, receiver) -> {
            System.err.println(event);

            receiver.complete("hi!");
        });


        //发送事件（调用）
        String rst1 = Dami.bus().<String, String>call(topic, "world").get();
        //发送事件（调用） //支持应急处理（或降级处理）（没有订阅时触发时）
        //String rst1 = Dami.bus().<String, String>call(topic, "world", r -> r.complete("def")).get();
        System.out.println(rst1);
    }
}
