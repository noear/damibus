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

        String topic = topicMapping + "." + method.getName();
        Object content = damiApi.getCoder().encode(method, args);

        if (method.getReturnType() == Void.class) {
            damiApi.getBus().send(topic, content);
            return null;
        } else {
            return damiApi.getBus().requestAndResponse(topic, content);
        }
    }
}
