package org.noear.dami.bus.impl;

import java.util.function.Consumer;

/**
 * 回调答复接收人
 *
 * @author noear
 * @since 1.0
 */
public class AcceptorCallback<R> implements Acceptor<R> {
    private final Consumer<R> future;
    public AcceptorCallback(Consumer<R> future){
        this.future = future;
    }
    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean accept(R value) {
        future.accept(value);
        return true;
    }
}
