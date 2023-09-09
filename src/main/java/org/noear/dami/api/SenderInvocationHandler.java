package org.noear.dami.api;

import org.noear.dami.bus.DamiBus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 发送者接口的调用代理
 *
 * @author noear
 * @since 1.0
 */
public class SenderInvocationHandler implements InvocationHandler {
    private DamiBus bus;
    private String topicMapping;
    private Coder coder;

    public SenderInvocationHandler(DamiBus bus, String topicMapping, Coder coder) {
        this.bus = bus;
        this.topicMapping = topicMapping;
        this.coder = coder;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String topic = topicMapping + "." + method.getName();
        Object content = coder.encode(method, args);

        if (method.getReturnType() == Void.class) {
            bus.send(topic, content);
            return null;
        } else {
            return bus.requestAndResponse(topic, content);
        }
    }
}
