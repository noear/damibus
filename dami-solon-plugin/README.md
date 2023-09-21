
## dami-solon-plugin 示例

更多详情，请查看 [src/test/java/features](src/test/java/features)

#### 依赖配置

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>dami-solon-plugin</artifactId>
    <version>0.27</version>
</dependency>
```

如果涉及类加载器隔离：请在主程序标为编译，在其它模块标为可选。

#### demo81_solon （无依赖实现效果）

```java
@DamiTopic("event.user")
public interface EventUserService {
    User getUser(long userId); //方法的主题 = topicMapping + "." + method.getName() //方法不能重名
}

//通过约定保持与 EventUserService 相同的接口定义（或者实现 EventUserService 接口，这个会带来依赖关系）
@DamiTopic("event.user")
public class EventUserServiceListener { //它相当于是个实现类
    public User getUser(long userId) {
        return new User(userId);
    }
}

@ExtendWith(SolonJUnit5Extension.class)
public class Demo81 {
    @Inject
    EventUserService eventUserService;

    @Test
    public void main(){
        User user = eventUserService.getUser(99);
        assert user.getUserId() == 99;
    }
}
```


#### demo82_solon （广播效果）

```java
@DamiTopic("demo82.event.user")
public interface EventUserNotices {
    void onCreated(long userId, String name);
}

@DamiTopic("demo82.event.user")
public class EventUserNoticesListener {
    public void onCreated(long userId, String name) {
        System.err.println("1-onCreated: userId=" +userId);
    }
}

@DamiTopic("demo82.event.user")
public class EventUserNoticesListener2 {

    public void onCreated(long userId, String name) {
        System.err.println("2-onCreated: userId=" +userId);
    }

}

@ExtendWith(SolonJUnit5Extension.class)
public class Demo82 {
    @Inject
    EventUserNotices eventUserNotices;

    @Test
    public void main() {
        eventUserNotices.onCreated(82, "noear");
    }
}
```

