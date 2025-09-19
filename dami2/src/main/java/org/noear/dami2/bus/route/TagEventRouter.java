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
import org.noear.dami2.lpc.impl.ProviderMethodEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 事件路由器（模式匹配实现方案；支持 * 和 ** 占位符；支持 / 或 . 做为间隔）
 *
 * @author noear
 * @example /a/*, /a/**b
 * @since 1.0
 */
public class TagEventRouter implements EventRouter {
    static final Logger log = LoggerFactory.getLogger(HashEventRouter.class);

    //路由记录
    private final Map<String, List<TagRouting>> topicRoutingMap = new ConcurrentHashMap<>();


    /**
     * 添加监听
     *
     * @param topicExpr 主题表达式
     * @param index     顺序位
     * @param listener  监听器
     */
    @Override
    public <P> void add(final String topicExpr, final int index, final EventListener<P> listener) {
        TagRouting routing = new TagRouting(topicExpr, index, listener);

        topicRoutingMap.computeIfAbsent(routing.getTopic(), k -> new ArrayList<>()).add(routing);

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
        TagRouting.TopicTags topicTags = TagRouting.TopicTags.get(topicExpr);
        final List<TagRouting> list = topicRoutingMap.get(topicTags.topic);

        if (list != null) {
            list.removeIf(r -> r.getExpr().equals(topicExpr) && r.getListener() == listener);
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
    public void remove(String topicExpr) {
        TagRouting.TopicTags topicTags = TagRouting.TopicTags.get(topicExpr);
        final List<TagRouting> list = topicRoutingMap.get(topicTags.topic);
        list.removeIf(routing -> routing.getExpr().equals(topicExpr));

        if (log.isDebugEnabled()) {
            log.debug("EventRouter listener removed(@{}): all..", topicExpr);
        }
    }

    /**
     * 路由匹配
     */
    @Override
    public List<EventListenerHolder> matching(String topicExpr) {
        TagRouting.TopicTags topicTags = TagRouting.TopicTags.get(topicExpr);
        final List<TagRouting> tagRoutes = topicRoutingMap.get(topicTags.topic);

        if (tagRoutes == null || tagRoutes.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<EventListenerHolder> result = new ArrayList<>();

            for (TagRouting routing : tagRoutes) {
                if (routing.matches(topicTags)) {
                    result.add(routing);
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
}