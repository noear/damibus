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
package org.noear.dami2.bus.impl;

import org.noear.dami2.bus.TopicListener;

/**
 * 路由选择工厂
 *
 * @author kamosama
 * @since 1.0
 * */
public interface RoutingFactory {
    /**
     * 创建路由记录
     *
     * @param topic    主题
     * @param index    顺序位
     * @param listener 监听器
     */
    <P> Routing create(final String topic, final int index, final TopicListener<P> listener);
}