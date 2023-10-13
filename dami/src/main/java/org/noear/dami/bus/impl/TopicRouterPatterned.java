package org.noear.dami.bus.impl;

import org.noear.dami.api.impl.MethodTopicListener;
import org.noear.dami.bus.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 主题路由器（模式匹配实现方案；支持 * 和 ** 占位符；支持 / 或 . 做为间隔）
 *
 *
 * @example /a/*, /a/**b
 * @author noear
 * @since 1.0
 */
public class TopicRouterPatterned<C,R> implements TopicRouter<C,R> {
    static final Logger log = LoggerFactory.getLogger(TopicRouterDefault.class);

    /**
     * 主题路由记录
     */
    private final List<Routing<C, R>> routingList = new ArrayList<>();

    /**
     * 路由工厂
     */
    private final RoutingFactory<C, R> routerFactory;

    public TopicRouterPatterned(RoutingFactory<C, R> routerFactory) {
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
    public synchronized void add(final String topic, final int index, final TopicListener<Payload<C, R>> listener) {
        routingList.add(routerFactory.create(topic, index, listener));

        if (log.isDebugEnabled()) {
            if (MethodTopicListener.class.isAssignableFrom(listener.getClass())) {
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
    public synchronized void remove(final String topic, final TopicListener<Payload<C, R>> listener) {
        for (int i = 0; i < routingList.size(); i++) {
            Routing<C, R> routing = routingList.get(i);
            if (routing.matches(topic)) {
                if (routing.getListener() == listener) {
                    routingList.remove(i);
                    i--;
                }
            }
        }

        if (log.isDebugEnabled()) {
            if (MethodTopicListener.class.isAssignableFrom(listener.getClass())) {
                log.debug("TopicRouter listener removed(@{}): {}", topic, listener);
            } else {
                log.debug("TopicRouter listener removed(@{}): {}", topic, listener.getClass().getName());
            }
        }
    }

    @Override
    public List<TopicListenerHolder<C, R>> matching(String topic) {
        List<TopicListenerHolder<C, R>> routings = routingList.stream()
                .filter(r -> r.matches(topic))
                .sorted(Comparator.comparing(Routing::getIndex))
                .collect(Collectors.toList());

        return routings;
    }
}