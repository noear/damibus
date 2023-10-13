package org.noear.dami.bus;

/**
 * @author noear
 * @since 1.0
 */
public class TopicListenerHolder<C,R> {
    protected final int index;
    protected final TopicListener<Payload<C,R>> listener;

    public TopicListenerHolder(int index, TopicListener<Payload<C,R>> listener) {
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
    public TopicListener<Payload<C, R>> getListener() {
        return listener;
    }
}
