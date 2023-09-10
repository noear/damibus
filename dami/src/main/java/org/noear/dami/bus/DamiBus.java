package org.noear.dami.bus;

import java.util.function.Consumer;

/**
 * 大米总线（提供 Local Procedure Call 服务）
 *
 * @author noear
 * @since 1.0
 */
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
     * 发送（不需要响应）
     *
     * @param topic   主题
     * @param content 内容
     */
    default void send(final String topic, final C content) {
        send(new Payload<>(topic, content));
    }

    /**
     * 发送（不需要响应）,自定义载体
     *
     * @param payload 发送载体
     */
    void send(final Payload<C, R> payload);

    /**
     * 请求并等待响应
     *
     * @param topic   主题
     * @param content 内容
     */
    default R requestAndResponse(final String topic, final C content) {
        return requestAndResponse(new Payload<>(topic, content));
    }

    /**
     * 请求并等待响应,自定义载体
     *
     * @param payload 发送载体
     */
    R requestAndResponse(final Payload<C, R> payload);

    /**
     * 请求并等待回调
     *
     * @param topic    主题
     * @param content  内容
     * @param callback 回调函数
     */
    default void requestAndCallback(final String topic, final C content, final Consumer<R> callback) {
        requestAndCallback(new Payload<>(topic, content), callback);
    }

    /**
     * 请求并等待回调,自定义载体
     *
     * @param payload 发送载体
     */
    void requestAndCallback(final Payload<C, R> payload, final Consumer<R> callback);

    /**
     * 响应
     *
     * @param request 请求装载
     * @param content 响应内容
     */
    void response(final Payload<C, R> request, final R content);

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
