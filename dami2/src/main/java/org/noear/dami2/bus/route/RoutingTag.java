/*
 * Copyright 2023～ noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.noear.dami2.bus.route;

import org.noear.dami2.bus.EventListener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监听路由记录（tag 模式）
 *
 * @author kamosama
 * @since 1.0
 */
public class RoutingTag<P> extends Routing<P> {
    private final TopicTags topicTags;

    /**
     * @param expr     表达式（: 为主题与标签的间隔符；,为标签的间隔符）
     * @param index    顺序位
     * @param listener 监听器
     */
    public RoutingTag(String expr, int index, EventListener<P> listener) {
        super(expr, index, listener);

        topicTags = TopicTags.get(expr);
    }

    public String getTopic() {
        return topicTags.topic;
    }

    @Override
    public boolean isPatterned() {
        return topicTags.tags.size() > 0;
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

        TopicTags sentTopicTags = TopicTags.get(sentTopic);


        boolean topicMatch = topicTags.topic.equals(sentTopicTags.topic);
        boolean notHasTags = topicTags.tags.isEmpty() || sentTopicTags.tags.isEmpty();

        //发送和监听双方都有tags则需要匹配
        boolean tagMatch = notHasTags || sentTopicTags.tags.stream().anyMatch(topicTags.tags::contains);

        return topicMatch && tagMatch;
    }


    static class TopicTags {
        public final Set<String> tags = new HashSet<>();
        public String topic;


        private static final Map<String, TopicTags> cached = new ConcurrentHashMap<>();

        /**
         * 获取 TopicTags
         */
        public static TopicTags get(String expr) {
            return cached.computeIfAbsent(expr, TopicTags::parse);
        }

        /**
         * 解析表达式
         * @return List<String> 第一个元素为topic，剩下的为tag
         */
        private static TopicTags parse(String expr) {
            TopicTags topicTags = new TopicTags();

            //处理只写了topic情况
            if (expr.indexOf(':') < 0) {
                topicTags.topic = expr;
                return topicTags;
            } else{
                //topic:tag1,tag2,tag3 以 : 切割为两部分
                String[] topicSplit = expr.split(":");
                topicTags.topic =  topicSplit[0];

                //处理"topic:"情况
                if (topicSplit.length == 1) {
                    return topicTags;
                }else {
                    //tag1,tag2,tag3 以 , 切割数组作为tags
                    String[] tagSplit = topicSplit[1].split(",");
                    topicTags.tags.addAll(Arrays.asList(tagSplit));
                    return topicTags;
                }
            }
        }
    }
}