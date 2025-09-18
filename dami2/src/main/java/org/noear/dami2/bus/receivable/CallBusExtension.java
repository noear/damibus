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
import org.noear.dami2.bus.Result;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * 调用扩展
 *
 * @author noear
 * @since 2.0
 */
public interface CallBusExtension extends DamiBusExtension {
    /**
     * 调用
     */
    default <C, R> CompletableFuture<R> call(String topic, C content) {
        return call(topic, content, null);
    }

    /**
     * 调用
     */
    default <C, R> CompletableFuture<R> call(String topic, C content, Consumer<CompletableFuture<R>> fallback) {
        return callAsResult(topic, content, fallback).getPayload().getReceiver();
    }

    /**
     * 调用
     */
    default <C, R> Result<CallPayload<C, R>> callAsResult(String topic, C content, Consumer<CompletableFuture<R>> fallback) {
        if (fallback == null) {
            return bus().send(topic, new CallPayload<>(content));
        } else {
            return bus().send(topic, new CallPayload<>(content), r -> {
                fallback.accept(r.getReceiver());
            });
        }
    }

    /**
     * 当调用时
     */
    default <C, R> void onCall(String topic, CallEventListener<C, R> listener) {
        bus().listen(topic, listener);
    }
}