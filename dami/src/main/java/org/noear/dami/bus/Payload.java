package org.noear.dami.bus;

/**
 * 事件负载
 *
 * @author noear
 * @since 1.0
 */
public interface Payload<C, R> {
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
     * 是否为请求（是的话，需要答复）
     */
    boolean isRequest();

    /**
     * 答复
     *
     * @param content 内容
     * @return 答复成功
     */
    boolean reply(final R content);

    /**
     * 唯一标识
     */
    String getGuid();

    /**
     * 主题
     */
    String getTopic();

    /**
     * 内容
     */
    C getContent();
}
