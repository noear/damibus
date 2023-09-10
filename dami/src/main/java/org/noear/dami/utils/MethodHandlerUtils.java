package org.noear.dami.utils;

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
     * java16+ 支持调用default method的方法
     */
    private static Method invokeDefaultMethod = null;

    static {
        //
        //JDK16+ 新增InvocationHandler.invokeDefault()
        //
        if (JavaUtils.JAVA_MAJOR_VERSION >= 16) {
            // https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8253870
            Method[] ms = InvocationHandler.class.getMethods();

            for (Method call : ms) {
                if ("invokeDefault".equals(call.getName())) {
                    invokeDefaultMethod = call;
                    break;
                }
            }
            if (invokeDefaultMethod == null) {
                //不可能发生
                throw new UnsupportedOperationException("The current java " + JavaUtils.JAVA_MAJOR_VERSION + " is not found: invokeDefault");
            }
        }
    }

    /**
     * 在代理模式下调用接口的默认的函数
     */
    public static Object invokeDefault(Object proxy, Method method, Object[] args) throws Throwable {
        if (JavaUtils.JAVA_MAJOR_VERSION <= 15) {
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

    /**
     * 在代理模式下调用 Object 的默认的函数
     */
    public static Object invokeObject(Class<?> interfaceClz, Object proxy, Method method, Object[] args) {
        String name = method.getName();

        switch (name) {
            case "toString":
                return interfaceClz.getName() + ".$Proxy";
            case "hashCode":
                return System.identityHashCode(proxy);
            case "equals":
                return proxy == args[0];
            default:
                throw new UnsupportedOperationException("Unsupported operation: " + interfaceClz.getName() + "::" + method.getName());
        }
    }
}
