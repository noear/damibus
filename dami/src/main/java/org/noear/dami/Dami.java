package org.noear.dami;

import org.noear.dami.api.DamiApi;
import org.noear.dami.api.DamiApiImpl;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.DamiBusImpl;
import org.noear.dami.bus.Interceptor;

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

    /**
     * 拦截
     *
     * @param interceptor 拦截器
     */
    public static void intercept(Interceptor interceptor) {
        intercept(0, interceptor);
    }

    /**
     * 拦截
     *
     * @param index       顺序位
     * @param interceptor 拦截器
     */
    public static void intercept(int index, Interceptor interceptor) {
        bus.intercept(index, interceptor);
    }
}
