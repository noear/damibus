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
package org.noear.dami.bus;

/**
 * 主题监听记录（构建主题与监听器关联）
 *
 * @author noear
 * @since 1.0
 * @since 2.0
 */
public class TopicListenRecord<L extends TopicListener> {
    private String topic;
    private L listener;

    public TopicListenRecord(String topic, L listener) {
        this.topic = topic;
        this.listener = listener;
    }

    /**
     * 主题
     */
    public String getTopic() {
        return topic;
    }

    /**
     * 方法主题监听器
     */
    public L getListener() {
        return listener;
    }
}
