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
package org.noear.dami.bus;

import java.util.List;

/**
 * 主题路由器
 *
 * @author kongweiguang
 */
public interface TopicRouter {
    /**
     * 添加监听
     *
     * @param topic    主题
     * @param index    顺序位
     * @param listener 监听器
     */
    <P> void add(final String topic, final int index, final TopicListener<Event<P>> listener);


    /**
     * 移除监听
     *
     * @param topic    主题
     * @param listener 监听器
     */
    <P> void remove(final String topic, final TopicListener<Event<P>> listener);

    /**
     * 移除监听
     *
     * @param topic 主题
     */
    <P> void remove(final String topic);

    /**
     * 路由匹配
     */
    List<TopicListenerHolder> matching(final String topic);

    /**
     * 计数
     */
    default int count(final String topic) {
        List list = matching(topic);
        return list == null ? 0 : list.size();
    }
}