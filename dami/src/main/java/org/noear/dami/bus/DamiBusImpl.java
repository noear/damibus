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
import org.noear.dami.bus.payload.RequestPayload;
import org.noear.dami.bus.payload.SubscribePayload;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 大米总线实现
 *
 * @author noear
 * @since 1.0
 */
public class DamiBusImpl implements DamiBus, DamiBusConfigurator {
    //路由器
    private TopicRouter router;
    //调度器
    private TopicDispatcher dispatcher;
    //负载工厂
    private EventFactory factory;

    public DamiBusImpl(TopicRouter router) {
        if (router == null) {
            this.router = new TopicRouterDefault();
        } else {
            this.router = router;
        }

        this.factory = EventDefault::new;
        this.dispatcher = new TopicDispatcherDefault();
    }

    public DamiBusImpl() {
        this(null);
    }

    /**
     * 设置主题路由器
     */
    public DamiBusConfigurator topicRouter(TopicRouter router) {
        if (router != null) {
            this.router = router;
        }
        return this;
    }

    @Override
    public DamiBusConfigurator topicDispatcher(TopicDispatcher dispatcher) {
        if (dispatcher != null) {
            this.dispatcher = dispatcher;
        }
        return this;
    }

    /**
     * 设置事件负载工厂
     */
    public DamiBusConfigurator eventFactory(EventFactory factory) {
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
    public <P> Result<P> send(final String topic, final P payload, Consumer<P> fallback) {
        AssertUtil.assertTopic(topic);

        Event<P> event = factory.create(topic, payload);

        dispatcher.dispatch(event, router);

        if (event.getHandled() == false) {
            if (fallback != null) {
                fallback.accept(payload);
            }
        }

        return event;
    }


    /**
     * 监听
     *
     * @param topic    主题
     * @param index    顺序位
     * @param listener 监听
     */
    @Override
    public <P> void listen(final String topic, final int index, final TopicListener<Event<P>> listener) {
        router.add(topic, index, listener);
    }

    /**
     * 取消监听
     *
     * @param topic    主题
     * @param listener 监听
     */
    @Override
    public <P> void unlisten(final String topic, final TopicListener<Event<P>> listener) {
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
    public TopicRouter router() {
        return this.router;
    }

    /// ////////////////////////

    /**
     * 调用
     */
    @Override
    public <C, R> CompletableFuture<R> call(String topic, C content, Consumer<CompletableFuture<R>> fallback) {
        if (fallback == null) {
            return this.<RequestPayload<C, R>>send(topic, new RequestPayload<>(content))
                    .getPayload()
                    .getReceiver();
        } else {
            return this.<RequestPayload<C, R>>send(topic, new RequestPayload<>(content), r -> {
                        fallback.accept(r.getReceiver());
                    })
                    .getPayload()
                    .getReceiver();
        }
    }

    /**
     * 处理
     */
    @Override
    public <C, R> void onCall(String topic, BiConsumer<C, CompletableFuture<R>> consumer) {
        this.<RequestPayload<C, R>>listen(topic, msg -> {
            consumer.accept(msg.getPayload().getContext(), msg.getPayload().getReceiver());
        });
    }

    /**
     * 流
     */
    @Override
    public <C, R> Publisher<R> stream(String topic, C content, Consumer<Subscriber<? super R>> fallback) {
        if (fallback == null) {
            return subscriber -> {
                this.<SubscribePayload<C, R>>send(topic, new SubscribePayload<>(content, subscriber));
            };
        } else {
            return subscriber -> {
                this.<SubscribePayload<C, R>>send(topic, new SubscribePayload<>(content, subscriber), r -> {
                    fallback.accept(r.getReceiver());
                });
            };
        }
    }

    /**
     * 提供（流）
     */
    @Override
    public <C, R> void onStream(String topic, BiConsumer<C, Subscriber<? super R>> consumer) {
        this.<SubscribePayload<C, R>>listen(topic, event -> {
            consumer.accept(event.getPayload().getContext(), event.getPayload().getReceiver());
        });
    }
}