/*
 * Copyright 2023～ noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.noear.dami2;

import org.noear.dami2.lpc.DamiLpc;
import org.noear.dami2.lpc.DamiLpcImpl;
import org.noear.dami2.bus.DamiBus;
import org.noear.dami2.bus.DamiBusImpl;

import java.util.*;

/**
 * 大米，本地过程调用框架
 *
 * @author noear
 * @since 1.0
 */
public class Dami {
    static DamiBus bus = new DamiBusImpl();
    static DamiLpc lpc = new DamiLpcImpl(Dami::bus);

    /**
     * 总线界面
     */
    public static DamiBus bus() {
        return bus;
    }

    /**
     * 接口界面
     */
    public static DamiLpc lpc() {
        return lpc;
    }

    /// ///////////////////

    /**
     * 新建总线界面（一般用于测试不相互干扰）
     */
    public static DamiBus newBus() {
        return new DamiBusImpl();
    }

    /**
     * 新建接口界面（一般用于测试不相互干扰）
     */
    public static DamiLpc newLpc() {
        return new DamiLpcImpl(newBus());
    }

}