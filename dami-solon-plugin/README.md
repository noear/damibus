
### 添加依赖配置

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>dami-solon-plugin</artifactId>
    <version>0.19.1</version>
</dependency>
```

### 示例代码

* Api 风格

```java
@DamiTopic("demo.user")
public interface UserEventSender {
    long created(long userId, String name); //方法的主题= topicMapping + "." + method.getName() 
    void updated(long userId, String name); //方法名字，不能重复
}

//通过约定保持与 Sender 相同的接口定义（或者实现 UserEventSender 接口）
@DamiTopic("demo.user")
public class UserEventListenerImpl {
    public long created(long userId, String name) {
        System.err.println("created: userId=" + userId + ", name=" + name);
        return userId;
    }
    public void updated(long userId, String name) {
        System.err.println("updated: userId=" + userId + ", name=" + name);
    }
}

@Component
public class ApiStyleDemo {
    @Inject
    UserEventSender userEventSender;

    @Init
    public void test(){
        //发送测试
        long rst = userEventSender.created(1, "noear");
        System.out.println("收到返回：" + rst);
        userEventSender.updated(2, "dami");
    }

    public static void main(String[] args) {
        Solon.start(ApiStyleDemo.class, args);
    }
}
```

* Bus 风格（将上面 UserEventSender::created 的发送，还原为 Bus 风格）

```java
//事件模式
@DamiTopic("demo.user.created")
public class UserCreatedTopicListender implements TopicListener<Payload<Map<String, Object>, Long>> {

    @Override
    public void onEvent(Payload<Map<String, Object>, Long> payload) throws Throwable {
        System.out.println(payload);

        Long userId = (Long) payload.getContent().get("userId");
        System.out.println("订阅：UserCreatedTopicListender:userId=" + userId);

        if(payload.isRequest()){
            Dami.<Map<String, Object>, Long>bus().response(payload, userId);
        }
    }
}

@Component
public class BusStyleDemo {
    @Inject
    UserEventSender userEventSender;

    @Init
    public void test() {
        //发送测试
        Map<String, Object> content = new LinkedHashMap<>();
        content.put("userId", 1);
        content.put("name", "noear");
        long rst = Dami.<Map<String, Object>, Long>bus().requestAndResponse("demo.user.created", content);
        System.out.println("收到返回：" + rst);
    }

    public static void main(String[] args) {
        Solon.start(ApiStyleDemo.class, args);
    }
}
```


### 如果需要拦截？

```java

//定义拦截器（可选）
@Component
public class ApiInterceptor implements Interceptor {
    @Override
    public void doIntercept(Payload payload, InterceptorChain chain) {
        System.out.println("拦截：" + payload.toString());
        chain.doIntercept(payload);
    }
}
```