package org.noear.dami;

import org.noear.dami.api.Coder;
import org.noear.dami.api.DamiApi;
import org.noear.dami.api.DamiApiConfigurator;
import org.noear.dami.bus.*;

/**
 * 大米配置器
 *
 * @author noear
 * @since 1.0
 */
public class DamiConfig {
    /**
     * 配置总线的主体路由器
     *
     * @param topicRouter 主题路由器
     */
    public static void configure(TopicRouter topicRouter) {
        if (topicRouter != null) {
            ((DamiBusConfigurator) Dami.bus).topicRouter(topicRouter);
        }
    }

    /**
     * 配置总线的负载工厂
     *
     * @param payloadFactory 负载工厂
     */
    public static void configure(PayloadFactory payloadFactory) {
        if (payloadFactory != null) {
            ((DamiBusConfigurator) Dami.bus).payloadFactory(payloadFactory);
        }
    }


    /**
     * 配置总线的超时
     *
     * @param timeout 超时（单位：毫秒）
     */
    public static void configure(long timeout) {
        if (timeout > 0) {
            ((DamiBusConfigurator) Dami.bus).timeout(timeout);
        }
    }


    /**
     * 配置接口的编解码器
     *
     * @param coder 编解码器
     */
    public static void configure(Coder coder) {
        if (coder != null) {
            ((DamiApiConfigurator) Dami.api).coder(coder);
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
