package org.noear.dami.bus;

import org.noear.dami.bus.impl.*;

/**
 * 大米总线实现
 *
 * @author noear
 * @since 1.0
 */
public class DamiBusImpl<C> implements DamiBus<C>, DamiBusConfigurator<C> {
    //路由器
    private TopicRouter<C> router;
    //调度器
    private TopicDispatcher<C> dispatcher;
    //负载工厂
    private MessageFactory<C> factory;

    public DamiBusImpl(TopicRouter<C> router) {
        if (router == null) {
            this.router = new TopicRouterDefault<>();
        } else {
            this.router = router;
        }

        this.factory = PayloadDefault::new;
        this.dispatcher = new TopicDispatcherDefault<>();
    }

    public DamiBusImpl() {
        this(null);
    }

    /**
     * 设置主题路由器
     */
    public DamiBusConfigurator<C> topicRouter(TopicRouter<C> router) {
        if (router != null) {
            this.router = router;
        }
        return this;
    }

    @Override
    public DamiBusConfigurator<C> topicDispatcher(TopicDispatcher<C> dispatcher) {
        if (dispatcher != null) {
            this.dispatcher = dispatcher;
        }
        return this;
    }

    /**
     * 设置事件负载工厂
     */
    public DamiBusConfigurator<C> payloadFactory(MessageFactory<C> factory) {
        if (factory != null) {
            this.factory = factory;
        }
        return this;
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

        Message<C> payload = factory.create(topic, content);

        dispatcher.dispatch(payload, router);

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
    public void listen(final String topic, final int index, final TopicListener<Message<C>> listener) {
        router.add(topic, index, listener);
    }

    /**
     * 取消监听
     *
     * @param topic    主题
     * @param listener 监听
     */
    @Override
    public void unlisten(final String topic, final TopicListener<Message<C>> listener) {
        router.remove(topic, listener);
    }

    /**
     * 取消监听
     *
     * @param topic 主题
     */
    @Override
    public void unlisten(String topic) {
        router.remove(topic);
    }

    /**
     * 路由器
     */
    public TopicRouter<C> router() {
        return this.router;
    }
}