package org.noear.dami.bus;

/**
 * 主题派发器
 *
 * @author noear
 * @since 1.0
 */
public interface TopicDispatcher<C,R> {
    /**
     * 添加拦截器
     */
    void addInterceptor(int index, Interceptor interceptor);

    /**
     * 派发
     */
    void dispatch(Payload<C, R> payload, TopicRouter<C, R> router);
}
