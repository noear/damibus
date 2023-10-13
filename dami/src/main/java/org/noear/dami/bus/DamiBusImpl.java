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
    //调度器
    private TopicDispatcher<C,R> dispatcher;
    //负载工厂
    private PayloadFactory<C, R> factory;
    //响应超时：默认3s
    private long timeout = 3000;

    public DamiBusImpl(TopicRouter<C, R> router, TopicDispatcher<C,R> dispatcher, PayloadFactory<C, R> factory) {
        if (router == null) {
            this.router = new TopicRouterDefault<>();
        } else {
            this.router = router;
        }

        if (dispatcher == null) {
            this.dispatcher = new TopicDispatcherDefault<>();
        } else {
            this.dispatcher = dispatcher;
        }

        if (this.factory == null) {
            this.factory = PayloadDefault::new;
        } else {
            this.factory = factory;
        }
    }

    public DamiBusImpl() {
        this(null, null, null);
    }

    public DamiBusImpl(TopicRouter<C, R> topicRouter) {
        this(topicRouter, null, null);
    }

    public DamiBusImpl(TopicRouter<C, R> topicRouter, TopicDispatcher<C, R> topicDispatcher) {
        this(topicRouter, topicDispatcher, null);
    }

    public DamiBusImpl(TopicDispatcher<C, R> topicDispatcher) {
        this(null, topicDispatcher, null);
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

    @Override
    public DamiBusConfigurator<C, R> topicDispatcher(TopicDispatcher<C, R> dispatcher) {
        if (dispatcher != null) {
            this.dispatcher = dispatcher;
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
        dispatcher.addInterceptor(index, interceptor);
    }

    /**
     * 发送（不需要答复）
     *
     * @param topic   主题
     * @param content 内容
     * @return 是否有订阅
     */
    @Override
    public boolean send(final String topic, final C content) {
        AssertUtil.assertTopic(topic);

        Payload<C, R> payload = factory.create(topic, content, null);

        dispatcher.handle(payload, router);

        return payload.getHandled();
    }

    /**
     * 发送并请求（会等待响应）
     *
     * @param topic   主题
     * @param content 内容
     * @return 响应结果
     */
    @Override
    public R sendAndRequest(final String topic, final C content) {
        AssertUtil.assertTopic(topic);

        CompletableFuture<R> future = new CompletableFuture<>();
        Payload<C, R> payload = factory.create(topic, content, new AcceptorResponse<>(future));

        dispatcher.handle(payload, router);

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
     * 发送并订阅
     *
     * @param topic    主题
     * @param content  内容
     * @param consumer 消费者
     * @return 是否有订阅
     */
    @Override
    public boolean sendAndSubscribe(final String topic, final C content, final Consumer<R> consumer) {
        AssertUtil.assertTopic(topic);

        Payload<C, R> payload = factory.create(topic, content, new AcceptorCallback<>(consumer));

        dispatcher.handle(payload, router);

        return payload.getHandled();
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