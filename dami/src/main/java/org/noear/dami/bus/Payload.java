package org.noear.dami.bus;

import java.io.Serializable;

/**
 * 事件负载
 *
 * @author noear
 * @since 1.0
 */
public interface Payload<C, R> extends Serializable {
    /**
     * 获取附件
     *
     * @param key 关键字
     */
    <T> T getAttachment(String key);

    /**
     * 设置附件
     *
     * @param key   关键字
     * @param value 值
     */
    <T> void setAttachment(String key, T value);

    /**
     * 设置处理标识
     */
    void setHandled();

    /**
     * 获取处理标识
     */
    boolean getHandled();

    /**
     * 要求答复
     *
     * @since 1.1.0
     */
    boolean requiredReply();

    /**
     * 是否为请求（是，则需要答复）
     *
     * @deprecated 1.1.0 （简化模式，不再支持流调用）
     */
    @Deprecated
    boolean isRequest();

    /**
     * 是否为订阅（是，则需要答复）
     *
     * @deprecated 1.1.0 （简化模式，不再支持流调用）
     */
    @Deprecated
    boolean isSubscribe();

    /**
     * 答复
     *
     * @param content 内容
     * @return 答复成功
     */
    boolean reply(final R content);

    /**
     * 负载标识
     */
    String getPlid();

    /**
     * 主题
     */
    String getTopic();

    /**
     * 内容
     */
    C getContent();
}
