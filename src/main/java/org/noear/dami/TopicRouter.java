package org.noear.dami;

import org.noear.dami.impl.Payload;

/**
 * topic路由接口
 *
 * @param <C>
 * @param <R>
 * @author kongweiguang
 */
public interface TopicRouter<C, R> {
    default void assertTopic(final String topic) {
        if (topic == null || topic.isEmpty()) {
            throw new IllegalArgumentException("The topic cannot be empty");
        }
    }

    /**
     * 添加监听
     */
    void add(final String topic, final int index, final TopicListener<Payload<C, R>> listener);


    /**
     * 移除监听
     */
    void remove(final String topic, final TopicListener<Payload<C, R>> listener);


    /**
     * 接收事件并路由
     */
    void handle(final Payload<C, R> payload);

}
