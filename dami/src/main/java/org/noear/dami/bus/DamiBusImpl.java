package org.noear.dami.bus;

import org.noear.dami.bus.impl.*;
import org.noear.dami.exception.DamiException;
import org.noear.dami.exception.DamiNoSubscriptionException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 大米总线实现
 *
 * @author noear
 * @since 1.0
 */
public class DamiBusImpl<C, R> implements DamiBus<C, R>, DamiBusConfigurator<C, R> {
    //路由器
    private TopicRouter<C, R> router;
    //负载工厂
    private PayloadFactory<C, R> factory;
    //超时：默认3s
    private long timeout = 3000;

    public DamiBusImpl() {
        router = new TopicRouterDefault<>();
        factory = PayloadDefault::new;
    }

    public DamiBusImpl(TopicRouter<C, R> topicRouter) {
        router = topicRouter;
        factory = PayloadDefault::new;
    }

    /**
     * 设置主题路由器
     */
    public DamiBusConfigurator<C, R> topicRouter(TopicRouter<C, R> router) {
        if (router != null) {
            this.router = router;
        }
        return this;
    }

    /**
     * 设置事件负载工厂
     */
    public DamiBusConfigurator<C, R> payloadFactory(PayloadFactory<C, R> factory) {
        if (factory != null) {
            this.factory = factory;
        }
        return this;
    }

    /**
     * 设置超时
     */
    @Override
    public DamiBusConfigurator<C, R> timeout(final long timeout) {
        this.timeout = timeout;
        return this;
    }


    /**
     * 获取超时
     */
    @Override
    public long timeout() {
        return timeout;
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
     * 发送（不需要答复）
     *
     * @param topic   主题
     * @param content 内容
     */
    @Override
    public void send(final String topic, final C content) {
        router.handle(factory.create(topic, content, null));
    }

    /**
     * 发送并等待响应
     *
     * @param topic   主题
     * @param content 内容
     */
    @Override
    public R sendAndResponse(final String topic, final C content) {
        CompletableFuture<R> future = new CompletableFuture<>();
        Payload<C, R> payload = factory.create(topic, content, new AcceptorResponse<>(future));
        router.handle(payload);

        if (payload.getHandled()) {
            try {
                return future.get(timeout, TimeUnit.MILLISECONDS);
            } catch (Throwable e) {
                throw new DamiException(e);
            }
        } else {
            throw new DamiNoSubscriptionException("No response subscription");
        }
    }

    /**
     * 发送并等待回调
     *
     * @param topic    主题
     * @param content  内容
     * @param callback 回调函数
     */
    @Override
    public void sendAndCallback(final String topic, final C content, final Consumer<R> callback) {
        Payload<C, R> payload = factory.create(topic, content, new AcceptorCallback<>(callback));

        router.handle(payload);
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