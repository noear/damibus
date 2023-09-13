package org.noear.dami.bus.impl;

/**
 * 答复
 *
 * @author noear
 * @since 1.0
 */
public interface Reply<R> {
    /**
     * 是否结束接收
     * */
    boolean isDone();

    /**
     * 接收
     * */
    void accept(R value);
}
