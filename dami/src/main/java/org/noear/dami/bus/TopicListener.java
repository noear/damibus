package org.noear.dami.bus;

/**
 * 主题监听器
 *
 * @author noear
 * @since 1.0
 */
@FunctionalInterface
public interface TopicListener<Event extends Payload> {
    /**
     * 处理监听事件
     *
     * @param event 事件
     */
    void onEvent(final Event event) throws Throwable;
}