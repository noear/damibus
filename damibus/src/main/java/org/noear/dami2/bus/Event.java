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

/**
 * 事件实体
 *
 * @author noear
 * @since 1.0
 * @since 2.0
 */
public interface Event<P> extends Result<P>, Serializable {
    /**
     * 获取附件
     *
     * @param key 关键字
     */
    <T> T getAttachment(String key);

    /**
     * 设置附件
     *
     * @param key   关键字
     * @param value 值
     */
    <T> void setAttachment(String key, T value);

    /**
     * 设置处理标识
     */
    void setHandled();

    /**
     * 获取处理标识（是否已处理）
     */
    boolean getHandled();

    /**
     * 主题
     */
    String getTopic();

    /**
     * 荷载
     */
    P getPayload();
}
