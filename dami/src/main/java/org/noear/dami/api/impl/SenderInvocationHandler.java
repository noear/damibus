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
import org.noear.dami.bus.Message;
import org.noear.dami.bus.payload.RequestPayload;
import org.noear.dami.exception.DamiNoListenException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 发送者接口的调用代理
 *
 * @author noear
 * @since 1.0
 */
public class SenderInvocationHandler implements InvocationHandler {
    private final DamiApi damiApi;
    private Class<?> interfaceClz;
    private final String topicMapping;

    public SenderInvocationHandler(DamiApi damiApi, Class<?> interfaceClz, String topicMapping) {
        this.damiApi = damiApi;
        this.interfaceClz = interfaceClz;
        this.topicMapping = topicMapping;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //暂不支持默认函数与Object函数
        if (method.getDeclaringClass().equals(Object.class)) {
            return invokeObject(proxy, method, args);
        }

        String topic = topicMapping + "." + method.getName();
        Object content = damiApi.coder().encode(method, args); //

        Object result = null;

        if (method.getReturnType() == void.class) { //不能用大写的 Void.class（不然对不上）
            if (damiApi.bus().send(topic, new RequestPayload(content)).getHandled() == false) {
                if (method.isDefault()) {
                    //如果没有订阅，且有默认实现
                    MethodHandlerUtils.invokeDefault(proxy, method, args);
                }
            }
        } else {
            Message<RequestPayload> message = damiApi.bus().send(topic, new RequestPayload(content));

            if (message.getHandled()) {
                result = message
                        .getPayload()
                        .getResponse()
                        .get();
            } else {
                if (method.isDefault()) {
                    //如果没有订阅，且有默认实现
                    result = MethodHandlerUtils.invokeDefault(proxy, method, args);
                } else {
                    //如果没有默认实现；给出异常提醒
                    throw new DamiNoListenException("No response subscription");
                }
            }
        }

        return result;
    }

    private Object invokeObject(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();

        switch (name) {
            case "toString":
                return interfaceClz.getName() + ".$Proxy{topicMapping='" + topicMapping + "'}";
            case "hashCode":
                return System.identityHashCode(proxy);
            case "equals":
                return proxy == args[0];
            default:
                return method.invoke(this, args);
        }
    }

    @Override
    public String toString() {
        return "SenderInvocationHandler{" +
                "interfaceClz=" + interfaceClz +
                ", topicMapping='" + topicMapping + '\'' +
                '}';
    }
}
