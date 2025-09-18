
## dami2-springboot-starter 示例 （支持 v2.x 和 v3.x）

更多详情，请查看 [src/test/java/features](src/test/java/features)

#### 依赖配置

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>dami2-springboot-starter</artifactId>
    <version>2.0.0-M1</version>
</dependency>
```

#### demo91_springboot （注解）

```java
@EnableAutoConfiguration
@SpringBootTest(classes = Demo91.class)
@ComponentScan("features.demo91_springboot")
public class Demo91 {
    @Test
    public void main() throws Exception {
        System.out.println(Dami.bus().call("user.demo", "solon")
                .get());
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

#### demo92_springboot （lpc 无依赖实现效果）

```java
@DamiTopic("demo92.event.user")
public interface EventUserService {
    User getUser(long userId); //方法的主题 = topicMapping + "." + method.getName() //方法不能重名
}

//通过约定保持与 EventUserService 相同的接口定义（或者实现 EventUserService 接口，这个会带来依赖关系）
@DamiTopic("demo92.event.user")
public class EventUserServiceImpl { // implements EventUserService // 它相当于是个实现类
    public User getUser(long userId) {
        return new User(userId);
    }
}

@EnableAutoConfiguration
@SpringBootTest(classes = Demo92.class)
@ComponentScan("features.demo92_springboot")
public class Demo92 {
    @Autowired
    EventUserService eventUserService;

    @Test
    public void main(){
        User user = eventUserService.getUser(99);
        assert user.getUserId() == 99;
    }
}
```


#### demo93_springboot （无依赖实现效果）

```java
@DamiTopic("demo93.event.user")
public interface EventUserNotices {
    void onCreated(long userId, String name);
}

@DamiTopic("demo93.event.user")
public class EventUserNoticesListener {
    public void onCreated(long userId, String name) {
        System.err.println("1-onCreated: userId=" +userId);
    }
}

@DamiTopic(value = "demo93.event.user", index = 2) //可以控制监听顺序
public class EventUserNoticesListener2 { // implements EventUserNotices
    public void onCreated(long userId, String name) {
        System.err.println("2-onCreated: userId=" +userId);
    }
}

@EnableAutoConfiguration
@SpringBootTest(classes = Demo93.class)
@ComponentScan("features.demo93_springboot")
public class Demo93 {
    @Autowired
    EventUserNotices eventUserNotices;

    @Test
    public void main(){
        eventUserNotices.onCreated(99, "noear");
    }
}
```

