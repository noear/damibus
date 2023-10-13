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
     * 获取响应超时（单位：毫秒）
     */
    long timeout();

    /**
     * 拦截
     *
     * @param index       顺序位
     * @param interceptor 拦截器
     */
    void intercept(int index, Interceptor<C,R> interceptor);

    /**
     * 拦截
     *
     * @param interceptor 拦截器
     */
    default void intercept(Interceptor<C,R> interceptor) {
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
     * 发送并请求（会等待响应）
     *
     * @param topic   主题
     * @param content 内容
     * @return 响应结果
     */
    R sendAndRequest(final String topic, final C content);

    /**
     * 发送并订阅
     *
     * @param topic    主题
     * @param content  内容
     * @param consumer 消费者
     * @return 是否有订阅目标
     */
    boolean sendAndSubscribe(final String topic, final C content, final Consumer<R> consumer);


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