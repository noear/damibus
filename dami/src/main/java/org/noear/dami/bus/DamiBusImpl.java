/*
 * Copyright 2023～ noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.noear.dami.bus;

import org.noear.dami.bus.impl.*;

import java.util.function.Consumer;

/**
 * 大米总线实现
 *
 * @author noear
 * @since 1.0
 */
public class DamiBusImpl<P> implements DamiBus<P>, DamiBusConfigurator<P> {
    //路由器
    private TopicRouter<P> router;
    //调度器
    private TopicDispatcher<P> dispatcher;
    //负载工厂
    private MessageFactory<P> factory;

    public DamiBusImpl(TopicRouter<P> router) {
        if (router == null) {
            this.router = new TopicRouterDefault<>();
        } else {
            this.router = router;
        }

        this.factory = MessageDefault::new;
        this.dispatcher = new TopicDispatcherDefault<>();
    }

    public DamiBusImpl() {
        this(null);
    }

    /**
     * 设置主题路由器
     */
    public DamiBusConfigurator<P> topicRouter(TopicRouter<P> router) {
        if (router != null) {
            this.router = router;
        }
        return this;
    }

    @Override
    public DamiBusConfigurator<P> topicDispatcher(TopicDispatcher<P> dispatcher) {
        if (dispatcher != null) {
            this.dispatcher = dispatcher;
        }
        return this;
    }

    /**
     * 设置事件负载工厂
     */
    public DamiBusConfigurator<P> messageFactory(MessageFactory<P> factory) {
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
     * @param topic    主题
     * @param payload  核载
     * @param fallback 应急处理（当没有订阅时执行）
     * @return 消息
     */
    @Override
    public Message<P> send(final String topic, final P payload, Consumer<P> fallback) {
        AssertUtil.assertTopic(topic);

        Message<P> message = factory.create(topic, payload);

        dispatcher.dispatch(message, router);

        if (message.getHandled() == false) {
            if (fallback != null) {
                fallback.accept(payload);
            }
        }

        return message;
    }


    /**
     * 监听
     *
     * @param topic    主题
     * @param index    顺序位
     * @param listener 监听
     */
    @Override
    public void listen(final String topic, final int index, final TopicListener<Message<P>> listener) {
        router.add(topic, index, listener);
    }

    /**
     * 取消监听
     *
     * @param topic    主题
     * @param listener 监听
     */
    @Override
    public void unlisten(final String topic, final TopicListener<Message<P>> listener) {
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
    public TopicRouter<P> router() {
        return this.router;
    }
}