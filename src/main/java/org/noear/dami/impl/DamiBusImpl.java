package org.noear.dami.impl;

import org.noear.dami.DamiBus;
import org.noear.dami.TopicListener;
import org.noear.dami.TopicRouter;

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
     * 发送（不需要响应）
     */
    @Override
    public void send(final String topic, final C content) {
        send(new Payload<>(topic, content));
    }

    @Override
    public void send(final Payload<C, R> payload) {
        router.handle(payload);
    }

    /**
     * 请求并等待响应
     */
    @Override
    public R requestAndResponse(final String topic, final C content) {
        return r(new Payload<>(topic, content));
    }

    @Override
    public R requestAndResponse(final Payload<C, R> payload, final Consumer<R> callback) {
        return r(payload);
    }

    private R r(final Payload<C, R> payload) {
        payload.future = new CompletableFuture<>();

        router.handle(payload);

        try {
            return payload.future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 请求并等待回调
     */
    @Override
    public void requestAndCallback(final String topic, final C content, final Consumer<R> callback) {
        c(new Payload<>(topic, content), callback);
    }

    @Override
    public void requestAndCallback(final Payload<C, R> payload, final Consumer<R> callback) {
        c(payload, callback);
    }

    private void c(final Payload<C, R> payload, final Consumer<R> callback) {
        payload.future = new CompletableFuture<>();
        payload.future.thenAccept(callback);

        router.handle(payload);
    }

    /**
     * 响应
     */
    @Override
    public void response(final Payload<C, R> request, final R content) {
        if (request.future == null) {
            throw new IllegalStateException("This payload does not support a response");
        }

        request.future.complete(content);
    }


    /**
     * 监听
     */
    @Override
    public void listen(final String topic, final TopicListener<Payload<C, R>> listener) {
        listen(topic, 0, listener);
    }

    /**
     * 监听
     */
    @Override
    public void listen(final String topic, final int index, final TopicListener<Payload<C, R>> listener) {
        router.add(topic, index, listener);
    }

    /**
     * 取消监听
     */
    @Override
    public void unlisten(final String topic, final TopicListener<Payload<C, R>> listener) {
        router.remove(topic, listener);
    }
}
