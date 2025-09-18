## dami 示例

更多详情，请查看 [src/test/java/features](src/test/java/features)

#### 依赖配置

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>dami</artifactId>
    <version>2.0.0-SNAPSHOT</version>
</dependency>
```

#### demo21_send

```java
public class Deom11 {
    static String topic = "demo.hello";

    public static void main(String[] args) {
        //监听事件
        Dami.bus().listen(topic, event -> {
            System.err.println(event.getPayload()); //可以有多个订阅
        });
        Dami.bus().listen(topic, event -> {
            CompletableFuture.runAsync(()-> { //也可以异步消费
                System.err.println(event.getPayload());
            });
        });


        //发送事件
        Dami.bus().send(topic, "world");
    }
}
```


#### demo12_call

```java
public class Demo12 {
    static String topic = "demo.hello";

    public static void main(String[] args) {
        //监听调用事件
        Dami.bus().<String, String>onCall(topic, (event, sink) -> {
            System.err.println(event);

            sink.complete("hi!");
        });


        //发送调用事件
        String rst1 = Dami.bus().<String, String>call(topic, "world").get();
        //发送事件//支持应急处理（或降级处理）（没有订阅时触发时）
        //String rst1 = Dami.bus().<String, String>call(topic, "world", () -> "def").get();
        System.out.println(rst1);
    }
}
```

### demo13_stream

```java
public class Demo13 {
    static String topic = "demo.hello";

    public static void main(String[] args) {
        //监听流事件
        Dami.bus().<String, String>onStream(topic, (event, sink) -> {
            System.err.println(event);

            sink.onNext("hello");
        });

        System.out.println(Thread.currentThread());

        //发送流事件
        Dami.bus().<String, String>stream(topic, "world").subscribe(new SimpleSubscriber<>()
                .doOnNext(item -> {
                    System.out.println(item);
                }));
    }
}
```


#### demo31_api

使用 ioc 适配版本更简便，详情：[dami-solon-plugin](dami-solon-plugin)、[dami-springboot-starter](dami-springboot-starter)

```java
//服务消费者接口
public interface UserService {
    void onCreated(Long userId, String name);

    Long getUserId(String name);
}

//通过约定保持与 UserService 相同的接口定义（或者实现 UserService 接口，但会带来依赖关系）
public class UserServiceImpl { // implements UserService
    public void onCreated(Long userId, String name) {
        System.err.println("onCreated: userId=" + userId + ", name=" + name);
    }

    public Long getUserId(String name) {
        return Long.valueOf(name.hashCode());
    }
}

public class Demo31 {
    public static void main(String[] args) {
        //注册服务实现
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        Dami.lpc().registerService(topicMapping, userServiceImpl);

        //创建服务消费者（接口代理）
        UserService userService = Dami.lpc().createConsumer(topicMapping, UserService.class);

        //发送测试
        userService.onCreated(1L, "noear");
        Long userId = userService.getUserId("dami");
        System.err.println("收到：响应：userId：" + userId);

        //注销服务实现
        Dami.lpc().unregisterService(topicMapping, userServiceImpl);
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
        Dami.bus().listen("demo/a/*", (event) -> {
            System.err.println(event);
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
        Dami.bus().listen("demo.a:id", (event) -> {
            System.err.println(event);
        });

        //发送事件
        Dami.bus().send("demo.a:id", "world1");
        Dami.bus().send("demo.a:id,name", "world2");
    }
}
```