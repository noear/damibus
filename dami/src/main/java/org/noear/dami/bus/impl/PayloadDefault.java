package org.noear.dami.bus.impl;

import org.noear.dami.bus.Acceptor;
import org.noear.dami.bus.Payload;
import org.noear.dami.exception.DamiException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 事件负载本地实现
 *
 * @author noear
 * @since 1.0
 */
public class PayloadDefault<C, R> implements Payload<C, R>, Serializable {
    private final String plid;
    private final String topic;
    private final C content;

    //附件
    private Map<String, Object> attachments;
    //处理标识
    private boolean handled;
    //答复接收器
    private final transient Acceptor<R> acceptor;

    /**
     * @param topic 主题
     * @param content 内容
     * @param acceptor 答复接收器
     * */
    public PayloadDefault(final  String plid, final String topic, final C content, Acceptor<R> acceptor) {
        this.plid = plid;
        this.topic = topic;
        this.content = content;
        this.acceptor = acceptor;
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
     * 是否为请求（是的话，需要答复）
     */
    @Override
    public boolean isRequest() {
        if (acceptor != null) {
            return acceptor.isSingle();
        } else {
            return false;
        }
    }

    /**
     * @deprecated 1.1.0 （简化模式，不再支持流调用）
     * */
    @Deprecated
    @Override
    public boolean isSubscribe() {
        if (acceptor != null) {
            return !acceptor.isSingle();
        } else {
            return false;
        }
    }


    /**
     * 答复
     *
     * @param content 内容
     */
    @Override
    public boolean reply(final R content) {
        if (acceptor == null) {
            throw new DamiException("This payload does not support a reply");
        }

        if (acceptor.isDone()) {
            return false;
        }

        return acceptor.accept(content);
    }


    /**
     * 唯一标识
     */
    @Override
    public String getPlid() {
        return plid;
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
                "plid='" + plid + '\'' +
                ", topic='" + topic + '\'' +
                ", content=" + content +
                ", attachments=" + attachments +
                '}';
    }
}
