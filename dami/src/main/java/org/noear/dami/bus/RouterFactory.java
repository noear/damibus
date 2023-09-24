package org.noear.dami.bus;

import org.noear.dami.bus.impl.RoutingBase;

public interface RouterFactory<C, R> {
    RoutingBase<C, R> create(final String topic, final int index, final TopicListener<Payload<C, R>> listener);
}