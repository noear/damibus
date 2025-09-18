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
import org.noear.dami2.bus.receivable.CallBusExtension;
import org.noear.dami2.bus.receivable.StreamBusExtension;
import org.noear.dami2.bus.route.EventRouter;

import java.util.function.Consumer;

/**
 * 大米总线（提供 Local Procedure Call 服务）
 *
 * @author noear
 * @since 1.0
 */
public interface DamiBus extends DamiBusExtension, CallBusExtension, StreamBusExtension {
    /**
     * 拦截
     *
     * @param index       顺序位
     * @param interceptor 拦截器
     */
    <P> void intercept(int index, EventInterceptor<P> interceptor);

    /**
     * 拦截
     *
     * @param interceptor 拦截器
     */
    default <P> void intercept(EventInterceptor<P> interceptor) {
        intercept(0, interceptor);
    }

    /**
     * 发送事件
     *
     * @param topic   事件主题
     * @param payload 事件荷载
     * @return 结果
     */
    default <P> Result<P> send(final String topic, final P payload) {
        return send(topic, payload, null);
    }

    /**
     * 发送事件
     *
     * @param topic    事件主题
     * @param payload  事件荷载
     * @param fallback 应急处理（当没有订阅时启用）
     * @return 结果
     */
    <P> Result<P> send(final String topic, final P payload, Consumer<P> fallback);

    /**
     * 发送事件
     *
     * @param event 事件
     * @return 结果
     */
    default <P> Result<P> send(Event<P> event) {
        return send(event, null);
    }

    /**
     * 发送事件
     *
     * @param event    事件
     * @param fallback 应急处理（当没有订阅时启用）
     * @return 结果
     */
    <P> Result<P> send(Event<P> event, Consumer<P> fallback);


    /**
     * 监听事件
     *
     * @param topic    事件主题
     * @param listener 监听器
     */
    default <P> void listen(final String topic, final EventListener<P> listener) {
        listen(topic, 0, listener);
    }

    /**
     * 监听事件
     *
     * @param topic    事件主题
     * @param index    顺序位
     * @param listener 监听器
     */
    <P> void listen(final String topic, final int index, final EventListener<P> listener);

    /**
     * 取消事件监听
     *
     * @param topic    事件主题
     * @param listener 监听器
     */
    <P> void unlisten(final String topic, final EventListener<P> listener);

    /**
     * 取消事件监听（主题下的所有监听）
     *
     * @param topic 事件主题
     */
    void unlisten(final String topic);

    /**
     * 路由器
     */
    EventRouter router();
}