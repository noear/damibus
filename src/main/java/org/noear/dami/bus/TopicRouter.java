package org.noear.dami.bus;

/**
 * 主题路由器
 *
 * @param <C>
 * @param <R>
 * @author kongweiguang
 */
public interface TopicRouter<C, R> {
    /**
     * 断言主题是否为空
     *
     * @param topic
     */
    default void assertTopic(final String topic) {
        if (topic == null || topic.isEmpty()) {
            throw new IllegalArgumentException("The topic cannot be empty");
        }
    }

    /**
     * 添加监听
     *
     * @param topic    主题
     * @param index    顺序位
     * @param listener 监听器
     */
    void add(final String topic, final int index, final TopicListener<Payload<C, R>> listener);


    /**
     * 移除监听
     *
     * @param topic    主题
     * @param listener 监听器
     */
    void remove(final String topic, final TopicListener<Payload<C, R>> listener);


    /**
     * 接收事件并路由
     *
     * @param payload 事件装载
     */
    void handle(final Payload<C, R> payload);
}
