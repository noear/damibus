/*
 * Copyright 2023～ noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.noear.dami.api.impl;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 函数代理工具（用于默认函数的处理跨版本）
 *
 * @author noear
 * @since 1.0
 */
public class MethodHandlerUtils {
    /**
     * Java 版本号
     */
    public static final int JAVA_MAJOR_VERSION;

    /**
     * java16+ 支持调用default method的方法
     */
    private static Method invokeDefaultMethod = null;

    static {
        /*
         * 获取 Java 版本号
         * http://openjdk.java.net/jeps/223
         * 1.8.x  = 8
         * 11.x   = 11
         * 17.x   = 17
         */
        int majorVersion;
        try {
            String version = System.getProperty("java.specification.version");
            if (version.startsWith("1.")) {
                version = version.substring(2);
            }
            majorVersion = Integer.parseInt(version);
        } catch (Throwable ignored) {
            majorVersion = 8;
        }
        JAVA_MAJOR_VERSION = majorVersion;

        /**
         * JDK16+ 新增InvocationHandler.invokeDefault()
         * */
        if (JAVA_MAJOR_VERSION >= 16) {
            // https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8253870
            Method[] ms = InvocationHandler.class.getMethods();

            for (Method call : ms) {
                if ("invokeDefault".equals(call.getName())) {
                    invokeDefaultMethod = call;
                    break;
                }
            }
            if (invokeDefaultMethod == null) {
                //应该不可能发生
                throw new UnsupportedOperationException("The current java " + JAVA_MAJOR_VERSION + " is not found: invokeDefault");
            }
        }
    }

    /**
     * 在代理模式下调用接口的默认的函数
     */
    public static Object invokeDefault(Object proxy, Method method, Object[] args) throws Throwable {
        if (JAVA_MAJOR_VERSION <= 15) {
            final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
                    .getDeclaredConstructor(Class.class);
            constructor.setAccessible(true);

            final Class<?> clazz = method.getDeclaringClass();
            return constructor.newInstance(clazz)
                    .in(clazz)
                    .unreflectSpecial(method, clazz)
                    .bindTo(proxy)
                    .invokeWithArguments(args);
        } else {
            Method invoke = invokeDefaultMethod;
            return invoke.invoke(null, proxy, method, args);
        }
    }
}
