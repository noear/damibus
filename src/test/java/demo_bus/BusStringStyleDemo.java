package demo_bus;

import org.noear.dami.Dami;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.bus.Payload;


public class BusStringStyleDemo {
    static String demo_topic = "demo.user.created";

    public static void main(String[] args) {
        TopicListener<Payload<String, String>> listener = createListener();

        //监听
        Dami.strBus().listen(demo_topic, listener);

        //发送测试
        sendTest();

        //取消监听
        Dami.strBus().unlisten(demo_topic, listener);
    }

    //创建监听器
    private static TopicListener<Payload<String, String>> createListener() {
        return payload -> {
            //接收处理
            System.out.println(payload);

            if (payload.isRequest()) {
                //如果是请求载体，再响应一下
                Dami.strBus().response(payload, "你发了：" + payload.getContent());
            }
        };
    }

    //发送测试
    private static void sendTest() {
        //普通发送
        Dami.strBus().send(demo_topic, "{user:'noear'}");

        //请求并等响应
        String rst1 = Dami.strBus().requestAndResponse(demo_topic, "{user:'dami'}");
        System.out.println("响应返回: " + rst1);

        //请求并等回调
        Dami.strBus().requestAndCallback(demo_topic, "{user:'solon'}", (rst2) -> {
            System.out.println("响应回调: " + rst2);
        });
    }
}