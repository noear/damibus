package org.noear.dami.api.impl;

import org.noear.dami.api.DamiApi;

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

        if (method.isDefault()) {
            if (damiApi.enableDefaultSend() == false) {
                return MethodHandlerUtils.invokeDefault(proxy, method, args);
            }
        }

        String topic = topicMapping + "." + method.getName();
        Object content = damiApi.coder().encode(method, args);

        if (method.getReturnType() == void.class) { //不能用大写的 Void.class（不然对不上）
            damiApi.bus().send(topic, content);
            return null;
        } else {
            return damiApi.bus().sendAndResponse(topic, content);
        }
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
