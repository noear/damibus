package org.noear.dami;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 主题监听管道
 *
 * @author noear
 * @since 1.0
 */
public class TopicListenPipeline<Event> implements TopicListener<Event> {
    private List<EH> list = new ArrayList<>();

    /**
     * 添加监听
     */
    public void add(TopicListener<Event> listener) {
        add(0, listener);
    }

    /**
     * 添加监听（带顺序位）
     *
     * @param index    顺序位
     * @param listener 监听器
     */
    public void add(int index, TopicListener<Event> listener) {
        list.add(new EH(index, listener));
        list.sort(Comparator.comparing(EH::getIndex));
    }

    /**
     * 移除监听
     *
     * @param listener 监听器
     */
    public void remove(TopicListener<Event> listener) {
        for (int i = 0; i < list.size(); i++) {
            if (listener.equals(list.get(i).listener)) {
                list.remove(i);
                i--;
            }
        }
    }

    @Override
    public void onEvent(Event event) throws Throwable {
        for (EH h : list) {
            h.listener.onEvent(event);
        }
    }

    static class EH<Event> {
        int index;
        TopicListener<Event> listener;

        EH(int index, TopicListener<Event> listener) {
            this.index = index;
            this.listener = listener;
        }

        public int getIndex() {
            return index;
        }
    }
}
