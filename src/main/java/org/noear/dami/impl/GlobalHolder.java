package org.noear.dami.impl;

import org.noear.dami.DamiBus;

/**
 * 全局执行人
 *
 * @author noear
 * @since 1.0
 */
public class GlobalHolder {
    static final DamiBus<String, String> str = new DamiBusImpl<>();
    static final DamiBus obj = new DamiBusImpl<>();


    public static DamiBus<String, String> str() {
        return GlobalHolder.str;
    }

    public static DamiBus obj() {
        return GlobalHolder.obj;
    }
}
