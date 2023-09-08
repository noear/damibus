package org.noear.dami;

/**
 * 主题监听器
 *
 * @author noear
 * @since 1.0
 */
public interface TopicListener<Event> {
    void onEvent(Event event) throws Throwable;
}