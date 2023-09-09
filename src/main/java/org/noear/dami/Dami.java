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
    static final DamiBus bus = new DamiBusImpl<>();
    static final DamiApi api = new DamiApiImpl();

    /**
     * 接口界面
     */
    public static DamiApi api() {
        return api;
    }

    /**
     * 弱类型总线界面（适合类隔离的场景）
     */
    public static DamiBus<String, String> strBus() {
        return bus;
    }

    /**
     * 泛型、强类型总线界面
     */
    public static <C, R> DamiBus<C, R> objBus() {
        return bus;
    }
}
