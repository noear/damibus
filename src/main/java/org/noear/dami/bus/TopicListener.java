package org.noear.dami.bus;

import java.io.Serializable;

/**
 * 主题监听器
 *
 * @author noear
 * @since 1.0
 */
@FunctionalInterface
public interface TopicListener<Event> extends Serializable {
    /**
     * 处理监听事件
     *
     * @param event 事件
     */
    void onEvent(final Event event) throws Throwable;
}