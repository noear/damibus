package org.noear.dami.bus.impl;

import java.util.function.Consumer;

/**
 * 回调答复
 *
 * @author noear
 * @since 1.0
 */
public class ReplyCallback<R> implements Reply<R> {
    Consumer<R> future;
    public ReplyCallback(Consumer<R> future){
        this.future = future;
    }
    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public void accept(R value) {
        future.accept(value);
    }
}
