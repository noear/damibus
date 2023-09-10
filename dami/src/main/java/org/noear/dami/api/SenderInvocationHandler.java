package org.noear.dami.api;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 发送者接口的调用代理
 *
 * @author noear
 * @since 1.0
 */
public class SenderInvocationHandler implements InvocationHandler {
    private DamiApi damiApi;
    private String topicMapping;

    public SenderInvocationHandler(DamiApi damiApi, String topicMapping) {
        this.damiApi = damiApi;
        this.topicMapping = topicMapping;
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
        Object content = damiApi.getCoder().encode(method, args);

        if (method.getReturnType() == void.class) { //不能用大写的 Void.class（不然对不上）
            damiApi.getBus().send(topic, content);
            return null;
        } else {
            return damiApi.getBus().requestAndResponse(topic, content);
        }
    }

    @Override
    public String toString() {
        return "SenderInvocationHandler{" +
                "topicMapping='" + topicMapping + '\'' +
                '}';
    }
}
