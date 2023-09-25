package org.noear.dami;

import org.noear.dami.api.DamiApi;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;
import org.noear.dami.bus.TopicRouter;

/**
 * 大米配置器
 *
 * @author noear
 * @since 1.0
 */
public class DamiConfig {
    /**
     * 配置总线实例（根据主题路由器自动配置）
     *
     * @param topicRouter 主题路由器
     */
    public static void configure(TopicRouter topicRouter) {
        if (topicRouter != null) {
            Dami.bus = new DamiBusImpl(topicRouter);
        }
    }

    /**
     * 配置总线实例
     *
     * @param bus 总线实例
     */
    public static void configure(DamiBus bus) {
        if (bus != null) {
            Dami.bus = bus;
        }
    }

    /**
     * 配置接口实例
     *
     * @param api 接口实例
     */
    public static void configure(DamiApi api) {
        if (api != null) {
            Dami.api = api;
        }
    }
}
