package org.noear.dami;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 大米总线实现
 *
 * @author noear
 * @since 1.0
 */
public class DamiBusImpl implements DamiBus {
    private static DamiBus global;

    public static DamiBus global() {
        if (global == null) {
            synchronized (DamiBus.class) {
                if (global == null) {
                    global = new DamiBusImpl();
                }
            }
        }

        return global;
    }

    /**
     * 路由器
     */
    private TopicRouter router = new TopicRouter();

    /**
     * 超时：默认3s
     */
    private long timeout = 3000;

    /**
     * 获取超时
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * 设置超时
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }


    /**
     * 发送（不需要响应）
     */
    public void send(String topic, String content) {
        router.handle(new Payload(topic, content));
    }

    /**
     * 请求并等待响应
     */
    public String requestAndResponse(String topic, String content) {
        Payload payload = new Payload(topic, content);
        payload.future = new CompletableFuture();
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
    public void requestAndCallback(String topic, String content, Consumer<String> callback) {
        Payload payload = new Payload(topic, content);
        payload.future = new CompletableFuture();
        payload.future.thenAccept(callback);
        router.handle(payload);
    }

    /**
     * 响应
     */
    public void response(Payload request, String content) {
        if (request.future == null) {
            throw new IllegalStateException("This payload does not support a response");
        }

        request.future.complete(content);
    }


    /**
     * 监听
     */
    public void listen(String topic, TopicListener<Payload> listener) {
        router.add(topic, listener);
    }

    /**
     * 监听
     */
    @Override
    public void listen(String topic, int index, TopicListener<Payload> listener) {
        router.add(topic, index, listener);
    }

    /**
     * 取消监听
     */
    public void unlisten(String topic, TopicListener<Payload> listener) {
        router.remove(topic, listener);
    }
}
