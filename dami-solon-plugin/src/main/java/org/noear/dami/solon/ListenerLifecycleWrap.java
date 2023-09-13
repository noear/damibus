package org.noear.dami.solon;

import org.noear.dami.Dami;
import org.noear.dami.bus.Payload;
import org.noear.dami.bus.TopicListener;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.bean.LifecycleBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 监听器的生命周期包装
 *
 * @author noear
 * @since 1.0
 */
public class ListenerLifecycleWrap implements LifecycleBean {
    List<ListenerRecord> listenerRecords = new ArrayList<>();

    public void add(String topicMapping, Object listener) {
        listenerRecords.add(new ListenerRecord(topicMapping, listener));
    }

    @Override
    public void start() throws Throwable {

    }

    @Override
    public void stop() throws Throwable {
        //停止时自动注销
        for (ListenerRecord r1 : listenerRecords) {
            if (r1.getListenerObj() instanceof TopicListener) {
                Dami.bus().unlisten(r1.getTopicMapping(), (TopicListener<Payload<Object, Object>>) r1.getListenerObj());
            } else {
                Dami.api().unregisterListener(r1.getTopicMapping(), r1.getListenerObj());
            }
        }
    }

    /**
     * 获取实例
     */
    public static ListenerLifecycleWrap getOf(AppContext context) {
        ListenerLifecycleWrap lifecycleWrap = (ListenerLifecycleWrap) context.getAttrs().get(ListenerLifecycleWrap.class);

        if (lifecycleWrap == null) {
            lifecycleWrap = new ListenerLifecycleWrap();
            context.getAttrs().put(ListenerLifecycleWrap.class, lifecycleWrap);
            context.lifecycle(lifecycleWrap);
        }

        return lifecycleWrap;
    }
}
