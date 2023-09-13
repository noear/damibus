package org.noear.dami.bus.plus;

/**
 * 大米总线类化版（直接使用内容类型的类全名直接做主题）
 *
 * @author noear
 * @since 1.0
 */
public interface DamiBusTyped {
    /**
     * 发送
     *
     * @param content 内容
     */
    <T> void send(T content);

    /**
     * 监听
     *
     * @param contentType 内容类型
     * @param listener    监听
     */
    default <T> void listen(Class<T> contentType, TopicContentListener<T> listener) {
        listen(contentType, 0, listener);
    }

    /**
     * 监听
     *
     * @param contentType 内容类型
     * @param index       顺序位
     * @param listener    监听器
     */
    <T> void listen(Class<T> contentType, int index, TopicContentListener<T> listener);

    /**
     * 取消监听
     *
     * @param contentType 内容类型
     * @param listener    监听器
     */
    <T> void unlisten(Class<T> contentType, TopicContentListener<T> listener);
}
