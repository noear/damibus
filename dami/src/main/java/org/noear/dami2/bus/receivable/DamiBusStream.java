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
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 大米总线响应式流扩展
 *
 * @author noear
 * @since 2.0
 */
public interface DamiBusStream extends DamiBusExtension {
    /**
     * 流
     */
    default <C, R> Publisher<R> stream(String topic, C content) {
        return stream(topic, content, null);
    }

    /**
     * 流
     */
    default <C, R> Publisher<R> stream(String topic, C content, Consumer<Subscriber<? super R>> fallback) {
        if (fallback == null) {
            return subscriber -> {
                bus().<StreamPayload<C, ? super R>>send(topic, new StreamPayload<>(content, subscriber));
            };
        } else {
            return subscriber -> {
                bus().<StreamPayload<C, ? super R>>send(topic, new StreamPayload<>(content, subscriber), r -> {
                    fallback.accept(r.getReceiver());
                });
            };
        }
    }

    /**
     * 当流时
     */
    default <C, R> void onStream(String topic, BiConsumer<C, Subscriber<? super R>> consumer) {
        bus().<StreamPayload<C, R>>listen(topic, event -> {
            consumer.accept(event.getPayload().getContent(), event.getPayload().getReceiver());
        });
    }
}