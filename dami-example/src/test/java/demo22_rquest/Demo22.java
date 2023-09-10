package demo22_rquest;

import org.noear.dami.Dami;

public class Demo22 {
    static String topic = "demo.hello";

    public static void main(String[] args) {
        //监听事件
        Dami.<Long, String>bus().listen(topic, payload -> {
            System.err.println(payload);

            if (payload.isRequest()) {
                Dami.<Long, String>bus().response(payload, "hi nihao!");
                Dami.<Long, String>bus().response(payload, "* hi nihao!");
                Dami.<Long, String>bus().response(payload, "** hi nihao!");
            }
        });


        //发送事件
        String rst1 = Dami.<Long, String>bus().requestAndResponse(topic, 2L);
        System.out.println(rst1);

        Dami.<Long, String>bus().requestAndCallback(topic, 3L, rst2 -> {
            System.out.println(rst2); //callback 可不限返回
        });
    }
}
