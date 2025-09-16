package org.noear.dami.bus;

import java.io.Serializable;

/**
 * 事件负载
 *
 * @author noear
 * @since 1.0
 */
public interface Message<C> extends Serializable {
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
     * 主题
     */
    String getTopic();

    /**
     * 内容
     */
    C getContent();
}
