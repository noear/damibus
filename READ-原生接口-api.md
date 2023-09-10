### 1、添加依赖配置

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>dami</artifactId>
    <version>0.19.3</version>
</dependency>
```

如果涉及类加载器隔离：请在主程序标为编译，在其它模块标为可选。

### 2、接口风格示例

配合自定义编码器，可适用于任何场景。如果是类隔离的场景，可以：

* a) 发送器 与 监听器，不要有依赖关系；使用基础类型做为参数或返回
* b) 发送器 与 监听器，不要有依赖关系；使用自定义编码解（比如 json 序列化），可支持任何类型的参数或返回


```java
public interface UserEventSender {
    long created(long userId, String name); //方法的主题= topicMapping + "." + method.getName() 
    void updated(long userId, String name); //方法名字，不能重复
}

//通过约定保持与 Sender 相同的接口定义（或者实现 UserEventSender 接口）
public class UserEventListenerImpl {
    public long created(long userId, String name) {
        System.err.println("created: userId=" + userId + ", name=" + name);
        return userId;
    }
    public void updated(long userId, String name) {
        System.err.println("updated: userId=" + userId + ", name=" + name);
    }
}

public class ApiStyleDemo {
    public static void main(String[] args) {
        //设定编码器
        //Dami.api().setCoder(new CoderDefault());

        //添加拦截器
        Dami.intercept((payload, chain) -> {
            System.out.println("拦截：" + payload.toString());
            chain.doIntercept(payload);
        });
        
        UserEventListenerImpl userEventListener = new UserEventListenerImpl();
        //注册监听器
        Dami.api().registerListener("demo.user", userEventListener);

        //创建发送器
        UserEventSender userEventSender = Dami.api().createSender("demo.user", UserEventSender.class);

        //发送测试
        long rst = userEventSender.created(1, "noear");
        System.out.println("收到返回：" + rst);
        userEventSender.updated(2, "dami");

        //注销监听器
        Dami.api().unregisterListener("demo.user", userEventListener);
    }
}
```