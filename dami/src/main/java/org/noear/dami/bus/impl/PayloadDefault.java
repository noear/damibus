package org.noear.dami.bus.impl;

import org.noear.dami.bus.Message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 事件负载本地实现
 *
 * @author noear
 * @since 1.0
 */
public class PayloadDefault<C> implements Message<C>, Serializable {
    private final String topic;
    private final C content;

    //附件
    private Map<String, Object> attachments;
    //处理标识
    private boolean handled;

    /**
     * @param topic 主题
     * @param content 内容
     * */
    public PayloadDefault(final String topic, final C content) {
        this.topic = topic;
        this.content = content;
    }

    /**
     * 获取附件
     *
     * @param key 关键字
     */
    @Override
    public <T> T getAttachment(String key) {
        if (attachments == null) {
            return null;
        }

        return (T) attachments.get(key);
    }

    /**
     * 设置附件
     *
     * @param key   关键字
     * @param value 值
     */
    @Override
    public <T> void setAttachment(String key, T value) {
        if (attachments == null) {
            attachments = new HashMap<>();
        }

        attachments.put(key, value);
    }

    @Override
    public void setHandled() {
        this.handled = true;
    }

    @Override
    public boolean getHandled() {
        return this.handled;
    }

    /**
     * 主题
     */
    @Override
    public String getTopic() {
        return topic;
    }

    /**
     * 内容
     */
    @Override
    public C getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Payload{" +
                ", topic='" + topic + '\'' +
                ", content=" + content +
                ", attachments=" + attachments +
                '}';
    }
}
