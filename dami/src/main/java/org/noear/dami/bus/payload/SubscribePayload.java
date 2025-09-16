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
package org.noear.dami.bus.payload;

import org.reactivestreams.Subscriber;

/**
 * 订阅核载（响应式流）
 *
 * @author noear
 * @since 2.0
 */
public class SubscribePayload<C,R> {
    private C content;
    private final Subscriber<R> subscriber;

    public SubscribePayload(C content, Subscriber<R> subscriber) {
        this.content = content;
        this.subscriber = subscriber;
    }

    /**
     * 内容
     */
    public C content() {
        return content;
    }

    /**
     * 获取订阅者
     */
    public Subscriber<R> getSubscriber() {
        return subscriber;
    }
}