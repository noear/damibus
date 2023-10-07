package org.noear.dami.bus.impl;

import org.noear.dami.api.impl.MethodTopicListener;
import org.noear.dami.bus.*;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.exception.DamiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.*;

/**
 * 主题路由器（默认啥希表实现方案）
 *
 * @author noear
 * @since 1.0
 */
public class TopicRouterDefault<C, R> extends TopicRouterBase<C,R> {
    static final Logger log = LoggerFactory.getLogger(TopicRouterDefault.class);

    /**
     * 主题监听管道
     */
    private final Map<String, TopicListenPipeline<Payload<C, R>>> pipelineMap = new LinkedHashMap<>();

    public TopicRouterDefault() {
        super();
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

        final TopicListenPipeline<Payload<C, R>> pipeline = pipelineMap.computeIfAbsent(topic, t -> new TopicListenPipeline<>());

        pipeline.add(index, listener);

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

        final TopicListenPipeline<Payload<C, R>> pipeline = pipelineMap.get(topic);

        if (pipeline != null) {
            pipeline.remove(listener);
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

        final TopicListenPipeline<Payload<C, R>> pipeline = pipelineMap.get(payload.getTopic());

        if (pipeline != null) {
            try {
                pipeline.onEvent(payload);
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