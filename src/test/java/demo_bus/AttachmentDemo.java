package demo_bus;

import org.noear.dami.Dami;

public class AttachmentDemo {
    static String demo_topic = "demo.user.created";

    public static void main(String[] args) {
        //添加拦截器
        Dami.intercept((payload, chain) -> {
            System.out.println("拦截：" + payload.toString());
            chain.doIntercept(payload);
        });

        //监听
        listen();

        //发送测试
        sendTest();
    }

    //监听
    private static void listen() {
        Dami.strBus().listen(demo_topic, 0, payload -> {
            //接收处理
            System.out.println(payload);

            //设置附件
            payload.setAttachment("name", "node1");
        });


        Dami.strBus().listen(demo_topic, 1, payload -> {
            //接收处理
            System.out.println(payload);

            //打印附件
            String name = payload.getAttachment("name");
            System.out.println(name);

            if (payload.isRequest()) {
                //如果是请求载体，再响应一下
                Dami.strBus().response(payload, "你发了：" + payload.getContent());
            }
        });
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