package org.noear.dami.bus.impl;

import org.noear.dami.api.impl.MethodTopicListener;
import org.noear.dami.bus.Payload;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.bus.TopicListenerHolder;
import org.noear.dami.bus.TopicRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 主题路由器（默认啥希表实现方案）
 *
 * @author noear
 * @since 1.0
 */
public class TopicRouterDefault<C, R> implements TopicRouter<C, R> {
    static final Logger log = LoggerFactory.getLogger(TopicRouterDefault.class);

    /**
     * 主题监听管道
     */
    private final Map<String, TopicListenPipeline<C, R>> pipelineMap = new LinkedHashMap<>();

    protected final ReentrantLock PIPELINE_MAP_LOCK = new ReentrantLock();

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
    public void add(final String topic, final int index, final TopicListener<Payload<C, R>> listener) {
        PIPELINE_MAP_LOCK.lock();
        try {
            final TopicListenPipeline<C, R> pipeline = pipelineMap.computeIfAbsent(topic, t -> new TopicListenPipeline<>());
            pipeline.add(index, listener);
        } finally {
            PIPELINE_MAP_LOCK.unlock();
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
    public void remove(final String topic, final TopicListener<Payload<C, R>> listener) {
        PIPELINE_MAP_LOCK.lock();
        try {
            final TopicListenPipeline<C, R> pipeline = pipelineMap.get(topic);
            if (pipeline != null) {
                pipeline.remove(listener);
            }
        } finally {
            PIPELINE_MAP_LOCK.unlock();
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
    public void remove(final String topic) {
        PIPELINE_MAP_LOCK.lock();
        try {
            pipelineMap.remove(topic);
        } finally {
            PIPELINE_MAP_LOCK.unlock();
        }

        if (log.isDebugEnabled()) {
            log.debug("TopicRouter listener removed(@{}): all..", topic);
        }
    }

    /**
     * 路由匹配
     */
    @Override
    public List<TopicListenerHolder<C, R>> matching(String topic) {
        final TopicListenPipeline<C, R> pipeline = pipelineMap.get(topic);

        if (pipeline == null) {
            return null;
        } else {
            return pipeline.getList();
        }
    }
}