package org.noear.dami.api.impl;

import org.noear.dami.api.DamiApi;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.bus.Message;

import java.lang.reflect.Method;

/**
 * 方法主题监听器
 *
 * @author noear
 * @since 1.0
 */
public class MethodTopicListener implements TopicListener<Message<Object,Object>> {
    private DamiApi damiApi;
    private Object target;
    private Method method;

    public MethodTopicListener(DamiApi damiApi, Object target, Method method) {
        this.damiApi = damiApi;
        this.target = target;
        this.method = method;
    }

    @Override
    public void onEvent(Message message) throws Throwable {
        //解码
        Object[] args = damiApi.coder().decode(method, message);

        //执行
        Object rst = method.invoke(target, args);

        if (message.requiredReply()) {
            //答复
            message.reply(rst);
        }
    }

    @Override
    public String toString() {
        return target.getClass().getName() + "::" + method.getName();
    }
}