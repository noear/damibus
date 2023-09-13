package org.noear.dami.bus.plus;

/**
 * 主题内容监听
 *
 * @author noear
 * @since 1.0
 */
public interface TopicContentListener<C> {
    void onEventContent(final C content) throws Throwable;
}
