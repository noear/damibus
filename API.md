
## 1、Mami，入口主类


```java
public class Dami {
    static final DamiBus bus = new DamiBusImpl<>();
    static final DamiApi api = new DamiApiImpl(bus);
    //泛型、强类型总线界面
    public static <C, R> DamiBus<C, R> bus() { return bus; }
    //弱类型总线界面（适合类隔离的场景）
    public static DamiBus<String, String> busStr() { return bus; }
    //接口界面
    public static DamiApi api() { return api; }
    //拦截
    public static void intercept(Interceptor interceptor) { intercept(0, interceptor); }
    //拦截
    public static void intercept(int index, Interceptor interceptor) { bus.intercept(index, interceptor);}
}
```

## 2、DamiBus<C, R>，总线模式接口


```java
public interface DamiBus<C, R> {
    //获取超时
    long getTimeout();
    //设置超时
    void setTimeout(final long timeout);
    //拦截
    void intercept(int index, Interceptor interceptor);
    
    //发送（不需要答复）
    default void send(final String topic, final C content) { send(new Payload<>(topic, content)); }
    //发送（不需要答复）,自定义载体
    void send(final Payload<C, R> payload);
    //发送并等待响应
    default R sendAndResponse(final String topic, final C content) { return sendAndResponse(new Payload<>(topic, content)); }
    //发送并等待响应,自定义载体
    R sendAndResponse(final Payload<C, R> payload);
    //发送并等待回调
    default void sendAndCallback(final String topic, final C content, final Consumer<R> callback) { sendAndCallback(new Payload<>(topic, content), callback); }
    //发送并等待回调,自定义载体
    void sendAndCallback(final Payload<C, R> payload, final Consumer<R> callback);
    
    //监听
    default void listen(final String topic, final TopicListener<Payload<C, R>> listener) { listen(topic, 0, listener); }
    //监听
    void listen(final String topic, final int index, final TopicListener<Payload<C, R>> listener);
    //取消监听
    void unlisten(final String topic, final TopicListener<Payload<C, R>> listener);
}
```


## 3、DamiApi，接口模式接口


```java
public interface DamiApi {
    //获取编码器
    Coder getCoder();
    //设置编码器
    void setCoder(Coder coder);
    //获取关联总线
    DamiBus getBus();
    
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


## 4、Payload<C, R>，事件装载接口


```java
public class Payload<C, R> implements Serializable {
    //获取附件
    public <T> T getAttachment(String key) {}
    //设置附件
    public <T> void setAttachment(String key, T value) {}
    //是否为请求（是的话，需要答复）
    public boolean isRequest() { }
    //答复
    public void reply(final R content) {}
    
    //唯一标识
    public String getGuid() {}
    //主题
    public String getTopic() {}
    //内容
    public C getContent() {}
}

```

Payload::reply，答复情况说明

| 发送接口              | 答复说明            | 备注                          |
|-------------------|-----------------|-----------------------------|
| send()            | 会出异常，提示不支持答复    | payload.isRequest() = false |
| sendAndResponse() | 第一条答复有效，且必须要有答复 | payload.isRequest() = true  |
| sendAndCallback() | 可以无限次答复有效       | payload.isRequest() = true  |


## 5、TopicListener<Event>，主题监听接口

```java
public interface TopicListener<Event extends Payload> {
    //处理事件
    void onEvent(final Event event) throws Throwable;
}
```

