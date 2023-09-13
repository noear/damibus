package org.noear.dami;

import org.noear.dami.api.DamiApi;
import org.noear.dami.api.DamiApiImpl;
import org.noear.dami.bus.DamiBus;
import org.noear.dami.bus.Interceptor;
import org.noear.dami.bus.plus.DamiBusPlus;
import org.noear.dami.bus.plus.DamiBusPlusImpl;
import org.noear.dami.bus.plus.DamiBusTyped;

/**
 * 大米，本地过程调用框架
 *
 * @author noear
 * @since 1.0
 */
public class Dami {
    static final DamiBusPlus bus = new DamiBusPlusImpl<>();
    static final DamiApi api = new DamiApiImpl(bus);


    /**
     * 泛型、强类型总线界面
     */
    public static <C, R> DamiBus<C, R> bus() {
        return bus;
    }

    /**
     * 弱类型总线界面（适合类隔离的场景）
     */
    public static DamiBus<String, String> busStr() {
        return bus;
    }

    /**
     * 类化版总线界面（内容类型直接做为主题，适合做广播）
     * */
    public static DamiBusTyped busTyped(){
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
