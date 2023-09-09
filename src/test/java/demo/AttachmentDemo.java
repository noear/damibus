package demo;

import org.noear.dami.DamiBus;

public class AttachmentDemo {
    static String demo_topic = "demo.user.created";

    public static void main(String[] args) {
        //监听
        listen();

        //发送测试
        sendTest();
    }

    //监听
    private static void listen() {
        DamiBus.str().listen(demo_topic, 0, payload -> {
            //接收处理
            System.out.println(payload);

            //设置附件
            payload.setAttachment("name", "node1");
        });


        DamiBus.str().listen(demo_topic, 1, payload -> {
            //接收处理
            System.out.println(payload);

            //打印附件
            String name = payload.getAttachment("name");
            System.out.println(name);

            if (payload.isRequest()) {
                //如果是请求载体，再响应一下
                DamiBus.str().response(payload, "你发了：" + payload.getContent());
            }
        });
    }

    //发送测试
    private static void sendTest() {
        //普通发送
        DamiBus.str().send(demo_topic, "{user:'noear'}");

        //请求并等响应
        String rst1 = DamiBus.str().requestAndResponse(demo_topic, "{user:'dami'}");
        System.out.println("响应返回: " + rst1);

        //请求并等回调
        DamiBus.str().requestAndCallback(demo_topic, "{user:'solon'}", (rst2) -> {
            System.out.println("响应回调: " + rst2);
        });
    }
}