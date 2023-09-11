
## Mami

```java
/**
 * 大米，本地过程调用框架
 *
 * @author noear
 * @since 1.0
 */
public class Dami {
    static final DamiBus bus = new DamiBusImpl<>();
    static final DamiApi api = new DamiApiImpl(bus);


    /**
     * 泛型、强类型总线界面
     */
    public static <C, R> DamiBus<C, R> bus() {
        return bus;
    }

    /**
     * 弱类型总线界面（适合类隔离的场景）
     */
    public static DamiBus<String, String> busStr() {
        return bus;
    }

    /**
     * 接口界面
     */
    public static DamiApi api() {
        return api;
    }

    /**
     * 拦截
     *
     * @param interceptor 拦截器
     */
    public static void intercept(Interceptor interceptor) {
        intercept(0, interceptor);
    }

    /**
     * 拦截
     *
     * @param index       顺序位
     * @param interceptor 拦截器
     */
    public static void intercept(int index, Interceptor interceptor) {
        bus.intercept(index, interceptor);
    }
}
```

## DamiBus<C, R>

```java
public interface DamiBus<C, R> {
    /**
     * 获取超时
     */
    long getTimeout();

    /**
     * 设置超时
     *
     * @param timeout 超时
     */
    void setTimeout(final long timeout);

    /**
     * 拦截
     *
     * @param index       顺序位
     * @param interceptor 拦截器
     */
    void intercept(int index, Interceptor interceptor);

    /**
     * 发送（不需要答复）
     *
     * @param topic   主题
     * @param content 内容
     */
    default void send(final String topic, final C content) {
        send(new Payload<>(topic, content));
    }

    /**
     * 发送（不需要答复）,自定义载体
     *
     * @param payload 发送载体
     */
    void send(final Payload<C, R> payload);

    /**
     * 发送并等待响应
     *
     * @param topic   主题
     * @param content 内容
     */
    default R sendAndResponse(final String topic, final C content) {
        return sendAndResponse(new Payload<>(topic, content));
    }

    /**
     * 发送并等待响应,自定义载体
     *
     * @param payload 发送载体
     */
    R sendAndResponse(final Payload<C, R> payload);

    /**
     * 发送并等待回调
     *
     * @param topic    主题
     * @param content  内容
     * @param callback 回调函数
     */
    default void sendAndCallback(final String topic, final C content, final Consumer<R> callback) {
        sendAndCallback(new Payload<>(topic, content), callback);
    }

    /**
     * 发送并等待回调,自定义载体
     *
     * @param payload 发送载体
     */
    void sendAndCallback(final Payload<C, R> payload, final Consumer<R> callback);

    /**
     * 签复
     *
     * @param request 请求装载
     * @param content 签复内容
     */
    void reply(final Payload<C, R> request, final R content);

    /**
     * 监听
     *
     * @param topic    主题
     * @param listener 监听
     */
    default void listen(final String topic, final TopicListener<Payload<C, R>> listener) {
        listen(topic, 0, listener);
    }

    /**
     * 监听
     *
     * @param topic    主题
     * @param index    顺序位
     * @param listener 监听
     */
    void listen(final String topic, final int index, final TopicListener<Payload<C, R>> listener);

    /**
     * 取消监听
     *
     * @param topic    主题
     * @param listener 监听
     */
    void unlisten(final String topic, final TopicListener<Payload<C, R>> listener);
}
```

## DamiApi

```java
public interface DamiApi {
    /**
     * 获取编码器
     */
    Coder getCoder();

    /**
     * 设置编码器
     *
     * @param coder 编码器
     */
    void setCoder(Coder coder);

    /**
     * 获取关联总线
     * */
    DamiBus getBus();

    /**
     * 创建发送器代理
     *
     * @param topicMapping 主题映射
     * @param senderClz    发送器接口类
     */
    <T> T createSender(String topicMapping, Class<T> senderClz);

    /**
     * 注册监听者实例（一个监听类，只能监听一个主题）
     *
     * @param topicMapping 主题映射
     * @param listenerObj  监听器实现类
     */
    default void registerListener(String topicMapping, Object listenerObj) {
        registerListener(topicMapping, 0, listenerObj);
    }

    /**
     * 注册监听者实例（一个监听类，只能监听一个主题）
     *
     * @param topicMapping 主题映射
     * @param index        顺序位
     * @param listenerObj  监听器实现类
     */
    void registerListener(String topicMapping, int index, Object listenerObj);

    /**
     * 取消注册监听者实例
     *
     * @param topicMapping 主题映射
     * @param listenerObj  监听器实现类
     */
    void unregisterListener(String topicMapping, Object listenerObj);
}
```