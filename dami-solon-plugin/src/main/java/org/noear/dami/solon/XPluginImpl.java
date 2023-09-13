package org.noear.dami.solon;

import org.noear.dami.Dami;
import org.noear.dami.api.Coder;
import org.noear.dami.bus.Interceptor;
import org.noear.dami.bus.plus.TopicContentListener;
import org.noear.dami.solon.annotation.DamiTopic;
import org.noear.solon.Solon;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.DamiTopicBeanBuilder;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.util.GenericUtil;

/**
 * @author noear
 * @since 1.0
 */
public class XPluginImpl implements Plugin {
    @Override
    public void start(AppContext context) throws Throwable {
        context.beanBuilderAdd(DamiTopic.class, new DamiTopicBeanBuilder());

        context.subWrapsOfType(Interceptor.class, wrap -> {
            Dami.intercept(wrap.index(), wrap.raw());
        });

        context.getBeanAsync(Coder.class, bean -> {
            Dami.api().setCoder(bean);
        });

        context.subWrapsOfType(TopicContentListener.class, warp->{
            Class<?>[] targs = GenericUtil.resolveTypeArguments(warp.clz(), TopicContentListener.class);
            Dami.busTyped().listen(targs[0], warp.raw());
            lifecycleWrap(warp, warp.clz().getName());
        });
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
