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
     * 发送调用事件
     *
     * @param topic 事件主题
     * @param data  数据
     * @return 接收器
     */
    default <D, R> CompletableFuture<R> call(String topic, D data) {
        return call(topic, data, null);
    }

    /**
     * 发送调用事件
     *
     * @param topic    事件主题
     * @param data     数据
     * @param fallback 应急处理（当没有计阅时启用）
     * @return 接收器
     */
    default <D, R> CompletableFuture<R> call(String topic, D data, Consumer<CompletableFuture<R>> fallback) {
        return callAsResult(topic, data, fallback).getPayload().getReceiver();
    }

    /**
     * 发送调用事件
     *
     * @param topic 事件主题
     * @param data  数据
     * @return 结果
     */
    default <D, R> Result<CallPayload<D, R>> callAsResult(String topic, D data) {
        return callAsResult(topic, data, null);
    }

    /**
     * 发送调用事件
     *
     * @param topic    事件主题
     * @param data     数据
     * @param fallback 应急处理（当没有计阅时启用）
     * @return 结果
     */
    default <D, R> Result<CallPayload<D, R>> callAsResult(String topic, D data, Consumer<CompletableFuture<R>> fallback) {
        if (fallback == null) {
            return bus().send(topic, new CallPayload<>(data));
        } else {
            return bus().send(topic, new CallPayload<>(data), r -> {
                fallback.accept(r.getReceiver());
            });
        }
    }

    /**
     * 监听调用事件
     *
     * @param topic   事件主题
     * @param handler 调用事件处理
     */
    default <D, R> void listen(String topic, CallEventHandler<D, R> handler) {
        listen(topic, 0, handler);
    }

    /**
     * 监听调用事件
     *
     * @param topic   事件主题
     * @param index   顺序位
     * @param handler 调用事件处理
     */
    default <D, R> void listen(String topic, int index, CallEventHandler<D, R> handler) {
        bus().<CallPayload<D, R>>listen(topic, index, event -> {
            handler.onCall(event, event.getPayload().getData(), event.getPayload().getReceiver());
        });
    }
}