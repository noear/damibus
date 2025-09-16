package org.noear.dami.api;

import org.noear.dami.bus.Message;

import java.lang.reflect.Method;

/**
 * 编码器
 *
 * @author noear
 * @since 1.0
 */
public interface Coder {
    /**
     * 编码
     *
     * @param method 方法
     * @param args   参数
     * @return 负载内容
     */
    Object encode(Method method, Object[] args) throws Throwable;

    /**
     * 解码
     *
     * @param method  方法
     * @param message 负载
     * @return 方法参数
     */
    Object[] decode(Method method, Message message) throws Throwable;
}
