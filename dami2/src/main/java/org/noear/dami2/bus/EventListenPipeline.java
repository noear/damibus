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
package org.noear.dami2.bus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 事件监听管道
 *
 * @author noear
 * @since 1.0
 */
public final class EventListenPipeline {
    private final List<EventListenerHolder> list = new ArrayList<>();

    /**
     * 添加监听
     *
     * @param listener 监听器
     */
    public <P> void add(final EventListener<P> listener) {
        add(0, listener);
    }

    /**
     * 添加监听（带顺序位）
     *
     * @param index    顺序位
     * @param listener 监听器
     */
    public <P> void add(final int index, final EventListener<P> listener) {
        list.add(new EventListenerHolder(index, listener));
        list.sort(Comparator.comparing(EventListenerHolder::getIndex));
    }

    /**
     * 移除监听
     *
     * @param listener 监听器
     */
    public <P> void remove(final EventListener<P> listener) {
        list.removeIf(e -> e.getListener().equals(listener));
    }

    public List<EventListenerHolder> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "TopicListenPipeline{" +
                "size=" + list.size() +
                '}';
    }
}
