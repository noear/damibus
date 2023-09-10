

### 示例代码

```java
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