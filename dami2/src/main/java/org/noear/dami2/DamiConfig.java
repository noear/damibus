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

import org.noear.dami2.bus.route.EventRouter;
import org.noear.dami2.lpc.Coder;
import org.noear.dami2.lpc.DamiLpc;
import org.noear.dami2.lpc.DamiLpcConfigurator;
import org.noear.dami2.bus.*;

/**
 * 大米配置器
 *
 * @author noear
 * @since 1.0
 */
public class DamiConfig {
    /**
     * 配置总线的事件路由器
     *
     * @param eventRouter 事件路由器
     */
    public static void configure(EventRouter eventRouter) {
        if (eventRouter != null) {
            ((DamiBusConfigurator) Dami.bus).eventRouter(eventRouter);
        }
    }

    /**
     * 配置总线的事件调度器
     *
     * @param eventDispatcher 事件调度器
     */
    public static void configure(EventDispatcher eventDispatcher) {
        if (eventDispatcher != null) {
            ((DamiBusConfigurator) Dami.bus).eventDispatcher(eventDispatcher);
        }
    }

    /**
     * 配置总线的事件工厂
     *
     * @param eventFactory 事件工厂
     */
    public static void configure(EventFactory eventFactory) {
        if (eventFactory != null) {
            ((DamiBusConfigurator) Dami.bus).eventFactory(eventFactory);
        }
    }


    /**
     * 配置接口的编解码器
     *
     * @param coder 编解码器
     */
    public static void configure(Coder coder) {
        if (coder != null) {
            ((DamiLpcConfigurator) Dami.lpc).coder(coder);
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
     * @param lpc 接口实例
     */
    public static void configure(DamiLpc lpc) {
        if (lpc != null) {
            Dami.lpc = lpc;
        }
    }
}
