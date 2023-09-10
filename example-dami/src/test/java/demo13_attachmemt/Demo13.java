package demo13_attachmemt;

import org.noear.dami.Dami;

public class Demo13 {
    static String topic = "demo.hello";

    public static void main(String[] args) {
        //监听事件
        Dami.busStr().listen(topic, 1, payload -> {
            System.err.println(payload);
            payload.setAttachment("name", "noear");
        });

        //监听事件
        Dami.busStr().listen(topic, 2, payload -> {
            System.err.println(payload);
        });


        //发送事件
        Dami.busStr().send(topic, "world");
    }
}
