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
package org.noear.dami.bus.receivable;

import org.noear.dami.bus.Event;
import org.noear.dami.bus.TopicListener;

import java.util.concurrent.CompletableFuture;

/**
 * 调用监听器
 *
 * @author noear
 * @since 2.0
 */
public interface CallTopicListener<C,R> extends TopicListener<Event<CallPayload<C, R>>> {
    /**
     * 处理监听事件
     *
     * @param event 事件
     */
    default void onEvent(Event<CallPayload<C, R>> event) throws Throwable {
        onCall(event, event.getPayload().getContent(), event.getPayload().getReceiver());
    }

    /**
     * 处理调用事件（由 onEvent 转发简化）
     *
     * @param event    事件
     * @param content  荷载内容
     * @param receiver 荷载接收器
     */
    void onCall(Event<CallPayload<C, R>> event, C content, CompletableFuture<R> receiver);
}