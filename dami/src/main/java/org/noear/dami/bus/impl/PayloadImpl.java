package org.noear.dami.bus.impl;

import org.noear.dami.bus.Payload;
import org.noear.dami.exception.DamiException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 事件负载实现
 *
 * @author noear
 * @since 1.0
 */
public class PayloadImpl<C, R> implements Payload<C, R>, Serializable {
    private final String guid;
    private final String topic;
    private final C content;

    //附件
    private Map<String, Object> attachments;
    //答复接收人
    private transient Acceptor<R> acceptor;

    public PayloadImpl(final String topic, final C content, Acceptor<R> acceptor) {
        this.guid = UUID.randomUUID().toString();
        this.topic = topic;
        this.content = content;
        this.acceptor = acceptor;
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
     * 是否为请求（是的话，需要答复）
     */
    public boolean isRequest() {
        //如果有接收人，且未结束接收
        return acceptor != null && acceptor.isDone() == false;
    }


    /**
     * 答复
     *
     * @param content 内容
     */
    public void reply(final R content) {
        if (isRequest() == false) {
            throw new DamiException("This payload does not support a reply");
        }

        if(acceptor.isDone()){
            throw new DamiException("This payload has completed the reply");
        }

        acceptor.accept(content);
    }


    /**
     * 唯一标识
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
