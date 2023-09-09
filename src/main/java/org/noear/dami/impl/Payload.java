package org.noear.dami.impl;

import java.io.Serializable;
import java.util.UUID;
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
     * 是否为请求（是的话，需要响应）
     * */
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
                ", content='" + content + '\'' +
                '}';
    }
}
