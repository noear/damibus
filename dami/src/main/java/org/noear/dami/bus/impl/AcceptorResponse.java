package org.noear.dami.bus.impl;

import java.util.concurrent.CompletableFuture;

/**
 * 响应签复接收人
 *
 * @author noear
 * @since 1.0
 */
public class AcceptorResponse<R> implements Acceptor<R> {
    CompletableFuture<R> future;

    public AcceptorResponse(CompletableFuture<R> future) {
        this.future = future;
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
