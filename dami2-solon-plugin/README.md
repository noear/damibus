
## dami2-solon-plugin 示例

更多详情，请查看 [src/test/java/features](src/test/java/features)

#### 依赖配置

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>dami2-solon-plugin</artifactId>
    <version>2.0.0-M1</version>
</dependency>
```

如果涉及类加载器隔离：请在主程序标为编译，在其它模块标为可选。

#### demo81_solon （注解）

```java
@SolonTest
public class Demo81 {
    @Test
    public void main() throws Throwable {
        System.out.println(Dami.bus().call("user.demo", "solon").get());
    }

    @DamiTopic("user.demo")
    public static class UserEventListener implements CallTopicListener<String, String> {
        @Override
        public void onCall(Event<CallPayload<String, String>> event, String data, CompletableFuture<String> data) {
            data.complete("Hi " + data);
        }
    }
}
```


#### demo82_solon （无依赖实现效果）

```java
@DamiTopic("demo82.event.user")
public interface EventUserService {
    User getUser(long userId); //方法的主题 = topicMapping + "." + method.getName() //方法不能重名
}

//通过约定保持与 EventUserService 相同的接口定义（或者实现 EventUserService 接口，这个会带来依赖关系）
@DamiTopic("demo82.event.user")
public class EventUserServiceImpl { // implements EventUserService // 它相当于是个实现类
    public User getUser(long userId) {
        return new User(userId);
    }
}

@SolonTest
public class Demo82 {
    @Inject
    EventUserService eventUserService;

    @Test
    public void main(){
        User user = eventUserService.getUser(99);
        assert user.getUserId() == 99;
    }
}
```


#### demo83_solon （广播效果）

```java
@DamiTopic("demo83.event.user")
public interface EventUserNotices {
    void onCreated(long userId, String name);
}

@DamiTopic("demo83.event.user")
public class EventUserNoticesListener {
    public void onCreated(long userId, String name) {
        System.err.println("1-onCreated: userId=" +userId);
    }
}


@DamiTopic(value = "demo83.event.user", index = 2) //可以控制监听顺序
public class EventUserNoticesListener2 { // implements EventUserNotices
    public void onCreated(long userId, String name) {
        System.err.println("2-onCreated: userId=" +userId);
    }
}


@SolonTest
public class Demo83 {
    @Inject
    EventUserNotices eventUserNotices;

    @Test
    public void main() {
        eventUserNotices.onCreated(99, "noear");
    }
}
```

