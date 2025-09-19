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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * 事件路由器（模式匹配实现方案；支持 * 和 ** 占位符；支持 / 或 . 做为间隔）
 *
 * @author noear
 * @example /a/*, /a/**b
 * @since 1.0
 */
public class EventRouterPatterned implements EventRouter {
    static final Logger log = LoggerFactory.getLogger(EventRouterDefault.class);

    /**
     * 路由记录
     */
    private final Map<String, EventListenPipeline> exactMatchMap = new ConcurrentHashMap<>();
    private final List<Routing> patternRoutes = new CopyOnWriteArrayList<>();


    /**
     * 路由工厂
     */
    private final RoutingFactory routerFactory;

    public EventRouterPatterned(RoutingFactory routerFactory) {
        super();
        this.routerFactory = routerFactory;
    }

    /**
     * 添加监听
     *
     * @param topic    主题
     * @param index    顺序位
     * @param listener 监听器
     */
    @Override
    public <P> void add(final String topic, final int index, final EventListener<P> listener) {
        Routing routing = routerFactory.create(topic, index, listener);
        if (routing.isPatterned()) {
            // 模式匹配路由
            patternRoutes.add(routing);
        } else {
            // 精确匹配路由
            exactMatchMap.computeIfAbsent(topic, k -> new EventListenPipeline()).add(index, listener);
        }

        if (log.isDebugEnabled()) {
            if (ProviderMethodEventListener.class.isAssignableFrom(listener.getClass())) {
                log.debug("EventRouter listener added(@{}): {}", topic, listener);
            } else {
                log.debug("EventRouter listener added(@{}): {}", topic, listener.getClass().getName());
            }
        }
    }

    /**
     * 移除监听
     *
     * @param topic    主题
     * @param listener 监听器
     */
    @Override
    public <P> void remove(final String topic, final EventListener<P> listener) {
        final EventListenPipeline pipeline = exactMatchMap.get(topic);
        if (pipeline != null) {
            pipeline.remove(listener);
        }

        patternRoutes.removeIf(routing -> routing.matches(topic) && routing.getListener() == listener);


        if (log.isDebugEnabled()) {
            if (ProviderMethodEventListener.class.isAssignableFrom(listener.getClass())) {
                log.debug("EventRouter listener removed(@{}): {}", topic, listener);
            } else {
                log.debug("EventRouter listener removed(@{}): {}", topic, listener.getClass().getName());
            }
        }
    }

    /**
     * 移除监听
     *
     * @param topic 主题
     */
    @Override
    public void remove(String topic) {
        exactMatchMap.remove(topic);
        patternRoutes.removeIf(routing -> routing.matches(topic));

        if (log.isDebugEnabled()) {
            log.debug("EventRouter listener removed(@{}): all..", topic);
        }
    }

    /**
     * 路由匹配
     */
    @Override
    public List<EventListenerHolder> matching(String topic) {
        List<EventListenerHolder> result = new ArrayList<>();

        // 1. 先检查模式匹配（只有需要时才执行）
        if (!patternRoutes.isEmpty()) {
            for (Routing routing : patternRoutes) {
                if (routing.matches(topic)) {
                    result.add(routing);
                }
            }
        }

        // 2. 再检查精确匹配
        EventListenPipeline exactMatches = exactMatchMap.get(topic);
        if (exactMatches != null) {
            if (result.isEmpty()) {
                //如果上面没有，则直接返回（减省排序时间）
                return exactMatches.getList();
            } else {
                result.addAll(exactMatches.getList());
            }
        }

        if (result.size() < 2) {
            //少于2条不用排序了（减省排序时间）
            return result;
        } else {
            result.sort(Comparator.comparing(EventListenerHolder::getIndex));
            return result;
        }
    }
}