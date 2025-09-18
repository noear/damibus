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

import org.noear.dami.bus.receivable.DamiBusCall;
import org.noear.dami.bus.receivable.DamiBusStream;

import java.util.function.Consumer;

/**
 * 大米总线（提供 Local Procedure Call 服务）
 *
 * @author noear
 * @since 1.0
 */
public interface DamiBus extends DamiBusExtension, DamiBusCall, DamiBusStream {
    /**
     * 拦截
     *
     * @param index       顺序位
     * @param interceptor 拦截器
     */
    <P> void intercept(int index, Interceptor<P> interceptor);

    /**
     * 拦截
     *
     * @param interceptor 拦截器
     */
    default <P> void intercept(Interceptor<P> interceptor) {
        intercept(0, interceptor);
    }

    /**
     * 发送（不需要答复）
     *
     * @param topic   主题
     * @param payload 荷载
     * @return 结果
     */
    default <P> Result<P> send(final String topic, final P payload) {
        return send(topic, payload, null);
    }

    /**
     * 发送（不需要答复）
     *
     * @param topic    主题
     * @param payload  荷载
     * @param fallback 应急处理（当没有订阅时执行）
     * @return 结果
     */
    <P> Result<P> send(final String topic, final P payload, Consumer<P> fallback);


    /**
     * 监听
     *
     * @param topic    主题
     * @param listener 监听
     */
    default <P> void listen(final String topic, final TopicListener<Event<P>> listener) {
        listen(topic, 0, listener);
    }

    /**
     * 监听
     *
     * @param topic    主题
     * @param index    顺序位
     * @param listener 监听
     */
    <P> void listen(final String topic, final int index, final TopicListener<Event<P>> listener);

    /**
     * 取消监听
     *
     * @param topic    主题
     * @param listener 监听
     */
    <P> void unlisten(final String topic, final TopicListener<Event<P>> listener);

    /**
     * 取消监听（主题下的所有监听）
     *
     * @param topic 主题
     */
    void unlisten(final String topic);

    /**
     * 路由器
     */
    TopicRouter router();
}