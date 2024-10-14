package org.noear.dami.solon;

import org.noear.dami.Dami;
import org.noear.dami.bus.TopicListener;
import org.noear.dami.solon.annotation.DamiTopic;
import org.noear.solon.Solon;
import org.noear.solon.core.BeanBuilder;
import org.noear.solon.core.BeanWrap;

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
            bw.context().wrapAndPut(clz, raw);
        } else {
            //增加代理支持
            bw.context().beanExtractOrProxy(bw);

            if (TopicListener.class.isAssignableFrom(clz)) {
                Dami.bus().listen(anno.value(), anno.index(), bw.raw());
            } else {
                Dami.api().registerListener(anno.value(), anno.index(), bw.raw());
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
