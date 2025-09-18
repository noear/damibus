package org.noear.dami2.bus.receivable;

import org.reactivestreams.Subscriber;

/**
 * 流可接收荷载
 *
 * @author noear
 * @since 2.0
 */
public class StreamPayload<C,R> extends ReceivablePayload<C, Subscriber<R>> {
    /**
     * @param context  荷载内容
     * @param receiver 接收器
     *
     */
    public StreamPayload(C context, Subscriber<R> receiver) {
        super(context, receiver);
    }

    /**
     * 接收器
     */
    @Override
    public Subscriber<R> getReceiver() {
        return super.getReceiver();
    }
}
