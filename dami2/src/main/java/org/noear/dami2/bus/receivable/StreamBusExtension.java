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
package org.noear.dami2.bus.receivable;

import org.noear.dami2.bus.DamiBusExtension;
import org.noear.dami2.bus.EventListener;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.function.Consumer;

/**
 * 生成流（响应式流）扩展
 *
 * @author noear
 * @since 2.0
 */
public interface StreamBusExtension extends DamiBusExtension {
    /**
     * 发送流事件
     */
    default <D, R> Publisher<R> stream(String topic, D data) {
        return stream(topic, data, null);
    }

    /**
     * 发送流事件
     */
    default <D, R> Publisher<R> stream(String topic, D data, Consumer<Subscriber<? super R>> fallback) {
        if (fallback == null) {
            return subscriber -> {
                bus().send(topic, new StreamPayload<>(data, subscriber));
            };
        } else {
            return subscriber -> {
                bus().send(topic, new StreamPayload<>(data, subscriber), r -> {
                    fallback.accept(r.getSink());
                });
            };
        }
    }

    /**
     * 监听流事件
     *
     * @param topic    事件主题
     * @param listener 监听器
     */
    default <D, R> void listen(String topic, StreamEventListener<D, R> listener) {
        listen(topic, 0, listener);
    }

    /**
     * 监听流事件
     *
     * @param topic    事件主题
     * @param index    顺序位
     * @param listener 监听器
     */
    default <D, R> void listen(String topic, int index, StreamEventListener<D, R> listener) {
        bus().listen(topic, index, (EventListener<? extends Object>) listener);
    }
}