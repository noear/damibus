package org.noear.dami.exception;


/**
 * Dami 专属异常
 *
 * @author Sorghum
 * @since 1.0
 * */
public class DamiException extends RuntimeException {
    public DamiException(String message) {
        super(message);
    }

    public DamiException(Throwable cause) {
        super(cause);
    }
}
