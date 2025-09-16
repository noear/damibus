package org.noear.dami.bus;

/**
 * @author noear
 * @since 1.0
 */
public class TopicListenerHolder<C> {
    protected final int index;
    protected final TopicListener<Message<C>> listener;

    public TopicListenerHolder(int index, TopicListener<Message<C>> listener) {
        this.index = index;
        this.listener = listener;
    }

    /**
     * 获取顺序位
     */
    public int getIndex() {
        return index;
    }

    /**
     * 获取监听器
     */
    public TopicListener<Message<C>> getListener() {
        return listener;
    }

    @Override
    public String toString() {
        return "TopicListenerHolder{" +
                "index=" + index +
                ", listener=" + listener +
                '}';
    }
}
