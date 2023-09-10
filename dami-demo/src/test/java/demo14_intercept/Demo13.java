package demo14_intercept;

import org.noear.dami.Dami;

public class Demo13 {
    static String topic = "demo.hello";

    public static void main(String[] args) {
        //拦截
        Dami.intercept((payload, chain) -> {
            System.err.println(payload);
            chain.doIntercept(payload);
        });

        //发送事件
        Dami.busStr().send(topic, "world1");
        Dami.busStr().send(topic, "world2");
        Dami.busStr().send(topic, "world3");
    }
}
