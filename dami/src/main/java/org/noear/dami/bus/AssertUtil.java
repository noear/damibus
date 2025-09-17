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

import org.noear.dami.exception.DamiException;

/**
 * @author noear
 * @since 1.0
 */
public class AssertUtil {
    /**
     * 断言不断 null
     *
     * @param obj 对象
     * @param msg 描述
     * @since 2.0
     */
    public static void notNull(Object obj, String msg) {
        if (obj == null) {
            throw new DamiException(msg);
        }
    }

    /**
     * 断言主题是否为空
     *
     * @param topic
     */
    public static void assertTopic(final String topic) {
        if (topic == null || topic.isEmpty()) {
            throw new DamiException("The topic cannot be empty");
        }
    }
}