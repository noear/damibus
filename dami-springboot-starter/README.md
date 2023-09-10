
## 1、dami-solon-plugin 示例

#### 依赖配置

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>dami-solon-plugin</artifactId>
    <artifactId>dami-springboot-starter</artifactId>
</dependency>
```

#### 代码

```java
@DamiTopic("demo.user")
public interface UserEventSender {
    void onCreated(long userId, String name); //方法的主题 = topicMapping + "." + method.getName() 

    void onUpdated(long userId, String name); //方法名字，不能重复
}

@DamiTopic("demo.user")
public class UserDemandListener {
    public User getUser(long userId) {
        return new User(userId);
    }
}

//通过约定保持与 Sender 相同的接口定义（或者实现 UserEventSender 接口）
@DamiTopic("demo.user")
public class UserEventListenerOfLive {
    public void onCreated(long userId, String name) {
        System.err.println("LIve:User:onCreated: userId=" + userId + ", name=" + name);
    }

    public void onUpdated(long userId, String name) {
        System.err.println("LIve:User:onUpdated: userId=" + userId + ", name=" + name);
    }
}

@DamiTopic("demo.user")
public interface UserDemandSender {
    User getUser(long userId);
}

@Component
public class Demo92 {
    @Autowired
    UserEventSender userEventSender;

    @Autowired
    UserDemandSender userDemandSender;

    @Autowired
    public void test() {
        userEventSender.onCreated(1,"noear");
        User user = userDemandSender.getUser(1);
    }

    public static void main(String[] args) {
        Solon.start(ApiStyleDemo.class, args);
    }
}
```

#### 如果需要拦截？

```java
//定义拦截器（可选）
@Component
public class DamiInterceptorImpl implements Interceptor {
    @Override
    public void doIntercept(Payload payload, InterceptorChain chain) {
        System.out.println("拦截：" + payload.toString());
        chain.doIntercept(payload);
    }
}
```
