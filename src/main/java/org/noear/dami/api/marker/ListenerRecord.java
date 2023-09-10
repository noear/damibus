package org.noear.dami.api.marker;

/**
 * 监听器记录
 *
 * @author noear
 * @since 1.0
 */
public class ListenerRecord {
    private final String topicMapping;
    private final Listener listener;

    public ListenerRecord(String topicMapping, Listener listener) {
        this.topicMapping = topicMapping;
        this.listener = listener;
    }

    public String getTopicMapping() {
        return topicMapping;
    }

    public Listener getListener() {
        return listener;
    }
}
