package org.noear.dami.impl;

import org.noear.dami.DamiBus;

/**
 * 全局执行人
 *
 * @author noear
 * @since 1.0
 */
public class GlobalHolder {
    static final DamiBus global = new DamiBusImpl<>();

    /**
     * 获取全局实例
     */
    public static DamiBus global() {
        return GlobalHolder.global;
    }
}
