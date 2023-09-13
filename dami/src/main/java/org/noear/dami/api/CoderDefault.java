package org.noear.dami.api;

import org.noear.dami.bus.Payload;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 编码器默认实现（Map）
 *
 * @author noear
 * @since 1.0
 */
public class CoderDefault implements Coder {
    /**
     * 编码
     *
     * @param method 方法
     * @param args   参数
     * @return 负载内容
     */
    @Override
    public Object encode(Method method, Object[] args) {
        Map<String, Object> map = new LinkedHashMap<>();

        //构建内容
        if (method.getParameterCount() > 0) {
            Parameter[] parameters = method.getParameters();
            for (int i = 0, len = method.getParameterCount(); i < len; i++) {
                map.put(parameters[i].getName(), args[i]);
            }
        }

        return map;
    }

    /**
     * 解码
     *
     * @param method  方法
     * @param payload 负载
     * @return 方法参数
     */
    @Override
    public Object[] decode(Method method, Payload payload) {
        Map<String, Object> map = (Map<String, Object>) payload.getContent();

        //构建执行参数（可以与发送者的参数，略有不同）
        Object[] args = new Object[method.getParameterCount()];
        Parameter[] parameters = method.getParameters();

        for (int i = 0, len = method.getParameterCount(); i < len; i++) {
            Parameter p1 = parameters[i];
            if (Payload.class.isAssignableFrom(p1.getType())) {
                args[i] = payload;
            } else {
                args[i] = map.get(p1.getName());
            }
        }

        return args;
    }
}
