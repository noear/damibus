package org.noear.dami.bus;

/**
 * 主题调度器
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
     * 调度处理
     */
    void handle(Payload<C, R> payload, TopicRouter<C, R> router);
}
