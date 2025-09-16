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
package org.noear.dami.api;

import org.noear.dami.bus.DamiBus;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * 大米接口（提供 Local Procedure Call 服务）
 *
 * @author noear
 * @since 1.0
 */
public interface DamiApi {
    /**
     * 获取编码器
     */
    Coder coder();

    /**
     * 获取关联总线
     *
     */
    <P> DamiBus<P> bus();

    /// /////////////////

    /**
     * 调用
     */
    default <C, R> CompletableFuture<R> call(String topic, C content) {
        return call(topic, content, null);
    }

    /**
     * 调用
     *
     * @param fallback 应用处理（或降级处理）
     */
    <C, R> CompletableFuture<R> call(String topic, C content, Supplier<R> fallback);

    /**
     * 处理
     */
    <C, R> void handle(String topic, BiConsumer<C, CompletableFuture<R>> consumer);

    /// ////////////////

    /**
     * 流
     */
    <C, R> Publisher<R> stream(String topic, C content);

    /**
     * 输出
     */
    <C, R> void feed(String topic, BiConsumer<C, Subscriber<? super R>> consumer);


    /// ////////////////

    /**
     * 创建发送器代理
     *
     * @param topicMapping 主题映射
     * @param senderClz    发送器接口类
     */
    <T> T createSender(String topicMapping, Class<T> senderClz);

    /**
     * 注册监听者实例（一个监听类，只能监听一个主题）
     *
     * @param topicMapping 主题映射
     * @param listenerObj  监听器实现类
     */
    default void registerListener(String topicMapping, Object listenerObj) {
        registerListener(topicMapping, 0, listenerObj);
    }

    /**
     * 注册监听者实例（一个监听类，只能监听一个主题）
     *
     * @param topicMapping 主题映射
     * @param index        顺序位
     * @param listenerObj  监听器实现类
     */
    void registerListener(String topicMapping, int index, Object listenerObj);

    /**
     * 取消注册监听者实例
     *
     * @param topicMapping 主题映射
     * @param listenerObj  监听器实现类
     */
    void unregisterListener(String topicMapping, Object listenerObj);
}