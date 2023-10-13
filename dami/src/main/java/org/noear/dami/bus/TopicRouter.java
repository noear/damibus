package org.noear.dami.bus;

import java.util.List;

/**
 * 主题路由器
 *
 * @param <C>
 * @param <R>
 * @author kongweiguang
 */
public interface TopicRouter<C, R> {
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
     * 路由匹配
     * */
    List<TopicListenerHolder<C, R>> matching(final  String topic);
}