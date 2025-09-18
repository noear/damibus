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
package org.noear.dami2.bus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 事件默认实现
 *
 * @author noear
 * @since 1.0
 * @since 2.0
 */
public class EventDefault<P> implements Event<P>, Serializable {
    private final String topic;
    private final P payload;

    //附件
    private Map<String, Object> attach;
    //处理标识
    private boolean handled;

    /**
     * @param topic   主题
     * @param payload 荷载
     *
     */
    public EventDefault(final String topic, final P payload) {
        this(topic, payload, null);
    }

    /**
     * @param topic   主题
     * @param payload 荷载
     *
     */
    public EventDefault(final String topic, final P payload, final Map<String, Object> attach) {
        this.topic = topic;
        this.payload = payload;
        this.attach = attach;
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
     * 获取附件
     */
    @Override
    public Map<String, Object> getAttach() {
        if (attach == null) {
            attach = new HashMap<>();
        }

        return attach;
    }

    /**
     * 获取主题
     */
    @Override
    public String getTopic() {
        return topic;
    }

    /**
     * 获取荷载
     */
    @Override
    public P getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "Event{" +
                "topic='" + topic + '\'' +
                ", payload=" + payload +
                ", attach=" + attach +
                '}';
    }
}
