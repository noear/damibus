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
package org.noear.dami2.bus;

import org.noear.dami2.bus.intercept.EventInterceptor;
import org.noear.dami2.bus.route.EventRouter;
import org.noear.dami2.bus.route.HashEventRouter;

import java.util.function.Consumer;

/**
 * 大米总线实现
 *
 * @author noear
 * @since 1.0
 */
public class DamiBusImpl implements DamiBus, DamiBusConfigurator {
    //路由器
    private EventRouter router;
    //调度器
    private EventDispatcher dispatcher;
    //生成器
    private EventFactory factory;

    public DamiBusImpl(EventRouter router) {
        if (router == null) {
            this.router = new HashEventRouter();
        } else {
            this.router = router;
        }

        this.factory = SimpleEvent::new;
        this.dispatcher = new EventDispatcherDefault();
    }

    public DamiBusImpl() {
        this(null);
    }

    /**
     * 设置事件路由器
     */
    public DamiBusConfigurator eventRouter(EventRouter router) {
        if (router != null) {
            this.router = router;
        }
        return this;
    }

    @Override
    public DamiBusConfigurator eventDispatcher(EventDispatcher dispatcher) {
        if (dispatcher != null) {
            this.dispatcher = dispatcher;
        }
        return this;
    }

    /**
     * 设置事件事件工厂
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
    public void intercept(int index, EventInterceptor interceptor) {
        dispatcher.addInterceptor(index, interceptor);
    }

    /**
     * 发送事件
     *
     * @param topic    事件主题
     * @param payload  荷载
     * @param fallback 应急处理（当没有订阅时启用）
     * @return 结果
     */
    @Override
    public <P> Result<P> send(final String topic, final P payload, Consumer<P> fallback) {
        AssertUtil.assertTopic(topic);

        Event<P> event = factory.create(topic, payload);

        return send(event, fallback);
    }

    /**
     * 发送事件
     *
     * @param event    事件
     * @param fallback 应急处理（当没有订阅时启用）
     * @return 结果
     */
    @Override
    public <P> Result<P> send(Event<P> event, Consumer<P> fallback) {
        dispatcher.dispatch(event, router);

        if (event.getHandled() == false) {
            if (fallback != null) {
                fallback.accept(event.getPayload());
            }
        }

        return event;
    }

    /**
     * 监听事件
     *
     * @param topic    事件主题
     * @param index    顺序位
     * @param listener 监听器
     */
    @Override
    public <P> void listen(final String topic, final int index, final EventListener<P> listener) {
        router.add(topic, index, listener);
    }

    /**
     * 取消监听
     *
     * @param topic    事件主题
     * @param listener 监听器
     */
    @Override
    public <P> void unlisten(final String topic, final EventListener<P> listener) {
        router.remove(topic, listener);
    }

    /**
     * 取消监听
     *
     * @param topic 事件主题
     */
    @Override
    public void unlisten(String topic) {
        router.remove(topic);
    }

    /**
     * 路由器
     */
    public EventRouter router() {
        return this.router;
    }

    /**
     * 当前总线
     */
    @Override
    public DamiBus bus() {
        return this;
    }
}