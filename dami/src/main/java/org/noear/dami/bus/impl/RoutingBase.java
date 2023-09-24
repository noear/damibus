package org.noear.dami.bus.impl;

import org.noear.dami.bus.Payload;
import org.noear.dami.bus.TopicListener;

public class RoutingBase<C, R> {
    private final TopicListener<Payload<C, R>> listener;
    private final int index;
    private final String expr;

    /**
     * @param expr     监听主题表达式
     * @param index    顺序位
     * @param listener 监听器
     */
    public RoutingBase(String expr, int index, TopicListener<Payload<C, R>> listener) {
        this.listener = listener;
        this.index = index;
        this.expr = expr;
    }

    /**
     * 匹配
     *
     * @param topicExpr 消息发送的主题表达式
     */
    public boolean matches(String topicExpr) {
        return this.getExpr().equals(topicExpr);
    }

    /**
     * 获取表达式
     */
    public String getExpr() {
        return expr;
    }

    /**
     * 获取顺序位
     */
    public int getIndex() {
        return index;
    }

    /**
     * 获取监听器
     */
    public TopicListener<Payload<C, R>> getListener() {
        return listener;
    }


}
