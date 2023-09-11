## dami 示例

更多示例，请查看 [src/test/java](src/test/java)

#### 依赖配置

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>dami</artifactId>
    <version>0.20.1</version>
</dependency>
```

#### demo11_send

```java
public class Deom11 {
    static String topic = "demo.hello";

    public static void main(String[] args) {
        //监听事件
        Dami.busStr().listen(topic, payload -> {
            System.err.println(payload);
        });


        //发送事件
        Dami.busStr().send(topic, "world");
    }
}
```

#### demo12_request

```java
public class Demo12 {
    static String topic = "demo.hello";

    public static void main(String[] args) {
        //监听事件
        Dami.busStr().listen(topic, payload -> {
            System.err.println(payload);

            if (payload.isRequest()) {
                Dami.busStr().response(payload, "hi nihao!");
                Dami.busStr().response(payload, "* hi nihao!");
                Dami.busStr().response(payload, "** hi nihao!");
            }
        });


        //发送事件
        String rst1 = Dami.busStr().requestAndResponse(topic, "world");
        System.out.println(rst1);

        Dami.busStr().requestAndCallback(topic, "world", rst2 -> {
            System.out.println(rst2); //callback 可不限返回
        });
    }
}
```

#### demo22_request

```java
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
```

#### demo31_api

```java
public interface UserEventSender {
    void onCreated(Long userId, String name);
    Long getUserId(String name);
}

public class UserEventListenerImpl {
    public void onCreated(Long userId, String name) {
        System.err.println("onCreated: userId=" + userId + ", name=" + name);
    }

    public Long getUserId(String name) {
        return Long.valueOf(name.hashCode());
    }
}

public class Demo31 {
    public static void main(String[] args) {
        UserEventListenerOfModule1 userEventListener = new UserEventListenerOfModule1();
        UserEventSender userEventSender = Dami.api().createSender("demo.user", UserEventSender.class);

        //注册监听器
        Dami.api().registerListener("demo.user", userEventListener);

        //发送测试
        userEventSender.onCreated(1L, "noear");
        Long userId = userEventSender.getUserId( "dami");
        System.err.println("收到：响应：userId：" + userId);

        //注销监听器
        Dami.api().unregisterListener("demo.user", userEventListener);
    }
}
```