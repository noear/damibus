package org.noear.dami.bus.impl;

import org.noear.dami.bus.Message;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.bus.TopicListenerHolder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 主题监听管道
 *
 * @author noear
 * @since 1.0
 */
public final class TopicListenPipeline<C> {
    private final List<TopicListenerHolder<C>> list = new ArrayList<>();

    /**
     * 添加监听
     *
     * @param listener 监听器
     */
    public void add(final TopicListener<Message<C>> listener) {
        add(0, listener);
    }

    /**
     * 添加监听（带顺序位）
     *
     * @param index    顺序位
     * @param listener 监听器
     */
    public void add(final int index, final TopicListener<Message<C>> listener) {
        list.add(new TopicListenerHolder<>(index, listener));
        list.sort(Comparator.comparing(TopicListenerHolder::getIndex));
    }

    /**
     * 移除监听
     *
     * @param listener 监听器
     */
    public void remove(final TopicListener<Message<C>> listener) {
        list.removeIf(e -> e.getListener().equals(listener));
    }

    public List<TopicListenerHolder<C>> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "TopicListenPipeline{" +
                "size=" + list.size() +
                '}';
    }
}
