package demo12_request;

import org.noear.dami.Dami;

public class Demo12 {
    static String topic = "demo.hello";

    public static void main(String[] args) {
        //监听事件
        Dami.busStr().listen(topic, payload -> {
            System.out.println(Thread.currentThread());
            System.err.println(payload);

            if (payload.isRequest()) {
                Dami.busStr().response(payload, "hi nihao!");
                Dami.busStr().response(payload, "* hi nihao!");
                Dami.busStr().response(payload, "** hi nihao!");
            }
        });

        System.out.println(Thread.currentThread());

        //发送事件
        String rst1 = Dami.busStr().requestAndResponse(topic, "world");
        System.out.println(rst1);

        Dami.busStr().requestAndCallback(topic, "world", rst2 -> {
            System.out.println(Thread.currentThread());
            System.out.println(rst2); //callback 可不限返回
        });
    }
}
