package org.noear.dami.api;

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
public class CoderDefault implements Coder{
    /**
     * 编码
     *
     * @param method 方法
     * @param args   参数
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
     * @param content 内容
     */
    @Override
    public Object[] decode(Method method, Object content) {
        Map<String, Object> map = (Map<String, Object>) content;

        //构建执行参数
        Object[] args = new Object[method.getParameterCount()];
        Parameter[] parameters = method.getParameters();

        for (int i = 0, len = method.getParameterCount(); i < len; i++) {
            args[i] = map.get(parameters[i].getName());
        }

        return args;
    }
}
