### 1、Dami，操作主类


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

### 2、Dami，配置操作类

```java
public class DamiConfig {
    //配置总线的事件路由器
    public static void configure(EventRouter eventRouter);
    //配置总线的事件调度器
    public static void configure(EventDispatcher eventDispatcher);
    //配置总线的事件工厂
    public static void configure(EventFactory eventFactory);

    //配置接口模式的编解码器
    public static void configure(Coder coder);

    //配置总线实例
    public static void configure(DamiBus bus);
    //配置接口实例
    public static void configure(DamiLpc lpc);
}
```

### 3、`DamiBus<P>`，总线模式接口


```java
public interface DamiBus {
    //拦截事件
    <P> void intercept(int index, EventInterceptor<P> interceptor);
    <P> void intercept(EventInterceptor<P> interceptor);
    
    //发送事件
    <P> Result<P> send(final String topic, final P payload);
    <P> Result<P> send(final String topic, final P payload, Consumer<P> fallback);
    
    //发送事件
    <P> Result<P> send(final Event<P> event);
    <P> Result<P> send(final Event<P> event, Consumer<P> fallback);
   
    //监听事件
    <P> void listen(final String topic, final TopicListener<P> listener);
    //监听事件
    <P> void listen(final String topic, final int index, final TopicListener<P> listener);
    //取消监听
    <P> void unlisten(final String topic, final TopicListener<P> listener);
    void unlisten(final String topic);

    //路由器
    TopicRouter router();
    
    //----------------------------
    // 通过继承 CallBusExtension 获得
    
    //发送调用事件
    default <D, R> CompletableFuture<R> call(String topic, D data);
    default <D, R> CompletableFuture<R> call(String topic, D data, Consumer<CompletableFuture<R>> fallback);
    default <D, R> Result<CallPayload<D, R>> callAsResult(String topic, D data);
    default <D, R> Result<CallPayload<D, R>> callAsResult(String topic, D data, Consumer<CompletableFuture<R>> fallback);
    
    //监听调用事件
    default <D, R> void listen(String topic, CallEventHandler<D, R> handler);
    default <D, R> void listen(String topic, int index, CallEventHandler<D, R> handler);
    
    //----------------------------
    // 通过继承 StreamBusExtension  获得
   
    //发送流事件
    default <D, R> Publisher<R> stream(String topic, D data);
    default <D, R> Publisher<R> stream(String topic, D data, Consumer<Subscriber<? super R>> fallback);
    
    //监听流事件
    default <D, R> void listen(String topic, StreamEventHandler<D, R> handler);
    default <D, R> void listen(String topic, int index, StreamEventHandler<D, R> handler);
}
```

通过不同的 lambda 参数个数设计，仍可支持在 listen 时使用 lambda 不冲突（可自动识别）。

### 4、DamiLpc，接口模式接口（lpc, 本地过程调用）


```java
public interface DamiLpc extends DamiBusExtension {
    //获取编码器
    Coder coder();
    
    //创建服务消费者（接口代理）
    <T> T createConsumer(String topicMapping, Class<T> consumerApi);
    
    //注册服务提供者（一个服务，只能监听一个主题）
    void registerProvider(String topicMapping, Object serviceObj);
    void registerProvider(String topicMapping, int index, Object serviceObj);
    
    //注销服务提供者
    void unregisterProvider(String topicMapping, Object serviceObj);
}
```

注意 topicMapping 与 topic 的区别：

* topicMapping 只用于 lpc，每个方法都通过（topicMapping + "." + mehtodName）形成自己的 topic。
* 提供：lpc 服务的方法名不要相同。否则形成的 topic 会冲突。

创建服务消费者（createConsumer）情况说明：

| 用例               | 对应总线接口                   | 说明               |
|------------------|--------------------------|------------------|
| void onCreated() | 返回为空的，call 发送            | 没有监听，不会异常        |
| User getUser()    | 返回类型的，call 发送 | 没有监听，会异常。且必须要有答复 |


### 5、`Event<P>`，事件负载接口


```java
public interface Event<P> extends Serializable {
    //设置处理标识（如果有监听，会标为已处理）
    void setHandled();
    //获取处理标识
    boolean getHandled();

    //附件
    Map<String, Object> getAttach();
    //主题
    String getTopic();
    //荷载
    P getPayload();
}
```

事件由：主题、荷载、附件，以入处理标识（是否有监听处理）组成。

### 6、`EventListener<P>`，事件监听接口

```java
@FunctionalInterface
public interface EventListener<P> {
    //处理监听事件
    void onEvent(final Event<P> event) throws Throwable;
}

@FunctionalInterface
public interface CallEventListener<D,R> extends EventListener<CallPayload<D, R>>, CallEventHandler<D,R> {
    //处理监听事件
    default void onEvent(Event<CallPayload<D, R>> event) throws Throwable {
        onCall(event, event.getPayload().getData(), event.getPayload().getSink());
    }
}

@FunctionalInterface
public interface StreamEventListener<D, R> extends EventListener<StreamPayload<D, R>> , StreamEventHandler<D,R> {
    //处理监听事件
    default void onEvent(Event<StreamPayload<D, R>> event) throws Throwable {
        onStream(event, event.getAttach(), event.getPayload().getData(), event.getPayload().getSink());
    }
}
```