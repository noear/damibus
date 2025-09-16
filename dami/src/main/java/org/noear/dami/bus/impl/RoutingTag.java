package org.noear.dami.bus.impl;

import org.noear.dami.bus.Message;
import org.noear.dami.bus.TopicListener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监听路由记录（tag 模式）
 *
 * @author kamosama
 * @since 1.0
 */
public class RoutingTag<C> extends Routing<C> {
    private final List<String> tags;
    private final String topic;

    /**
     * @param expr     表达式（: 为主题与标签的间隔符；,为标签的间隔符）
     * @param index    顺序位
     * @param listener 监听器
     */
    public RoutingTag(String expr, int index, TopicListener<Message<C>> listener) {
        super(expr, index, listener);

        List<String> exprList = TopicTags.get(expr);
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
     * @param sentTopic 发送的主题
     */
    public boolean matches(String sentTopic) {
        if (super.matches(sentTopic)) {
            return true;
        }

        List<String> exprList = TopicTags.get(sentTopic);
        List<String> sendTags = exprList.subList(1, exprList.size());

        boolean topicMatch = exprList.get(0).equals(this.topic);

        boolean notHasTags = tags.isEmpty() || sendTags.isEmpty();
        //发送和监听双方都有tags则需要匹配
        boolean tagMatch = notHasTags || sendTags.stream().anyMatch(this.tags::contains);

        return topicMatch && tagMatch;
    }

    static class TopicTags {
        private static final Map<String, List<String>> cached = new ConcurrentHashMap<>();

        /**
         * 获取 TopicTags
         */
        public static List<String> get(String expr) {
            return cached.computeIfAbsent(expr, TopicTags::parse);
        }

        /**
         * 解析表达式
         * @return List<String> 第一个元素为topic，剩下的为tag
         */
        private static List<String> parse(String expr) {
            List<String> parseList = new ArrayList<>();

            //处理只写了topic情况
            if (expr.contains(":") == false) {
                parseList.add(expr);
                return parseList;
            }

            //topic:tag1,tag2,tag3 以 : 切割为两部分
            String[] topicSplit = expr.split(":", 2);
            parseList.add(topicSplit[0]);

            //处理"topic:"情况
            if (topicSplit[1].isEmpty()) {
                return parseList;
            }
            //tag1,tag2,tag3 以 , 切割数组作为tags
            String[] tagSplit = topicSplit[1].split(",");
            parseList.addAll(Arrays.asList(tagSplit));

            return parseList;
        }
    }
}