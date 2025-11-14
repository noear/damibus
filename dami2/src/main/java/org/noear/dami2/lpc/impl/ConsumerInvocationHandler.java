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
import org.noear.dami2.bus.Result;
import org.noear.dami2.exception.DamiNoListenException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

/**
 * 消费者的调用代理
 *
 * @author noear
 * @since 1.0
 */
public class ConsumerInvocationHandler implements InvocationHandler {
    private final DamiLpc damiApi;
    private Class<?> interfaceClz;
    private final String topicMapping;

    public ConsumerInvocationHandler(DamiLpc damiApi, Class<?> interfaceClz, String topicMapping) {
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
        Object data = damiApi.coder().encode(method, args); //

        Object result = null;


        Result<CallPayload<Object, Object>> event = damiApi.bus().callAsResult(topic, data, null);

        if (method.getReturnType() == void.class) { //不能用大写的 Void.class（不然对不上）
            //无返回结果
            if (event.getHandled()) {
                if (event.getPayload().getSink().isCompletedExceptionally()) {
                    try {
                        event.getPayload().getSink().get();
                    } catch (ExecutionException e) {
                        throw e.getCause();
                    }
                }
            } else {
                if (method.isDefault()) {
                    //如果没有订阅，且有默认实现
                    MethodHandlerUtils.invokeDefault(proxy, method, args);
                }
            }
        } else {
            //有返回结果
            if (event.getHandled()) {
                try {
                    result = event.getPayload().getSink().get();
                } catch (ExecutionException e) {
                    throw e.getCause();
                }
            } else {
                if (method.isDefault()) {
                    //如果没有订阅，且有默认实现（相当于降级处理）
                    result = MethodHandlerUtils.invokeDefault(proxy, method, args);
                } else {
                    //如果没有默认实现；给出异常提醒
                    throw new DamiNoListenException("No listen implementation");
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
        return "ConsumerInvocationHandler{" +
                "interfaceClz=" + interfaceClz +
                ", topicMapping='" + topicMapping + '\'' +
                '}';
    }
}