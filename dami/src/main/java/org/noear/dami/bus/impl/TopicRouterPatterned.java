package org.noear.dami.bus.impl;

import org.noear.dami.api.impl.MethodTopicListener;
import org.noear.dami.bus.Message;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.bus.TopicListenerHolder;
import org.noear.dami.bus.TopicRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


/**
 * 主题路由器（模式匹配实现方案；支持 * 和 ** 占位符；支持 / 或 . 做为间隔）
 *
 * @author noear
 * @example /a/*, /a/**b
 * @since 1.0
 */
public class TopicRouterPatterned<C> implements TopicRouter<C> {
    static final Logger log = LoggerFactory.getLogger(TopicRouterDefault.class);

    /**
     * 主题路由记录
     */
    private final List<Routing<C>> routingList = new ArrayList<>();

    protected final ReentrantLock ROUTING_LIST_LOCK = new ReentrantLock();


    /**
     * 路由工厂
     */
    private final RoutingFactory<C> routerFactory;

    public TopicRouterPatterned(RoutingFactory<C> routerFactory) {
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
    public void add(final String topic, final int index, final TopicListener<Message<C>> listener) {
        ROUTING_LIST_LOCK.lock();
        try {
            routingList.add(routerFactory.create(topic, index, listener));
        } finally {
            ROUTING_LIST_LOCK.unlock();
        }

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
    public void remove(final String topic, final TopicListener<Message<C>> listener) {
        ROUTING_LIST_LOCK.lock();
        try {
            routingList.removeIf(routing -> routing.matches(topic) && routing.getListener() == listener);
        } finally {
            ROUTING_LIST_LOCK.unlock();
        }

        if (log.isDebugEnabled()) {
            if (MethodTopicListener.class.isAssignableFrom(listener.getClass())) {
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
    public List<TopicListenerHolder<C>> matching(String topic) {
        List<TopicListenerHolder<C>> routings = routingList.stream()
                .filter(r -> r.matches(topic))
                .sorted(Comparator.comparing(Routing::getIndex))
                .collect(Collectors.toList());

        return routings;
    }
}