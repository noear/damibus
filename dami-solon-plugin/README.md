
## dami-solon-plugin 示例

更多详情，请查看 [src/test/java/features](src/test/java/features)

#### 依赖配置

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>dami-solon-plugin</artifactId>
    <version>0.22</version>
</dependency>
```

如果涉及类加载器隔离：请在主程序标为编译，在其它模块标为可选。

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

//通过约定保持与 Sender 相同的接口定义（或者实现 UserEventSender 接口，这个会带来依赖关系）
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

@SolonMain
@Component
public class Demo82 {
    @Inject
    UserEventSender userEventSender;

    @Inject
    UserDemandSender userDemandSender;

    @Init
    public void test() {
        userEventSender.onCreated(1,"noear");
        User user = userDemandSender.getUser(1);
    }

    public static void main(String[] args) {
        Solon.start(Demo82.class, args);
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
