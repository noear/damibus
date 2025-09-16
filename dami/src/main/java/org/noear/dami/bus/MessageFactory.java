package org.noear.dami.bus;

/**
 * 事件负载工厂
 *
 * @author noear
 * @since 1.0
 */
public interface MessageFactory<C> {
    /**
     * 创建
     *
     * @param topic    主题
     * @param content  内容
     */
    Message<C> create(final String topic, final C content);
}
