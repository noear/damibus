## dami 示例

更多详情，请查看 [src/test/java/features](src/test/java/features)

#### 依赖配置

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>dami</artifactId>
    <version>1.0.6</version>
</dependency>
```

#### demo21_send

```java
//总线风格。bus()
public class Deom11 {
    static String topic = "demo.hello";

    public static void main(String[] args) {
        //监听事件
        Dami.bus().listen(topic, payload -> {
            System.err.println(payload); //可以有多个订阅
        });
        Dami.bus().listen(topic, payload -> {
            CompletableFuture.runAsync(()-> { //也可以异步消费
                System.err.println(payload);
            });
        });


        //发送事件
        Dami.bus().send(topic, "world");
    }
}
```

#### demo12_request

```java
//泛型总线风格。<C,R>bus()
public class Demo12 {
    static String topic = "demo.hello";

    public static void main(String[] args) {
        //监听事件
        Dami.<String,String>bus().listen(topic, payload -> {
            System.err.println(payload);

            if (payload.isRequest() || payload.isSubscribe()) {
                payload.reply("hi!"); // sendAndRequest 只接收第一个
                payload.reply("* hi nihao!");
                payload.reply("** hi nihao!");
            }
        });


        //发送事件
        String rst1 = Dami.<String,String>bus().sendAndRequest(topic, "world"); //要求有返回值
        System.out.println(rst1);

        Dami.<String,String>bus().sendAndSubscribe(topic, "world", rst2 -> {
            System.out.println(rst2); //subscribe 不限回调次数
        });
    }
}
```

#### demo31_api

使用 ioc 适配版本更简便，详情：[dami-solon-plugin](dami-solon-plugin)、[dami-springboot-starter](dami-springboot-starter)

```java
//接口风格
public interface EventUser {
    void onCreated(Long userId, String name);
    Long getUserId(String name);
}

//通过约定保持与 Sender 相同的接口定义（或者实现 UserEventSender 接口，但会带来依赖关系）
public class EventUserListener1 { // implements EventUser
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
        EventUserListener1 userEventListener = new EventUserListener1();
        api.registerListener(topicMapping, userEventListener);

        //生成发送器
        EventUser eventUser = api.createSender(topicMapping, EventUser.class);

        //发送测试
        eventUser.onCreated(1L, "noear");
        Long userId = eventUser.getUserId("dami");
        System.err.println("收到：响应：userId：" + userId);

        //注销监听器
        api.unregisterListener(topicMapping, userEventListener);
    }
}
```

### demo15_custom

定制能力，切换为模式匹配路由器


```java
public class Demo15_path {
    public void main(){
        //切换为模式匹配路由器（支持 * 和 ** 占位符；支持 / 或 . 做为间隔）
        DamiConfig.configure(new TopicRouterPatterned(RoutingPath::new));

        //拦截
        Dami.bus().listen("demo/a/*", (payload) -> {
            System.err.println(payload);
        });

        //发送事件
        Dami.bus().send("demo/a/1", "world1");
        Dami.bus().send("demo/a/2", "world2");
    }
}
```

```java
public class Demo15_tag {
    public void main(){
        //切换为模式匹配路由器（支持 * 和 ** 占位符；支持 / 或 . 做为间隔）
        DamiConfig.configure(new TopicRouterPatterned(RoutingTag::new));

        //拦截
        Dami.bus().listen("demo.a:id", (payload) -> {
            System.err.println(payload);
        });

        //发送事件
        Dami.bus().send("demo.a:id", "world1");
        Dami.bus().send("demo.a:id,name", "world2");
    }
}
```