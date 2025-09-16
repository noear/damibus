package org.noear.dami.bus.impl;

import org.noear.dami.bus.Message;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.bus.TopicListenerHolder;

/**
 * 路由记录
 *
 * @author noear
 * @since 1.0
 * */
public class Routing<C> extends TopicListenerHolder<C> {
    private final String expr;

    /**
     * @param expr     监听主题表达式
     * @param index    顺序位
     * @param listener 监听器
     */
    public Routing(String expr, int index, TopicListener<Message<C>> listener) {
        super(index, listener);
        this.expr = expr;
    }

    /**
     * 匹配
     *
     * @param sentTopic 发送的主题
     */
    public boolean matches(String sentTopic) {
        return this.getExpr().equals(sentTopic);
    }

    /**
     * 获取表达式
     */
    public String getExpr() {
        return expr;
    }
}
