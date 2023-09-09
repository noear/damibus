package org.noear.dami.api;

import org.noear.dami.DamiBus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 接送者接口调用代理
 *
 * @author noear
 * @since 1.0
 */
public class SenderInvocationHandler implements InvocationHandler {
    String topicMapping;

    public SenderInvocationHandler(String topicMapping) {
        this.topicMapping = topicMapping;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String topic = topicMapping + "." + method.getName();
        Map<String, Object> content = new LinkedHashMap<>();

        //构建内容
        if (method.getParameterCount() > 0) {
            Parameter[] parameters = method.getParameters();
            for (int i = 0, len = method.getParameterCount(); i < len; i++) {
                content.put(parameters[i].getName(), args[i]);
            }
        }

        if (method.getReturnType() == Void.class) {
            DamiBus.obj().send(topic, content);
            return null;
        } else {
            return DamiBus.obj().requestAndResponse(topic, content);
        }
    }
}
