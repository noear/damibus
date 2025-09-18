package org.noear.dami2.bus.receivable;

import org.noear.dami2.bus.Event;
import org.reactivestreams.Subscriber;

/**
 *
 * @author noear 2025/9/18 created
 *
 */
@FunctionalInterface
public interface StreamEventHandler<C,R> {
    /**
     * 处理流事件（由 onEvent 转发简化）
     *
     * @param event    事件
     * @param content  荷载内容
     * @param receiver 荷载接收器
     */
    void onStream(Event<StreamPayload<C, R>> event, boolean isStream, C content, Subscriber<R> receiver);
}
