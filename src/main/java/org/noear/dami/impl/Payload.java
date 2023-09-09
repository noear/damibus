package org.noear.dami.impl;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * 事件装载
 *
 * @author noear
 * @since 1.0
 */
public class Payload<C, R> implements Serializable {
    private final String guid;
    private final String topic;
    private final C content;

    private Map<String, Object> attachments;

    protected transient CompletableFuture<R> future;


    public Payload(final String topic, final C content) {
        this(UUID.randomUUID().toString(), topic, content);
    }

    public Payload(final String guid, final String topic, final C content) {
        this.guid = guid;
        this.topic = topic;
        this.content = content;
    }

    /**
     * 获取附件
     *
     * @param key 关键字
     */
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
    public <T> void setAttachment(String key, T value) {
        if (attachments == null) {
            attachments = new HashMap<>();
        }

        attachments.put(key, value);
    }

    /**
     * 是否为请求（是的话，需要响应）
     */
    public boolean isRequest() {
        return future != null;
    }

    /**
     * 唯一性id
     */
    public String getGuid() {
        return guid;
    }

    /**
     * 主题
     */
    public String getTopic() {
        return topic;
    }

    /**
     * 内容
     */
    public C getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Payload{" +
                "guid='" + guid + '\'' +
                ", topic='" + topic + '\'' +
                ", content=" + content +
                ", attachments=" + attachments +
                '}';
    }
}