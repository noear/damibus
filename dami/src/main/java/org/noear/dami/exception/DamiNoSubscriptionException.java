package org.noear.dami.exception;


/**
 * Dami 无订阅异常
 *
 * @author Sorghum
 * @since 1.0
 * */
public class DamiNoSubscriptionException extends DamiException {
    public DamiNoSubscriptionException(String message) {
        super(message);
    }

    public DamiNoSubscriptionException(Throwable cause) {
        super(cause);
    }
}
