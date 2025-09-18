package org.noear.dami.bus.receivable;

import java.util.concurrent.CompletableFuture;

/**
 * 调用可接收荷载
 *
 * @author noear
 * @since 2.0
 */
public class CallPayload<C,R> extends ReceivablePayload<C, CompletableFuture<R>> {
    /**
     * @param context 荷载内容
     */
    public CallPayload(C context) {
        super(context, new CompletableFuture<>());
    }

    /**
     * @param context  荷载内容
     * @param receiver 接收器
     */
    public CallPayload(C context, CompletableFuture<R> receiver) {
        super(context, receiver);
    }

    /**
     * 接收器
     */
    @Override
    public CompletableFuture<R> getReceiver() {
        return super.getReceiver();
    }
}