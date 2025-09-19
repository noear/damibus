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

import org.noear.dami2.bus.EventListenPipeline;
import org.noear.dami2.lpc.impl.ProviderMethodEventListener;
import org.noear.dami2.bus.EventListener;
import org.noear.dami2.bus.EventListenerHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件路由器（默认啥希表实现方案）
 *
 * @author noear
 * @since 1.0
 */
public class EventRouterDefault implements EventRouter {
    static final Logger log = LoggerFactory.getLogger(EventRouterDefault.class);

    /**
     * 主题监听管道
     */
    private final Map<String, EventListenPipeline> pipelineMap = new ConcurrentHashMap<>();

    /**
     * 添加监听
     *
     * @param topicExpr 主题表达式
     * @param index     顺序位
     * @param listener  监听器
     */
    @Override
    public <P> void add(final String topicExpr, final int index, final EventListener<P> listener) {
        pipelineMap.computeIfAbsent(topicExpr, t -> new EventListenPipeline()).add(index, listener);

        if (log.isDebugEnabled()) {
            if (ProviderMethodEventListener.class.isAssignableFrom(listener.getClass())) {
                log.debug("EventRouter listener added(@{}): {}", topicExpr, listener);
            } else {
                log.debug("EventRouter listener added(@{}): {}", topicExpr, listener.getClass().getName());
            }
        }
    }

    /**
     * 移除监听
     *
     * @param topicExpr 主题表达式
     * @param listener  监听器
     */
    @Override
    public <P> void remove(final String topicExpr, final EventListener<P> listener) {
        final EventListenPipeline pipeline = pipelineMap.get(topicExpr);
        if (pipeline != null) {
            pipeline.remove(listener);
        }

        if (log.isDebugEnabled()) {
            if (ProviderMethodEventListener.class.isAssignableFrom(listener.getClass())) {
                log.debug("EventRouter listener removed(@{}): {}", topicExpr, listener);
            } else {
                log.debug("EventRouter listener removed(@{}): {}", topicExpr, listener.getClass().getName());
            }
        }
    }

    /**
     * 移除监听
     *
     * @param topicExpr 主题表达式
     */
    @Override
    public void remove(final String topicExpr) {
        pipelineMap.remove(topicExpr);

        if (log.isDebugEnabled()) {
            log.debug("EventRouter listener removed(@{}): all..", topicExpr);
        }
    }

    /**
     * 路由匹配
     */
    @Override
    public List<EventListenerHolder> matching(String topic) {
        final EventListenPipeline pipeline = pipelineMap.get(topic);

        if (pipeline == null) {
            return null;
        } else {
            return pipeline.getList();
        }
    }
}