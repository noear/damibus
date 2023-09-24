package org.noear.dami.bus.impl;

import org.noear.dami.bus.Payload;
import org.noear.dami.bus.TopicListener;

/**
 * 路由选择工厂
 *
 * @author kamosama
 * @since 1.0
 * */
public interface RoutingFactory<C, R> {
    /**
     * 创建路由记录
     *
     * @param topic    主题
     * @param index    顺序位
     * @param listener 监听器
     */
    Routing<C, R> create(final String topic, final int index, final TopicListener<Payload<C, R>> listener);
}