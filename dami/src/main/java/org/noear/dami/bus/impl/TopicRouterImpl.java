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
 * 主题路由器
 *
 * @author noear
 * @since 1.0
 */
public final class TopicRouterImpl<C, R> implements TopicRouter<C, R>, Interceptor<C,R> {
    static final Logger log = LoggerFactory.getLogger(TopicRouterImpl.class);

    /**
     * 主题监听管道
     */
    private final Map<String, TopicListenPipeline<Payload<C, R>>> pipelineMap = new LinkedHashMap<>();

    /**
     * 拦截器
     */
    private final List<InterceptorEntity> interceptors = new ArrayList<>();

    public TopicRouterImpl() {
        interceptors.add(new InterceptorEntity(Integer.MAX_VALUE, this));
    }

    /**
     * 添加拦截器
     *
     * @param index       顺序位
     * @param interceptor 拦截器
     */
    @Override
    public synchronized void addInterceptor(int index, Interceptor interceptor) {
        interceptors.add(new InterceptorEntity(index, interceptor));

        if (interceptors.size() > 1) {
            //排序（顺排）
            interceptors.sort(Comparator.comparing(x -> x.getIndex()));
        }

        if (log.isDebugEnabled()) {
            log.debug("TopicRouter interceptor added: {}", interceptor.getClass().getName());
        }
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
     * 接收事件
     *
     * @param payload 事件负载
     */
    @Override
    public void handle(final Payload<C, R> payload) {
        new InterceptorChain<C, R>(interceptors).doIntercept(payload);
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

    /**
     * 断言主题是否为空
     *
     * @param topic
     */
    protected void assertTopic(final String topic) {
        if (topic == null || topic.isEmpty()) {
            throw new DamiException("The topic cannot be empty");
        }
    }
}