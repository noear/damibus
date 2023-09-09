package org.noear.dami.api;

import java.lang.reflect.Method;

/**
 * 编码器
 *
 * @author noear
 * @since 1.0
 */
public interface Coder {
    Object encode(Method method, Object[] args) throws Throwable;

    Object[] decode(Method method, Object content) throws Throwable;
}
