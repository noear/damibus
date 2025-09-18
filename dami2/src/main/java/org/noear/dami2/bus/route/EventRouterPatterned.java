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

import org.noear.dami2.lpc.impl.ServiceMethodEventListener;
import org.noear.dami2.bus.EventListener;
import org.noear.dami2.bus.EventListenerHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


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
    private final List<Routing> routingList = new ArrayList<>();

    protected final ReentrantLock ROUTING_LIST_LOCK = new ReentrantLock();


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
        ROUTING_LIST_LOCK.lock();
        try {
            routingList.add(routerFactory.create(topic, index, listener));
        } finally {
            ROUTING_LIST_LOCK.unlock();
        }

        if (log.isDebugEnabled()) {
            if (ServiceMethodEventListener.class.isAssignableFrom(listener.getClass())) {
                log.debug("TopicRouter listener added(@{}): {}", topic, listener);
            } else {
                log.debug("TopicRouter listener added(@{}): {}", topic, listener.getClass().getName());
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
        ROUTING_LIST_LOCK.lock();
        try {
            routingList.removeIf(routing -> routing.matches(topic) && routing.getListener() == listener);
        } finally {
            ROUTING_LIST_LOCK.unlock();
        }

        if (log.isDebugEnabled()) {
            if (ServiceMethodEventListener.class.isAssignableFrom(listener.getClass())) {
                log.debug("TopicRouter listener removed(@{}): {}", topic, listener);
            } else {
                log.debug("TopicRouter listener removed(@{}): {}", topic, listener.getClass().getName());
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
        ROUTING_LIST_LOCK.lock();
        try {
            routingList.removeIf(routing -> routing.matches(topic));
        } finally {
            ROUTING_LIST_LOCK.unlock();
        }

        if (log.isDebugEnabled()) {
            log.debug("TopicRouter listener removed(@{}): all..", topic);
        }
    }

    /**
     * 路由匹配
     */
    @Override
    public List<EventListenerHolder> matching(String topic) {
        List<EventListenerHolder> routings = routingList.stream()
                .filter(r -> r.matches(topic))
                .sorted(Comparator.comparing(Routing::getIndex))
                .collect(Collectors.toList());

        return routings;
    }
}