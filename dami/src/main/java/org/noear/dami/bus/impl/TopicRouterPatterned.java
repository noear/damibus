package org.noear.dami.bus.impl;

import org.noear.dami.api.impl.MethodTopicListener;
import org.noear.dami.bus.*;
import org.noear.dami.exception.DamiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
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
public class TopicRouterPatterned<C,R> extends TopicRouterBase<C,R> {
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
        assertTopic(topic);

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
        assertTopic(topic);

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

    /**
     * 事件拦截并路由分发
     */
    @Override
    public void doIntercept(Payload<C, R> payload, InterceptorChain chain) {
        assertTopic(payload.getTopic());

        if (log.isTraceEnabled()) {
            log.trace("{}", payload);
        }

        final List<Routing<C, R>> routings = routingList.stream()
                .filter(r -> r.matches(payload.getTopic()))
                .sorted(Comparator.comparing(Routing::getIndex))
                .collect(Collectors.toList());

        if (!routings.isEmpty()) {
            try {
                for (Routing<C, R> r1 : routings) {
                    r1.getListener().onEvent(payload);
                }
                payload.setHandled();
            } catch (InvocationTargetException e) {
                throw new DamiException(e.getTargetException());
            } catch (UndeclaredThrowableException e) {
                throw new DamiException(e.getUndeclaredThrowable());
            } catch (Throwable e) {
                throw new DamiException(e);
            }
        } else {
            if (log.isWarnEnabled()) {
                log.warn("There's no matching listening on the topic(@{})", payload.getTopic());
            }
        }
    }
}