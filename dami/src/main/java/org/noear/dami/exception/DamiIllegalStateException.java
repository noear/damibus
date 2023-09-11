package org.noear.dami.exception;


/**
 * Dami包装的IllegalStateException
 * <p> 方便用户捕获异常
 * @author Sorghum
 * @since 1.0
 * */
public class DamiIllegalStateException extends IllegalStateException {

    public DamiIllegalStateException(String s) {
        super(s);
    }


    public DamiIllegalStateException(Throwable cause) {
        super(cause);
    }

    /**
     * 获取根异常
     * <p>由于反射调用,直接cause可能是InvocationTargetException
     * @return 根异常
     */
    public Throwable getRootCause() {
        Throwable rootCause = this;
        Throwable cause = getCause();
        while (cause != null && cause != rootCause) {
            rootCause = cause;
            cause = cause.getCause();
        }
        return rootCause;
    }
}
