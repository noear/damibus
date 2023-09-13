package org.noear.dami.bus.plus;

import org.noear.dami.bus.TopicListener;
import org.noear.dami.bus.Payload;

/**
 * 主题监听桥接器
 *
 * @author noear
 * @since 1.0
 */
public class TopicListenerBridge<C,R> implements TopicListener<Payload<C,R>> {
    TopicContentListener contentListener;

    public TopicListenerBridge(TopicContentListener contentListener) {
        this.contentListener = contentListener;
    }

    @Override
    public void onEvent(Payload<C, R> payload) throws Throwable {
        contentListener.onEventContent(payload.getContent());
    }
}
