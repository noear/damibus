
## 1、Mami，操作主类


```java
public class Dami {
    static DamiBus bus = new DamiBusImpl();
    static DamiLpc lpc = new DamiLpcImpl(Dami::bus);

    //总线界面
    public static DamiBus bus() {
        return bus;
    }

    //接口界面
    public static DamiLpc lpc() {
        return lpc;
    }
}
```

## 2、Dami，配置操作类

```java
public class DamiConfig {
    //配置总线的主题路由器
    public static void configure(TopicRouter topicRouter);
    //配置总线的主题调度器
    public static void configure(TopicDispatcher topicDispatcher);
    //配置总线的负载工厂
    public static void configure(EventFactory eventFactory);

    //配置接口模式的编解码器
    public static void configure(Coder coder);

    //配置总线实例
    public static void configure(DamiBus bus);
    //配置接口实例
    public static void configure(DamiLpc lpc);
}
```

## 3、DamiBus<P>，总线模式接口


```java
public interface DamiBus {
    //拦截
    <P> void intercept(int index, Interceptor<P> interceptor);
    <P> void intercept(Interceptor<P> interceptor);
    
    //发送（不需要答复）=> 返回是否有订阅处理
    <P> Result<P> send(final String topic, final P payload);
    <P> Result<P> send(final String topic, final P payload, Consumer<P> fallback);
   
    //监听
    <P> void listen(final String topic, final TopicListener<P> listener);
    //监听
    <P> void listen(final String topic, final int index, final TopicListener<P> listener);
    //取消监听
    <P> void unlisten(final String topic, final TopicListener<P> listener);
    void unlisten(final String topic);

    //路由器
    TopicRouter router();
}
```


## 4、DamiLpc，接口模式接口


```java
public interface DamiLpc {
    //获取编码器
    Coder coder();
    
    //创建服务消费者（接口代理）
    <T> T createConsumer(String topicMapping, Class<T> consumerApi);
    
    //注册服务实现（一个服务，只能监听一个主题）
    void registerService(String topicMapping, Object serviceObj);
    void registerService(String topicMapping, int index, Object serviceObj);
    //注销服务实现
    void unregisterService(String topicMapping, Object serviceObj);
}
```

DamiApi::createSender，发送者接口代理情况说明

| 用例               | 对应总线接口                   | 说明               |
|------------------|--------------------------|------------------|
| void onCreated() | 返回为空的，send 发送            | 没有监听，不会异常        |
| User getUser()   | 返回类型的，call 发送 | 没有监听，会异常。且必须要有答复 |


## 5、Event<P>，事件负载接口


```java
public interface Event<P> extends Serializable {
    //获取附件
    <T> T getAttachment(String key);
    //设置附件
    <T> void setAttachment(String key, T value);
    
    //设置处理标识（如果有监听，会标为已处理）
    void setHandled();
    //获取处理标识
    boolean getHandled();

    //主题
    String getTopic();
    //荷载
    P getPayload();
}

```

## 6、TopicListener<Event>，主题监听接口

```java
public interface TopicListener<P> {
    //处理监听事件
    void onEvent(final Event<P> event) throws Throwable;
}
```