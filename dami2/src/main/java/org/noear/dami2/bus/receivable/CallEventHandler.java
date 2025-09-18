package org.noear.dami2.bus.receivable;

import org.noear.dami2.bus.Event;

import java.util.concurrent.CompletableFuture;

/**
 *
 * @author noear
 * @since 2.0
 */
@FunctionalInterface
public interface CallEventHandler<C,R> {
    /**
     * 处理调用事件（由 onEvent 转发简化）
     *
     * @param event    事件
     * @param content  荷载内容
     * @param receiver 荷载接收器
     */
    void onCall(Event<CallPayload<C, R>> event, C content, CompletableFuture<R> receiver);
}
