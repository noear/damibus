package org.noear.dami.bus;

/**
 * 主题路由器
 *
 * @param <C>
 * @param <R>
 * @author kongweiguang
 */
public interface TopicRouter<C, R> {
    /**
     * 添加拦截器
     *
     * @param index       顺序位
     * @param interceptor 拦截器
     */
    void addInterceptor(int index, Interceptor interceptor);

    /**
     * 添加监听
     *
     * @param topic    主题
     * @param index    顺序位
     * @param listener 监听器
     */
    void add(final String topic, final int index, final TopicListener<Payload<C, R>> listener);


    /**
     * 移除监听
     *
     * @param topic    主题
     * @param listener 监听器
     */
    void remove(final String topic, final TopicListener<Payload<C, R>> listener);


    /**
     * 接收事件并路由
     *
     * @param payload 事件负载
     * @return 是否有订阅
     */
    void handle(final Payload<C, R> payload);
}