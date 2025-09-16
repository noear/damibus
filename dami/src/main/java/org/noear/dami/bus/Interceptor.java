package org.noear.dami.bus;

/**
 * 拦截器
 *
 * @author noear
 * @since 1.0
 */
public interface Interceptor<C> {
    /**
     * 拦截
     *
     * @param message 事件负载
     * @param chain   拦截链
     */
    void doIntercept(Message<C> message, InterceptorChain<C> chain);
}
