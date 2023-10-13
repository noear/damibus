package org.noear.dami.bus.impl;

import org.noear.dami.bus.Payload;
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
public final class TopicListenPipeline<C,R> {
    private final List<TopicListenerHolder<C, R>> list = new ArrayList<>();

    /**
     * 添加监听
     *
     * @param listener 监听器
     */
    public void add(final TopicListener<Payload<C, R>> listener) {
        add(0, listener);
    }

    /**
     * 添加监听（带顺序位）
     *
     * @param index    顺序位
     * @param listener 监听器
     */
    public void add(final int index, final TopicListener<Payload<C, R>> listener) {
        list.add(new TopicListenerHolder<>(index, listener));
        list.sort(Comparator.comparing(TopicListenerHolder::getIndex));
    }

    /**
     * 移除监听
     *
     * @param listener 监听器
     */
    public void remove(final TopicListener<Payload<C, R>> listener) {
        list.removeIf(e -> e.getListener().equals(listener));
    }

    public List<TopicListenerHolder<C, R>> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "TopicListenPipeline{" +
                "size=" + list.size() +
                '}';
    }
}
