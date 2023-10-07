
## 1、Mami，主类


```java
public class Dami {
    static final DamiBus bus = new DamiBusImpl<>();
    static final DamiApi api = new DamiApiImpl(Dami::bus);
    //泛型、强类型总线界面
    public static <C, R> DamiBus<C, R> bus() { return bus; }
    //接口界面
    public static DamiApi api() { return api; }
    //拦截
    public static void intercept(Interceptor interceptor) { intercept(0, interceptor); }
    //拦截
    public static void intercept(int index, Interceptor interceptor) { bus.intercept(index, interceptor);}
}
```

## 2、Dami，配置类

```java
public class DamiConfig {
    //配置总线的主体路由器
    public static void configure(TopicRouter topicRouter);
    //配置总线的负载工厂
    public static void configure(PayloadFactory payloadFactory);
    //配置总线的响应超时
    public static void configure(long timeout);

    //配置接口模式的编解码器
    public static void configure(Coder coder);

    //配置总线实例
    public static void configure(DamiBus bus);
    //配置接口实例
    public static void configure(DamiApi api);
}
```

## 3、DamiBus<C, R>，总线模式接口


```java
public interface DamiBus<C, R> {
    //获取超时
    long timeout();
    //拦截
    void intercept(int index, Interceptor interceptor);
    
    //发送（不需要答复）=> 返回是否有订阅处理
    boolean send(final String topic, final C content);
    //发送并等待响应 => 返回响应结果（没有订阅处理，会异常）
    R sendAndResponse(final String topic, final C content);
    //发送并等待回调 => 返回是否有订阅处理
    boolean sendAndCallback(final String topic, final C content, final Consumer<R> callback);
    
    //监听
    default void listen(final String topic, final TopicListener<Payload<C, R>> listener) { listen(topic, 0, listener); }
    //监听
    void listen(final String topic, final int index, final TopicListener<Payload<C, R>> listener);
    //取消监听
    void unlisten(final String topic, final TopicListener<Payload<C, R>> listener);
}
```


## 4、DamiApi，接口模式接口


```java
public interface DamiApi {
    //获取编码器
    Coder coder();
    //获取关联总线
    DamiBus bus();
    
    //创建发送器代理
    <T> T createSender(String topicMapping, Class<T> senderClz);
    
    //注册监听者实例（一个监听类，只能监听一个主题）
    default void registerListener(String topicMapping, Object listenerObj) { registerListener(topicMapping, 0, listenerObj); }
    //注册监听者实例（一个监听类，只能监听一个主题）
    void registerListener(String topicMapping, int index, Object listenerObj);
    //取消注册监听者实例
    void unregisterListener(String topicMapping, Object listenerObj);
}
```

DamiApi::createSender，发送者接口代理情况说明

| 用例               | 对应总线接口                   | 说明               |
|------------------|--------------------------|------------------|
| void onCreated() | 返回为空的，send 发送            | 没有监听，不会异常        |
| User getUser()   | 返回类型的，sendAndResponse 发送 | 没有监听，会异常。且必须要有答复 |


## 5、Payload<C, R>，事件负载接口


```java
public interface Payload<C, R> extends Serializable {
    //获取附件
    <T> T getAttachment(String key);
    //设置附件
    <T> void setAttachment(String key, T value);
    //设置处理标识
    void setHandled();
    //获取处理标识
    boolean getHandled();

    //是否为请求（是的话，需要答复）
    boolean isRequest();
    //答复
    boolean reply(final R content);

    //唯一标识
    String getGuid();
    //主题
    String getTopic();
    //内容
    C getContent();
}

```

Payload::reply，答复情况说明

| 发送接口              | 答复说明            | 备注                          |
|-------------------|-----------------|-----------------------------|
| send()            | 会出异常，提示不支持答复    | payload.isRequest() = false |
| sendAndResponse() | 第一条答复有效，且必须要有答复 | payload.isRequest() = true  |
| sendAndCallback() | 可以无限次答复有效       | payload.isRequest() = true  |


## 6、TopicListener<Event>，主题监听接口

```java
public interface TopicListener<Event> {
    //处理监听事件
    void onEvent(final Event event) throws Throwable;
}
```

