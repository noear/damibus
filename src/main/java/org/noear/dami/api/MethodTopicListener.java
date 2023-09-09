package org.noear.dami.api;

import org.noear.dami.DamiBus;
import org.noear.dami.TopicListener;
import org.noear.dami.bus.Payload;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * @author noear
 * @since 1.0
 */
public class MethodTopicListener implements TopicListener<Payload<Object,Object>> {
    private Object target;
    private Method method;

    public MethodTopicListener(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    @Override
    public void onEvent(Payload payload) throws Throwable {
        Map<String, Object> content = (Map<String, Object>) payload.getContent();

        //构建执行参数
        Object[] args = new Object[method.getParameterCount()];
        Parameter[] parameters = method.getParameters();

        for (int i = 0, len = method.getParameterCount(); i < len; i++) {
            args[i] = content.get(parameters[i].getName());
        }

        //执行
        Object rst = method.invoke(target, args);

        if (payload.isRequest()) {
            //响应
            DamiBus.obj().response(payload, rst);
        }
    }
}
