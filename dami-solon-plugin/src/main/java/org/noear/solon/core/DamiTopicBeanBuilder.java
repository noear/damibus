package org.noear.solon.core;

import org.noear.dami.Dami;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.solon.ListenerLifecycleWrap;
import org.noear.dami.solon.annotation.DamiTopic;
import org.noear.solon.Solon;

/**
 * TopicMapping 构建器
 *
 * @author noear
 * @since 1.0
 */
public class DamiTopicBeanBuilder implements BeanBuilder<DamiTopic> {
    @Override
    public void doBuild(Class<?> clz, BeanWrap bw, DamiTopic anno) throws Throwable {
        if (clz.isInterface()) {
            Object raw = Dami.api().createSender(anno.value(), clz);
            bw.rawSet(raw);
        } else {
            if (TopicListener.class.isAssignableFrom(clz)) {
                Dami.bus().listen(anno.value(), bw.raw());
            } else {
                Dami.api().registerListener(anno.value(), bw.raw());
            }

            lifecycleWrap(bw, anno.value());
        }
    }

    /**
     * 包装生命周期
     */
    private void lifecycleWrap(BeanWrap bw, String topicMapping) {
        if (Solon.context() != bw.context()) {
            //如果不是根容器，则停止时自动注销
            ListenerLifecycleWrap lifecycleWrap = ListenerLifecycleWrap.getOf(bw.context());

            lifecycleWrap.add(topicMapping, bw.raw());
        }
    }
}
