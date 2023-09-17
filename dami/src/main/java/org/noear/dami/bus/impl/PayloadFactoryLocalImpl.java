package org.noear.dami.bus.impl;

import org.noear.dami.bus.Acceptor;
import org.noear.dami.bus.Payload;
import org.noear.dami.bus.PayloadFactory;

/**
 * 事件负载工厂本地实现
 *
 * @author noear
 * @since 1.0
 */
public class PayloadFactoryLocalImpl<C, R> implements PayloadFactory<C, R> {
    @Override
    public Payload<C, R> create(String topic, C content, Acceptor<R> acceptor) {
        return new PayloadLocalImpl<>(topic, content, acceptor);
    }
}
