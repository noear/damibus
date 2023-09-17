package org.noear.dami.bus;

/**
 * 事件负载工厂
 *
 * @author noear
 * @since 1.0
 */
public interface PayloadFactory<C, R> {
    /**
     * 创建
     *
     * @param topic    主题
     * @param content  内容
     * @param acceptor 接收人
     */
    Payload<C, R> create(final String topic, final C content, Acceptor<R> acceptor);
}
