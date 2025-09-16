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
package org.noear.dami.api.impl;

import org.noear.dami.api.DamiApi;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.bus.Message;
import org.noear.dami.bus.payload.RequestPayload;

import java.lang.reflect.Method;

/**
 * 方法主题监听器
 *
 * @author noear
 * @since 1.0
 */
public class MethodTopicListener implements TopicListener<Message<RequestPayload>> {
    private DamiApi damiApi;
    private Object target;
    private Method method;

    public MethodTopicListener(DamiApi damiApi, Object target, Method method) {
        this.damiApi = damiApi;
        this.target = target;
        this.method = method;
    }

    @Override
    public void onEvent(Message<RequestPayload> message) throws Throwable {
        //解码
        Object[] args = damiApi.coder().decode(method, message);

        //执行
        Object rst = method.invoke(target, args);

        message.getPayload().getResponse().complete(rst);
    }

    @Override
    public String toString() {
        return target.getClass().getName() + "::" + method.getName();
    }
}