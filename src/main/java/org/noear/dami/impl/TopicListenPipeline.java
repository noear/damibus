package org.noear.dami.impl;

import org.noear.dami.TopicListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 主题监听管道
 *
 * @author noear
 * @since 1.0
 */
public final class TopicListenPipeline<Event> implements TopicListener<Event> {
    private final List<EH<Event>> list = new ArrayList<>();

    /**
     * 添加监听
     */
    public void add(final TopicListener<Event> listener) {
        add(0, listener);
    }

    /**
     * 添加监听（带顺序位）
     *
     * @param index    顺序位
     * @param listener 监听器
     */
    public void add(final int index, final TopicListener<Event> listener) {
        list.add(new EH<>(index, listener));
        list.sort(Comparator.comparing(EH::getIndex));
    }

    /**
     * 移除监听
     *
     * @param listener 监听器
     */
    public void remove(final TopicListener<Event> listener) {
        list.removeIf(e -> e.listener.equals(listener));
    }

    @Override
    public void onEvent(final Event event) throws Throwable {
        for (EH<Event> h : list) {
            h.listener.onEvent(event);
        }
    }

    private final static class EH<Event> {
        final int index;
        final TopicListener<Event> listener;

        EH(int index, TopicListener<Event> listener) {
            this.index = index;
            this.listener = listener;
        }

        public int getIndex() {
            return index;
        }
    }
}
