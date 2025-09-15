package org.noear.dami.bus;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 大米总线（提供 Local Procedure Call 服务）
 *
 * @author noear
 * @since 1.0
 */
public interface DamiBus<C, R> {
    /**
     * 拦截
     *
     * @param index       顺序位
     * @param interceptor 拦截器
     */
    void intercept(int index, Interceptor<C, R> interceptor);

    /**
     * 拦截
     *
     * @param interceptor 拦截器
     */
    default void intercept(Interceptor<C, R> interceptor) {
        intercept(0, interceptor);
    }

    /**
     * 发送（不需要答复）
     *
     * @param topic   主题
     * @param content 内容
     * @return 是否有订阅
     */
    boolean send(final String topic, final C content);

    /**
     * 调用（要求有一个答复）
     *
     * @param topic   主题
     * @param content 内容
     * @return 响应结果
     */
    default R call(final String topic, final C content) {
        return call(topic, content, 3000);
    }

    /**
     * 调用（要求有一个答复）
     *
     * @param topic   主题
     * @param content 内容
     * @param timeout 超时（毫秒）
     * @return 响应结果
     */
    default R call(final String topic, final C content, long timeout) {
        return call(topic, content, timeout, null);
    }

    /**
     * 调用（要求有一个答复）
     *
     * @param topic    主题
     * @param content  内容
     * @param fallback 应急处理（如果没有返回）
     * @return 响应结果
     */
    default R call(final String topic, final C content, Supplier<R> fallback) {
        return call(topic, content, 3000, fallback);
    }

    /**
     * 调用（要求有一个答复）
     *
     * @param topic    主题
     * @param content  内容
     * @param timeout  超时（毫秒）
     * @param fallback 应急处理（如果没有返回）
     * @return 响应结果
     */
    R call(final String topic, final C content, long timeout, Supplier<R> fallback);


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

    /**
     * 取消监听（主题下的所有监听）
     *
     * @param topic 主题
     */
    void unlisten(final String topic);

    /**
     * 路由器
     */
    TopicRouter<C, R> router();

    /// ////////////////

    /**
     * 发送并请求（会等待响应）
     *
     * @param topic   主题
     * @param content 内容
     * @return 响应结果
     * @deprecated 1.1.0 （简化模式，更名为 call） {@link #call(String, Object)}
     */
    @Deprecated
    default R sendAndRequest(final String topic, final C content) {
        return sendAndRequest(topic, content, 3000);
    }

    /**
     * 发送并请求（会等待响应）
     *
     * @param topic   主题
     * @param content 内容
     * @param timeout 超时（毫秒）
     * @return 响应结果
     * @deprecated 1.1.0 （简化模式，更名为 call） {@link #call(String, Object, long)}
     */
    @Deprecated
    default R sendAndRequest(final String topic, final C content, long timeout) {
        return sendAndRequest(topic, content, timeout, null);
    }

    /**
     * 发送并请求（会等待响应）
     *
     * @param topic    主题
     * @param content  内容
     * @param fallback 应急处理（如果没有返回）
     * @return 响应结果
     * @deprecated 1.1.0 （简化模式，更名为 call） {@link #call(String, Object, Supplier)}
     */
    @Deprecated
    default R sendAndRequest(final String topic, final C content, Supplier<R> fallback) {
        return sendAndRequest(topic, content, 3000, fallback);
    }

    /**
     * 发送并请求（会等待响应）
     *
     * @param topic    主题
     * @param content  内容
     * @param timeout  超时（毫秒）
     * @param fallback 应急处理（如果没有返回）
     * @return 响应结果
     * @deprecated 1.1.0 （简化模式，更名为 call） {@link #call(String, Object, long, Supplier)}
     */
    @Deprecated
    default R sendAndRequest(final String topic, final C content, long timeout, Supplier<R> fallback) {
        return call(topic, content, timeout, fallback);
    }


    /**
     * 发送并订阅
     *
     * @param topic    主题
     * @param content  内容
     * @param consumer 消费者
     * @return 是否有订阅目标
     * @deprecated 1.1.0 （简化模式，不再支持流调用）
     */
    @Deprecated
    boolean sendAndSubscribe(final String topic, final C content, final Consumer<R> consumer);
}