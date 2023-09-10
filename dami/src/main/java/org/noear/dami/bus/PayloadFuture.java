package org.noear.dami.bus;

/**
 * 事件装载 Future
 *
 * @author noear
 * @since 1.0
 */
public interface PayloadFuture<T> {
    /**
     * 接受
     * */
    boolean accept(T value);
}
