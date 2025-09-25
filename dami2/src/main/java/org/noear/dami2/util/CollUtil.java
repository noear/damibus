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
package org.noear.dami2.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 集合工具
 *
 * @author noear
 * @since 2.0
 */
public class CollUtil {
    public static Map asMap(Object... keyValues) {
        if (keyValues.length % 2 != 0) {
            throw new IllegalArgumentException("keyValues.length % 2 != 0");
        } else {
            Map map = new LinkedHashMap(keyValues.length / 2);

            for (int i = 0; i < keyValues.length; i += 2) {
                map.put(keyValues[i], keyValues[i + 1]);
            }

            return map;
        }
    }
}
