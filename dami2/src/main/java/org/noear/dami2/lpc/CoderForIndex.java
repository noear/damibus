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
 * 参数序位对齐编码器
 *
 * @author noear
 * @since 2.0
 */
public class CoderForIndex implements Coder {
    /**
     * 编码
     *
     * @param method 方法
     * @param args   方法参数
     * @return 荷载数据
     */
    @Override
    public Object encode(Method method, Object[] args) {
        return args;
    }

    /**
     * 解码
     *
     * @param method 方法
     * @param event  事件
     * @return 方法参数
     */
    @Override
    public Object[] decode(Method method, Event<CallPayload> event) {
        Object[] argSource = (Object[]) event.getPayload().getData();

        //构建执行参数（可以与发送者的参数，略有不同）
        Object[] args = new Object[method.getParameterCount()];

        for (int i = 0, len = method.getParameterCount(); i < len; i++) {
            if (i < argSource.length) {
                args[i] = argSource[i];
            } else {
                args[i] = null;
            }
        }

        return args;
    }
}
