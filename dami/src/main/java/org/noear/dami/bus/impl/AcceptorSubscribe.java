package org.noear.dami.bus.impl;

import org.noear.dami.bus.Acceptor;

import java.util.function.Consumer;

/**
 * 请求接收器
 *
 * @author noear
 * @since 1.0
 */
public class AcceptorSubscribe<R> implements Acceptor<R> {
    private final Consumer<R> future;
    public AcceptorSubscribe(Consumer<R> future){
        this.future = future;
    }

    @Override
    public boolean isSingle() {
        return false;
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
