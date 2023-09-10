package demo11_event;

import org.noear.dami.Dami;

public class Deom11 {
    static String topic = "demo.hello";

    public static void main(String[] args) {
        //监听事件
        Dami.busStr().listen(topic, payload -> {
            System.err.println(payload);
        });


        //发送事件
        Dami.busStr().send(topic, "world");
    }
}
