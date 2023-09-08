# dami
Local Procedure Call

为多插件（或模块）之间解耦而设计。


原始需求来自 hotplug 的应用需要：在 classloader 隔离下，现有的 EventBus 不能满足需求（因为强类型，会形成 clas 依赖）

demo

```java
public class DemoApp {
    static String demo_topic = "demo.user.created";

    public static void main(String[] args) throws Throwable {
        listen();
        send();
    }

    //监听
    private static void listen() {
        DamiBus.global().listen(demo_topic, payload -> {
            System.out.println(payload);
            //接收
            if (payload.isRequest()) {
                //如果是请求载体，则响应一下
                DamiBus.global().response(payload, "你发了：" + payload.getContent());
            }
        });
    }

    //发送
    private static void send() {
        //普通发送
        DamiBus.global().send(demo_topic, "{user:'noear'}");


        //请求并等响应
        String rst1 = DamiBus.global().requestAndResponse(demo_topic, "{user:'dami'}");
        System.out.println("响应返回: " + rst1);

        //请求并等回调
        DamiBus.global().requestAndCallback(demo_topic, "{user:'solon'}", (rst2) -> {
            System.out.println("响应回调: " + rst2);
        });

    }
}
```
