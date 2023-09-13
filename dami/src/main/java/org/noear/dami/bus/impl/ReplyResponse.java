package org.noear.dami.bus.impl;

import java.util.concurrent.CompletableFuture;

/**
 * 响应签复
 *
 * @author noear
 * @since 1.0
 */
public class ReplyResponse<R> implements Reply<R> {
    CompletableFuture<R> future;

    public ReplyResponse(CompletableFuture<R> future) {
        this.future = future;
    }

    @Override
    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public void accept(R value) {
        future.complete(value);
    }
}
