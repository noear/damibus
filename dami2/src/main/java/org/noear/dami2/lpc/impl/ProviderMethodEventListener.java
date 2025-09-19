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
package org.noear.dami2.lpc.impl;

import org.noear.dami2.bus.receivable.CallPayload;
import org.noear.dami2.lpc.DamiLpc;
import org.noear.dami2.bus.EventListener;
import org.noear.dami2.bus.Event;

import java.lang.reflect.Method;

/**
 * 服务的方法事件监听器
 *
 * @author noear
 * @since 1.0
 */
public class ProviderMethodEventListener implements EventListener<CallPayload> {
    private DamiLpc damiApi;
    private Object target;
    private Method method;

    public ProviderMethodEventListener(DamiLpc damiApi, Object target, Method method) {
        this.damiApi = damiApi;
        this.target = target;
        this.method = method;
    }

    @Override
    public void onEvent(Event<CallPayload> event) throws Throwable {
        //解码
        Object[] args = damiApi.coder().decode(method, event);

        //执行
        Object rst = method.invoke(target, args);

        event.getPayload().getSink().complete(rst);
    }

    @Override
    public String toString() {
        return target.getClass().getName() + "::" + method.getName();
    }
}