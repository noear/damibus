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
package org.noear.dami2.lpc;

import org.noear.dami2.bus.Event;
import org.noear.dami2.bus.receivable.CallPayload;

import java.lang.reflect.Method;

/**
 * 编码器
 *
 * @author noear
 * @since 1.0
 */
public interface Coder {
    /**
     * 编码
     *
     * @param method 方法
     * @param args   方法参数
     * @return 荷载数据
     */
    Object encode(Method method, Object[] args) throws Throwable;

    /**
     * 解码
     *
     * @param method 方法
     * @param event  事件
     * @return 方法参数
     */
    Object[] decode(Method method, Event<CallPayload> event) throws Throwable;
}
