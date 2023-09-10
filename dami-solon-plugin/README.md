
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
//定义拦截器（可选）
@Component
public class ApiInterceptor implements Interceptor {
    @Override
    public void doIntercept(Payload payload, InterceptorChain chain) {
        System.out.println("拦截：" + payload.toString());
        chain.doIntercept(payload);
    }
}

@Dami(topicMapping = "demo.user")
public interface UserEventSender {
    long created(long userId, String name); //方法的主题= topicMapping + "." + method.getName() 
    void updated(long userId, String name); //方法名字，不能重复
}

//通过约定保持与 Sender 相同的接口定义（或者实现 UserEventSender 接口）
@Dami(topicMapping = "demo.user")
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

* Bus 风格

```java
//事件模式
@Dami(topicMapping = "demo.user.created")
public class UserCreatedTopicListender implements TopicListener<Payload<Map<String,Object>,Long>> {

    @Override
    public void onEvent(Payload<Map<String, Object>, Long> payload) throws Throwable {
        System.out.println(payload);
        System.out.println("订阅：UserCreatedTopicListender:userId=" + payload.getContent().get("userId"));
    }
}
```