package org.noear.dami.bus.impl;

/**
 * 答复接收人
 *
 * @author noear
 * @since 1.0
 */
public interface Acceptor<R> {
    /**
     * 是否结束接收
     * */
    boolean isDone();

    /**
     * 接收
     * */
    boolean accept(R value);
}