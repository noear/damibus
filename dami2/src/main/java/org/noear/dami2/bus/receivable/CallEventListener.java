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

import org.noear.dami2.bus.Event;
import org.noear.dami2.bus.EventListener;

import java.util.concurrent.CompletableFuture;

/**
 * 调用监听器
 *
 * @author noear
 * @since 2.0
 */
@FunctionalInterface
public interface CallEventListener<D,R> extends EventListener<CallPayload<D, R>> {
    /**
     * 处理监听事件
     *
     * @param event 事件
     */
    default void onEvent(Event<CallPayload<D, R>> event) throws Throwable {
        onCall(event, event.getPayload().getData(), event.getPayload().getSink());
    }

    /**
     * 处理调用事件（可由 onEvent 转发简化）
     *
     * @param event 事件
     * @param data  数据
     * @param sink  接收器（future）
     */
    void onCall(Event<CallPayload<D, R>> event, D data, CompletableFuture<R> sink);
}