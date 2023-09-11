package org.noear.dami.bus;

import org.noear.dami.exception.DamiException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 大米总线实现
 *
 * @author noear
 * @since 1.0
 */
public final class DamiBusImpl<C, R> implements DamiBus<C, R> {
    /**
     * 路由器
     */
    private final TopicRouter<C, R> router = new TopicRouterImpl<>();

    /**
     * 超时：默认3s
     */
    private long timeout = 3000;

    /**
     * 获取超时
     */
    @Override
    public long getTimeout() {
        return timeout;
    }

    /**
     * 设置超时
     */
    @Override
    public void setTimeout(final long timeout) {
        this.timeout = timeout;
    }

    /**
     * 拦截
     *
     * @param index       顺序位
     * @param interceptor 拦截器
     */
    @Override
    public void intercept(int index, Interceptor interceptor) {
        router.addInterceptor(index, interceptor);
    }

    /**
     * 发送（不需要答复）,自定义载体
     *
     * @param payload 发送载体
     */
    @Override
    public void send(final Payload<C, R> payload) {
        router.handle(payload);
    }

    /**
     * 发送并等待响应,自定义载体
     *
     * @param payload 发送载体
     */
    @Override
    public R sendAndResponse(final Payload<C, R> payload) {
        CompletableFuture<R> future = new CompletableFuture<>();
        payload.future = future::complete;
        router.handle(payload);

        try {
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (Throwable e) {
            throw new DamiException(e);
        }
    }

    /**
     * 发送并等待回调,自定义载体
     *
     * @param payload 发送载体
     */
    @Override
    public void sendAndCallback(final Payload<C, R> payload, final Consumer<R> callback) {
        payload.future = callback;

        router.handle(payload);
    }

    /**
     * 答复
     *
     * @param request 请求装载
     * @param content 答复内容
     */
    @Override
    public void reply(final Payload<C, R> request, final R content) {
        if (request.isRequest() == false) {
            throw new DamiException("This payload does not support a response");
        }

        request.future.accept(content);
    }

    /**
     * 监听
     *
     * @param topic    主题
     * @param index    顺序位
     * @param listener 监听
     */
    @Override
    public void listen(final String topic, final int index, final TopicListener<Payload<C, R>> listener) {
        router.add(topic, index, listener);
    }

    /**
     * 取消监听
     *
     * @param topic    主题
     * @param listener 监听
     */
    @Override
    public void unlisten(final String topic, final TopicListener<Payload<C, R>> listener) {
        router.remove(topic, listener);
    }
}