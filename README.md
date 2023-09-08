# Dami (Local Procedure Call)

本地过程调用框架，为本地多模块之间通讯解耦而设计。可适于任何 java 开发环境，零依赖，仅五个类。（有可能会推出多语言版本）


示例：

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
