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
    private final DamiBus bus;
    private final String topicMapping;
    private final Coder coder;

    public SenderInvocationHandler(DamiBus bus, String topicMapping, Coder coder) {
        this.bus = bus;
        this.topicMapping = topicMapping;
        this.coder = coder;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //暂不支持默认函数与Object函数
        if (method.getDeclaringClass().equals(Object.class)) {
           return method.invoke(this,args);
        }

        if (method.isDefault()){
            throw new IllegalStateException("Default functions are not currently supported");
        }

        String topic = topicMapping + "." + method.getName();
        Object content = coder.encode(method, args);

        if (method.getReturnType() == Void.class) {
            bus.send(topic, content);
            return null;
        } else {
            return bus.requestAndResponse(topic, content);
        }
    }

    @Override
    public String toString() {
        return "SenderInvocationHandler{" +
                "topicMapping='" + topicMapping + '\'' +
                '}';
    }
}
