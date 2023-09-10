### 1、添加依赖配置

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>dami</artifactId>
    <version>0.19.3</version>
</dependency>
```

如果涉及类加载器隔离：请在主程序标为编译，在其它模块标为可选。

### 2、弱类型总线风格示例

适合类隔离的场景

```java
public class BusStringStyleDemo {
    static String demo_topic = "demo.user.created";

    public static void main(String[] args) {
        TopicListener<Payload<String, String>> listener = createListener();

        //监听
        Dami.busStr().listen(demo_topic, listener);

        //发送测试
        sendTest();

        //取消监听
        Dami.busStr().unlisten(demo_topic, listener);
    }

    //创建监听器
    private static TopicListener<Payload<String, String>> createListener() {
        return payload -> {
            //接收处理
            System.out.println(payload);

            if (payload.isRequest()) {
                //如果是请求载体，再响应一下
                Dami.busStr().response(payload, "你发了：" + payload.getContent());
            }
        };
    }

    //发送测试
    private static void sendTest() {
        //普通发送
        Dami.busStr().send(demo_topic, "{user:'noear'}");

        //请求并等响应
        String rst1 = Dami.busStr().requestAndResponse(demo_topic, "{user:'dami'}");
        System.out.println("响应返回: " + rst1);

        //请求并等回调
        Dami.busStr().requestAndCallback(demo_topic, "{user:'solon'}", (rst2) -> {
            System.out.println("响应回调: " + rst2);
        });
    }
}
```

### 4、泛型、强类型总线风格示例


```java
public class BusStyleDemo {
    static String demo_topic = "demo.user.info";

    public static void main(String[] args) {
        TopicListener<Payload<User, User>> listener = createListener();

        //监听
        Dami.<User, User>bus().listen(demo_topic, listener);

        //发送测试
        sendTest();

        //取消监听
        Dami.<User, User>bus().unlisten(demo_topic, listener);
    }

    //创建监听器
    private static TopicListener<Payload<User, User>> createListener() {
        return payload -> {
            //接收处理
            System.out.println(payload);

            if (payload.isRequest()) {
                final User content = payload.getContent().sing("你太美");
                //如果是请求载体，再响应一下
                Dami.<User, User>bus().response(payload, content);
            }
        };
    }

    //发送测试
    private static void sendTest() {
        final User user = new User().name("kk").age(2.5).hobby(new String[]{"唱", "跳", "rap", "打篮球"});
        //普通发送
        Dami.<User, Void>bus().send(demo_topic, user);

        //普通发送,自定义构建参数
        Dami.<User, Void>bus().send(new Payload<>("123", demo_topic, user));

        //请求并等响应
        User rst1 = Dami.<User, User>bus().requestAndResponse(demo_topic, user);
        System.out.println("响应返回: " + rst1);

        user.sing("ai kun");
        //请求并等回调
        Dami.<User, User>bus().requestAndCallback(demo_topic, user, rst2 -> {
            System.out.println("响应回调: " + rst2);
        });
    }
}
```