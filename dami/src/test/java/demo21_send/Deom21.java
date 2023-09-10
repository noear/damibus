package demo21_send;

import org.noear.dami.Dami;

public class Deom21 {
    static String topic = "demo.hello";

    public static void main(String[] args) {
        //监听事件
        Dami.<String, String>bus().listen(topic, payload -> {
            System.err.println(payload);
        });


        //发送事件
        Dami.<String, String>bus().send(topic, "world");
    }
}
