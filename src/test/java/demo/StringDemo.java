package demo;

import org.noear.dami.DamiBus;
import org.noear.dami.impl.Payload;
import org.noear.dami.TopicListener;

/**
 * 不需要判断发送和接受类型，直接用string就行
 *
 * @author noear
 * @since 1.0
 */
public class StringDemo {
    static String demo_topic = "demo.user.created";

    public static void main(String[] args) {
        TopicListener<Payload<String, String>> listener = createListener();

        //监听
        DamiBus.<String, String>global().listen(demo_topic, listener);

        //发送测试
        sendTest();

        //取消监听
        DamiBus.<String, String>global().unlisten(demo_topic, listener);
    }

    //创建监听器
    private static TopicListener<Payload<String, String>> createListener() {
        return payload -> {
            //接收处理
            System.out.println(payload);

            if (payload.isRequest()) {
                //如果是请求载体，再响应一下
                DamiBus.<String, String>global().response(payload, "你发了：" + payload.getContent());
            }
        };
    }

    //发送测试
    private static void sendTest() {
        //普通发送
        DamiBus.global().send(demo_topic, "{user:'noear'}");

        //请求并等响应
        String rst1 = DamiBus.<String, String>global().requestAndResponse(demo_topic, "{user:'dami'}");
        System.out.println("响应返回: " + rst1);

        //请求并等回调
        DamiBus.global().requestAndCallback(demo_topic, "{user:'solon'}", (rst2) -> {
            System.out.println("响应回调: " + rst2);
        });
    }
}