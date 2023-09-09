package org.noear.dami.api;

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
     */
    Object encode(Method method, Object[] args) throws Throwable;

    /**
     * 解码
     *
     * @param method  方法
     * @param content 内容
     */
    Object[] decode(Method method, Object content) throws Throwable;
}
