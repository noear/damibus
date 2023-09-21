package org.noear.dami;

import org.noear.dami.api.DamiApi;
import org.noear.dami.bus.DamiBus;

/**
 * 大米配置器
 *
 * @author noear
 * @since 1.0
 */
public class DamiConfig {
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
