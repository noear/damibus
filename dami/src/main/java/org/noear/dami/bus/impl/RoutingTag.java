package org.noear.dami.bus.impl;

import org.noear.dami.bus.Payload;
import org.noear.dami.bus.TopicListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 监听路由记录
 */
public class RoutingTag<C, R> extends RoutingBase<C, R> {
    private final List<String> tags;
    private final String topic;

    /**
     * @param expr     表达式（* 表示一段，** 表示不限段）
     * @param index    顺序位
     * @param listener 监听器
     */
    public RoutingTag(String expr, int index, TopicListener<Payload<C, R>> listener) {
        super(expr, index, listener);
        List<String> exprList = parseExpr(expr);
        this.topic = exprList.get(0);
        if (exprList.size() == 1) {
            this.tags = Collections.emptyList();
        } else {
            this.tags = exprList.subList(1, exprList.size());
        }
    }

    /**
     * 匹配
     *
     * @param sendTopic 消息发送的主题表达式
     */
    public boolean matches(String sendTopic) {
        if (super.matches(sendTopic)) {
            return true;
        }

        List<String> exprList = parseExpr(sendTopic);
        List<String> sendTags = exprList.subList(1, exprList.size());

        boolean topicMatch = exprList.get(0).equals(this.topic);

        boolean hasTags = tags.isEmpty() || sendTags.isEmpty();
        //发送和监听一方有tags则需要匹配
        boolean tagMatch = hasTags || sendTags.stream().anyMatch(this.tags::contains);

        return topicMatch && tagMatch;
    }

    private List<String> parseExpr(String expr) {
        List<String> parseList = new ArrayList<>();
        //处理只写了topic情况
        if (expr.contains(":") == false) {
            parseList.add(expr);
            return parseList;
        }
        String[] topicSplit = expr.split(":", 2);
        parseList.add(topicSplit[0]);

        //处理"topic:"情况
        if (topicSplit[1].isEmpty()) {
            return parseList;
        }

        String[] tagSplit = topicSplit[1].split(",");

        parseList.addAll(Arrays.asList(tagSplit));

        return parseList;
    }
}