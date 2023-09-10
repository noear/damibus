package demo_bus;

import org.noear.dami.Dami;

public class CallbackDemo {
    static String demo_topic = "demo.user.created";

    public static void main(String[] args) {
        //监听
        listen();

        //发送测试
        sendTest();
    }

    //监听
    private static void listen() {
        Dami.busStr().listen(demo_topic,  payload -> {
            //接收处理
            System.out.println(payload);

            if (payload.isRequest()) {
                //如果是请求载体，再响应一下
                Dami.busStr().response(payload, "你发了1：" + payload.getContent());
                Dami.busStr().response(payload, "你发了2：" + payload.getContent());
                Dami.busStr().response(payload, "你发了3：" + payload.getContent());
            }
        });
    }

    //发送测试
    private static void sendTest() {
        //普通发送
        Dami.busStr().send(demo_topic, "{user:'noear'}");

        //请求并等回调
        Dami.busStr().requestAndCallback(demo_topic, "{user:'solon'}", (rst2) -> {
            System.out.println("响应回调: " + rst2);
        });
    }
}
