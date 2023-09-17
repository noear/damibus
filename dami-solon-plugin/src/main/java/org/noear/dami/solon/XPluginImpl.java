package org.noear.dami.solon;

import org.noear.dami.Dami;
import org.noear.dami.api.Coder;
import org.noear.dami.bus.Interceptor;
import org.noear.dami.solon.annotation.DamiTopic;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;

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
    }
}
