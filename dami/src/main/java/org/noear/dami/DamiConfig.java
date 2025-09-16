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
package org.noear.dami;

import org.noear.dami.api.Coder;
import org.noear.dami.api.DamiApi;
import org.noear.dami.api.DamiApiConfigurator;
import org.noear.dami.bus.*;

/**
 * 大米配置器
 *
 * @author noear
 * @since 1.0
 */
public class DamiConfig {
    /**
     * 配置总线的主体路由器
     *
     * @param topicRouter 主题路由器
     */
    public static void configure(TopicRouter topicRouter) {
        if (topicRouter != null) {
            ((DamiBusConfigurator) Dami.bus).topicRouter(topicRouter);
        }
    }

    /**
     * 配置总线的主题调度器
     *
     * @param topicDispatcher 主题调度器
     */
    public static void configure(TopicDispatcher topicDispatcher) {
        if (topicDispatcher != null) {
            ((DamiBusConfigurator) Dami.bus).topicDispatcher(topicDispatcher);
        }
    }

    /**
     * 配置总线的负载工厂
     *
     * @param messageFactory 负载工厂
     */
    public static void configure(MessageFactory messageFactory) {
        if (messageFactory != null) {
            ((DamiBusConfigurator) Dami.bus).messageFactory(messageFactory);
        }
    }


    /**
     * 配置接口的编解码器
     *
     * @param coder 编解码器
     */
    public static void configure(Coder coder) {
        if (coder != null) {
            ((DamiApiConfigurator) Dami.api).coder(coder);
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
     * @param api 接口实例
     */
    public static void configure(DamiApi api) {
        if (api != null) {
            Dami.api = api;
        }
    }
}
