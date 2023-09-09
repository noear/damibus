package org.noear.dami.api;

import org.noear.dami.Dami;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 接送者接口调用代理
 *
 * @author noear
 * @since 1.0
 */
public class SenderInvocationHandler implements InvocationHandler {
    private String topicMapping;
    private Coder coder;

    public SenderInvocationHandler(String topicMapping, Coder coder) {
        this.topicMapping = topicMapping;
        this.coder = coder;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String topic = topicMapping + "." + method.getName();
        Object content = coder.encode(method, args);

        if (method.getReturnType() == Void.class) {
            Dami.objBus().send(topic, content);
            return null;
        } else {
            return Dami.objBus().requestAndResponse(topic, content);
        }
    }
}
