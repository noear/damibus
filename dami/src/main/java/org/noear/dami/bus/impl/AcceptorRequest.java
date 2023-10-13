package org.noear.dami.bus.impl;

import org.noear.dami.bus.Acceptor;

import java.util.concurrent.CompletableFuture;

/**
 * 订阅接收器
 *
 * @author noear
 * @since 1.0
 */
public class AcceptorRequest<R> implements Acceptor<R> {
    private final CompletableFuture<R> future;

    public AcceptorRequest(CompletableFuture<R> future) {
        this.future = future;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public boolean accept(R value) {
        return future.complete(value);
    }
}
