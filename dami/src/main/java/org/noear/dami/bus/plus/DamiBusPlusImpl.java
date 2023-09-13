package org.noear.dami.bus.plus;

import org.noear.dami.bus.DamiBusImpl;
import org.noear.dami.bus.TopicListener;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author noear
 * @since 1.0
 */
public class DamiBusPlusImpl<C,R> extends DamiBusImpl<C, R> implements DamiBusPlus<C,R> {
    private Map<Class<? extends TopicContentListener>, TopicListener> contentListenerMap = new LinkedHashMap<>();


    /**
     * 发送（不需要答复）
     *
     * @param content 事件内容
     */
    @Override
    public <T> void send(final T content) {
        this.send(content.getClass().getName(), (C) content);
    }

    /**
     * 监听
     *
     * @param contentType 内容类型
     * @param listener    监听
     */
    @Override
    public <T> void listen(Class<T> contentType, final TopicContentListener<T> listener) {
        listen(contentType, 0, listener);
    }

    /**
     * 监听
     *
     * @param contentType 内容类型
     * @param index       顺序位
     * @param listener    监听器
     */
    @Override
    public synchronized <T> void listen(final Class<T> contentType, int index, final TopicContentListener<T> listener) {
        if (contentListenerMap.containsKey(listener.getClass())) {
            return;
        }

        TopicListenerBridge<C, R> topicListener = new TopicListenerBridge<>(listener);
        contentListenerMap.put(listener.getClass(), topicListener);

        this.listen(contentType.getName(), index, topicListener);
    }

    /**
     * 取消监听
     *
     * @param listener 监听器
     */
    @Override
    public synchronized <T> void unlisten(final Class<T> contentType, final TopicContentListener<T> listener) {
        TopicListenerBridge<C, R> topicListener = (TopicListenerBridge<C, R>) contentListenerMap.remove(listener.getClass());

        if (topicListener != null) {
            this.unlisten(contentType.getName(), topicListener);
        }
    }
}
