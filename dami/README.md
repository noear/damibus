## dami 示例

更多详情，请查看 [src/test/java/features](src/test/java/features)

#### 依赖配置

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>dami</artifactId>
    <version>0.22</version>
</dependency>
```


#### demo21_send

```java
//泛型总线风格。<C,R>bus()
public class Deom11 {
    static String topic = "demo.hello";

    public static void main(String[] args) {
        //监听事件
        Dami.<String,Long>bus().listen(topic, payload -> {
            System.err.println(payload);
        });


        //发送事件
        Dami.<String,Long>bus().send(topic, "world");
    }
}
```

#### demo12_request

```java
//字符串总线风格。busStr() = <String,String>bus()
public class Demo12 {
    static String topic = "demo.hello";

    public static void main(String[] args) {
        //监听事件
        Dami.busStr().listen(topic, payload -> {
            System.err.println(payload);

            if (payload.isRequest()) {
                payload.reply("hi nihao!"); // sendAndResponse 只接收第一个
                payload.reply("* hi nihao!");
                payload.reply("** hi nihao!");
            }
        });


        //发送事件
        String rst1 = Dami.busStr().sendAndResponse(topic, "world");
        System.out.println(rst1);

        Dami.busStr().sendAndCallback(topic, "world", rst2 -> {
            System.out.println(rst2); //callback 可不限返回
        });
    }
}
```

#### demo31_api

使用 ioc 适配版本更简便，详情：[dami-solon-plugin](../dami-solon-plugin)、[dami-springboot-starter](../dami-springboot-starter)

```java
//接口风格
public interface UserEventSender {
    void onCreated(Long userId, String name);
    Long getUserId(String name);
}

//通过约定保持与 Sender 相同的接口定义（或者实现 UserEventSender 接口，但会带来依赖关系）
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
        //注册监听器
        UserEventListenerOfModule1 userEventListener = new UserEventListenerOfModule1();
        Dami.api().registerListener("demo.user", userEventListener);

        //生成发送器
        UserEventSender userEventSender = Dami.api().createSender("demo.user", UserEventSender.class);

        //发送测试
        userEventSender.onCreated(1L, "noear");
        Long userId = userEventSender.getUserId( "dami");
        System.err.println("收到：响应：userId：" + userId);

        //注销监听器
        Dami.api().unregisterListener("demo.user", userEventListener);
    }
}
```