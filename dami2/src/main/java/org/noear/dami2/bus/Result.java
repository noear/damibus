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

import java.util.Map;
import java.util.function.Consumer;

/**
 * 发送结果
 *
 * @author noear
 * @since 2.0
 */
public interface Result<P> {
    /**
     * 获取处理标识（是否已处理）
     */
    boolean getHandled();

    /**
     * 获取附件
     */
    Map<String, Object> getAttach();

    /**
     * 主题
     */
    String getTopic();

    /**
     * 荷载
     */
    P getPayload();

    /**
     * （有结果后）然后消费
     */
    default Result<P> thenConsume(Consumer<Result<P>> consumer) {
        consumer.accept(this);
        return this;
    }
}
