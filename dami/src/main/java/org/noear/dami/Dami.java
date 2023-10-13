package org.noear.dami;

import org.noear.dami.api.DamiApi;
import org.noear.dami.api.DamiApiImpl;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;

/**
 * 大米，本地过程调用框架
 *
 * @author noear
 * @since 1.0
 */
public class Dami {
    static DamiBus bus = new DamiBusImpl<>();
    static DamiApi api = new DamiApiImpl(Dami::bus);

    /**
     * 总线界面
     */
    public static <C, R> DamiBus<C, R> bus() {
        return bus;
    }

    /**
     * 接口界面
     */
    public static DamiApi api() {
        return api;
    }
}
