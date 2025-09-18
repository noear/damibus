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
package org.noear.dami.bus.impl;

import org.noear.dami.bus.Event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 事件实体实现
 *
 * @author noear
 * @since 1.0
 * @since 2.0
 */
public class EventDefault<P> implements Event<P>, Serializable {
    private final String topic;
    private final P payload;

    //附件
    private Map<String, Object> attachments;
    //处理标识
    private boolean handled;

    /**
     * @param topic 主题
     * @param payload 荷载
     * */
    public EventDefault(final String topic, final P payload) {
        this.topic = topic;
        this.payload = payload;
    }

    /**
     * 获取附件
     *
     * @param key 关键字
     */
    @Override
    public <T> T getAttachment(String key) {
        if (attachments == null) {
            return null;
        }

        return (T) attachments.get(key);
    }

    /**
     * 设置附件
     *
     * @param key   关键字
     * @param value 值
     */
    @Override
    public <T> void setAttachment(String key, T value) {
        if (attachments == null) {
            attachments = new HashMap<>();
        }

        attachments.put(key, value);
    }

    @Override
    public void setHandled() {
        this.handled = true;
    }

    @Override
    public boolean getHandled() {
        return this.handled;
    }

    /**
     * 主题
     */
    @Override
    public String getTopic() {
        return topic;
    }

    /**
     * 荷载
     */
    @Override
    public P getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "Event{" +
                ", topic='" + topic + '\'' +
                ", payload=" + payload +
                ", attachments=" + attachments +
                '}';
    }
}
