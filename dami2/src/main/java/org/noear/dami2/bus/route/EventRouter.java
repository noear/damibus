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
package org.noear.dami2.bus.route;

import org.noear.dami2.bus.EventListener;
import org.noear.dami2.bus.EventListenerHolder;

import java.util.List;

/**
 * 事件路由器
 *
 * @author kongweiguang
 */
public interface EventRouter {
    /**
     * 添加监听
     *
     * @param topicExpr 主题表达式
     * @param index     顺序位
     * @param listener  监听器
     */
    <P> void add(final String topicExpr, final int index, final EventListener<P> listener);


    /**
     * 移除监听
     *
     * @param topicExpr 主题表达式
     * @param listener  监听器
     */
    <P> void remove(final String topicExpr, final EventListener<P> listener);

    /**
     * 移除监听
     *
     * @param topicExpr 主题表达式
     */
    <P> void remove(final String topicExpr);

    /**
     * 路由匹配
     */
    List<EventListenerHolder> matching(final String topic);

    /**
     * 计数
     */
    default int count(final String topic) {
        List list = matching(topic);
        return list == null ? 0 : list.size();
    }
}