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
    public static void configure(DamiBus bus) {
        if (bus != null) {
            Dami.bus = bus;
        }
    }

    public static void configure(DamiApi api) {
        if (api != null) {
            Dami.api = api;
        }
    }
}
